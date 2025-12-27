"""
Сцена: хранение геометрии и материалов.
"""

import numpy as np
from numba import njit
from .geometry import ray_triangle_intersect, compute_triangle_normal, triangle_area, sample_triangle_point
from .math_utils import dot, length


class Scene:
    """
    Контейнер для 3D сцены.

    Хранит:
        - Треугольники (вершины)
        - Материалы (диффузный цвет, зеркальность, излучение)
        - Источники света
    """

    def __init__(self):
        # Списки для построения сцены
        self._triangles = []     # (v0, v1, v2) для каждого треугольника
        self._material_ids = []  # индекс материала для каждого треугольника
        self._materials = []     # список материалов
        self._light_indices = [] # индексы светящихся треугольников

        # Финальные numpy массивы (создаются при compile())
        self.vertices = None     # shape: (n_triangles, 3, 3) - вершины
        self.normals = None      # shape: (n_triangles, 3) - нормали
        self.areas = None        # shape: (n_triangles,) - площади
        self.material_ids = None # shape: (n_triangles,) - индексы материалов

        # Материалы
        self.diffuse = None      # shape: (n_materials, 3) - диффузный цвет
        self.specular = None     # shape: (n_materials, 3) - зеркальность
        self.emission = None     # shape: (n_materials, 3) - излучение

        # Источники света
        self.light_indices = None
        self.light_areas = None
        self.total_light_area = 0.0

    def add_material(self, diffuse=(0.5, 0.5, 0.5), specular=(0.0, 0.0, 0.0),
                     emission=(0.0, 0.0, 0.0), warn_on_violation=True):
        """
        Добавляет материал.

        Параметры:
            diffuse: цвет диффузного отражения (Ламберт)
            specular: коэффициент зеркального отражения
            emission: излучение (для источников света)
            warn_on_violation: выдавать предупреждение при нарушении физичности

        Возвращает индекс материала.
        
        Физичность материала: для каждого канала RGB должно выполняться
        diffuse + specular ≤ 1 (суммарное отражение не превышает 1).
        """
        diffuse_arr = np.array(diffuse, dtype=np.float64)
        specular_arr = np.array(specular, dtype=np.float64)
        emission_arr = np.array(emission, dtype=np.float64)
        
        # Проверка физичности: diffuse + specular ≤ 1 для каждого канала
        total_reflection = diffuse_arr + specular_arr
        violations = total_reflection > 1.0
        
        if np.any(violations) and warn_on_violation:
            channels = ['R', 'G', 'B']
            violated_channels = [channels[i] for i in range(3) if violations[i]]
            print(f"Предупреждение: материал нарушает физичность модели!")
            print(f"  Каналы с нарушением: {', '.join(violated_channels)}")
            for i, ch in enumerate(channels):
                if violations[i]:
                    print(f"  {ch}: diffuse={diffuse_arr[i]:.3f} + specular={specular_arr[i]:.3f} = {total_reflection[i]:.3f} > 1.0")
        
        self._materials.append((diffuse_arr, specular_arr, emission_arr))
        return len(self._materials) - 1

    def add_triangle(self, v0, v1, v2, material_id):
        """Добавляет треугольник с заданным материалом."""
        idx = len(self._triangles)
        self._triangles.append((
            np.array(v0, dtype=np.float64),
            np.array(v1, dtype=np.float64),
            np.array(v2, dtype=np.float64)
        ))
        self._material_ids.append(material_id)

        # Если материал светится - это источник света
        if np.max(self._materials[material_id][2]) > 0:
            self._light_indices.append(idx)

    def add_quad(self, v0, v1, v2, v3, material_id):
        """Добавляет четырёхугольник как два треугольника."""
        self.add_triangle(v0, v1, v2, material_id)
        self.add_triangle(v0, v2, v3, material_id)

    def compile(self):
        """
        Компилирует сцену в numpy массивы для быстрого доступа.
        Вызывать после добавления всей геометрии.
        """
        n_tris = len(self._triangles)
        n_mats = len(self._materials)

        # Копируем вершины
        self.vertices = np.zeros((n_tris, 3, 3), dtype=np.float64)
        for i, (v0, v1, v2) in enumerate(self._triangles):
            self.vertices[i, 0] = v0
            self.vertices[i, 1] = v1
            self.vertices[i, 2] = v2

        # Индексы материалов
        self.material_ids = np.array(self._material_ids, dtype=np.int32)

        # Предвычисляем нормали и площади
        self.normals = np.zeros((n_tris, 3), dtype=np.float64)
        self.areas = np.zeros(n_tris, dtype=np.float64)
        for i in range(n_tris):
            self.normals[i] = compute_triangle_normal(
                self.vertices[i, 0], self.vertices[i, 1], self.vertices[i, 2]
            )
            self.areas[i] = triangle_area(
                self.vertices[i, 0], self.vertices[i, 1], self.vertices[i, 2]
            )

        # Материалы
        self.diffuse = np.zeros((n_mats, 3), dtype=np.float64)
        self.specular = np.zeros((n_mats, 3), dtype=np.float64)
        self.emission = np.zeros((n_mats, 3), dtype=np.float64)
        for i, (d, s, e) in enumerate(self._materials):
            self.diffuse[i] = d
            self.specular[i] = s
            self.emission[i] = e

        # Источники света
        self.light_indices = np.array(self._light_indices, dtype=np.int32)
        if len(self._light_indices) > 0:
            self.light_areas = self.areas[self.light_indices]
            self.total_light_area = np.sum(self.light_areas)
        else:
            self.light_areas = np.array([], dtype=np.float64)
            self.total_light_area = 0.0

        print(f"Сцена: {n_tris} треугольников, {n_mats} материалов, "
              f"{len(self._light_indices)} источников света")


@njit(cache=True, fastmath=True)
def intersect_scene(ray_origin, ray_dir, vertices, normals, material_ids):
    """
    Поиск ближайшего пересечения луча со сценой.
    
    Перебирает все треугольники и находит ближайшее пересечение.
    
    Возвращает: (t, hit_point, normal, material_id)
        t: расстояние до пересечения (-1 если нет)
        hit_point: точка пересечения
        normal: нормаль в точке пересечения
        material_id: индекс материала (-1 если нет пересечения)
    """
    closest_t = np.inf
    hit_idx = -1

    n_triangles = vertices.shape[0]

    # Перебираем все треугольники
    for i in range(n_triangles):
        t = ray_triangle_intersect(
            ray_origin, ray_dir,
            vertices[i, 0], vertices[i, 1], vertices[i, 2]
        )
        if t > 0 and t < closest_t:
            closest_t = t
            hit_idx = i

    # Нет пересечения
    if hit_idx < 0:
        return -1.0, np.zeros(3), np.zeros(3), -1

    # Вычисляем точку пересечения
    hit_point = ray_origin + ray_dir * closest_t
    normal = normals[hit_idx].copy()

    # Ориентируем нормаль к источнику луча
    if dot(normal, ray_dir) > 0:
        normal = -normal

    return closest_t, hit_point, normal, material_ids[hit_idx]


@njit(cache=True, fastmath=True)
def sample_light_point(vertices, normals, emission, material_ids,
                       light_indices, light_areas, total_light_area):
    """
    Выборка случайной точки на источнике света.
    Источник выбирается пропорционально площади (выборка по значимости).

    Возвращает: (point, normal, emission, pdf)
    """
    n_lights = len(light_indices)
    if n_lights == 0:
        return np.zeros(3), np.zeros(3), np.zeros(3), 0.0

    # Выбираем источник пропорционально площади
    r = np.random.random() * total_light_area
    cumsum = 0.0
    light_idx = 0
    for i in range(n_lights):
        cumsum += light_areas[i]
        if r <= cumsum:
            light_idx = i
            break

    # Получаем треугольник источника
    tri_idx = light_indices[light_idx]

    # Случайная точка на треугольнике
    point = sample_triangle_point(
        vertices[tri_idx, 0], vertices[tri_idx, 1], vertices[tri_idx, 2]
    )
    normal = normals[tri_idx]
    mat_id = material_ids[tri_idx]
    emiss = emission[mat_id]

    # PDF для точки на выбранном источнике (условный PDF)
    # Общий PDF = (area_i / total_area) * (1 / area_i) = 1 / total_area
    # Это учитывается в формуле NEE умножением на total_light_area
    pdf = 1.0 / light_areas[light_idx]

    return point, normal, emiss, pdf
