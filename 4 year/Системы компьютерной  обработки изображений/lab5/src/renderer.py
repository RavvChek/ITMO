"""
Ядро рендеринга методом трассировки путей (Path Tracing).
"""

import numpy as np
from numba import njit, prange
from .math_utils import dot, reflect, random_cosine_hemisphere, length
from .scene import intersect_scene, sample_light_point
from .camera import get_ray


@njit(cache=True, fastmath=True)
def trace_path(ray_origin, ray_dir, vertices, normals, material_ids,
               diffuse, specular, emission,
               light_indices, light_areas, total_light_area,
               max_depth, rr_depth):
    """
    Трассировка одного пути.
    
    Алгоритм:
    1. Находим пересечение луча со сценой
    2. Если попали в источник света - возвращаем его излучение
    3. Иначе выбираем тип отражения (диффузное или зеркальное)
    4. Для диффузного - вычисляем прямое освещение + продолжаем путь
    5. Применяем русскую рулетку для обрыва пути
    
    Параметры:
        max_depth: максимальная глубина трассировки
        rr_depth: глубина, с которой начинается русская рулетка
    """
    color = np.zeros(3)           # накопленный цвет
    throughput = np.ones(3)       # коэффициент пропускания пути
    
    current_origin = ray_origin.copy()
    current_dir = ray_dir.copy()
    
    for depth in range(max_depth):
        # 1. Ищем пересечение со сценой
        t, hit_point, normal, mat_id = intersect_scene(
            current_origin, current_dir, vertices, normals, material_ids
        )
        
        # Луч ушёл в пустоту
        if mat_id < 0:
            break
        
        # Получаем свойства материала
        mat_diffuse = diffuse[mat_id]
        mat_specular = specular[mat_id]
        mat_emission = emission[mat_id]
        
        # 2. Попали в источник света
        if mat_emission[0] > 0 or mat_emission[1] > 0 or mat_emission[2] > 0:
            color = color + throughput * mat_emission
            break
        
        # 3. Русская рулетка (для обрыва маловажных путей)
        total_refl = mat_diffuse + mat_specular
        p_continue = min(max(total_refl[0], max(total_refl[1], total_refl[2])), 0.95)
        
        if depth >= rr_depth:
            if np.random.random() > p_continue:
                break
            throughput = throughput / p_continue
        
        # 4. Выбор типа отражения (выборка по значимости)
        diff_weight = max(mat_diffuse[0], max(mat_diffuse[1], mat_diffuse[2]))
        spec_weight = max(mat_specular[0], max(mat_specular[1], mat_specular[2]))
        total_weight = diff_weight + spec_weight
        
        if total_weight < 1e-6:
            break
        
        p_diffuse = diff_weight / total_weight
        
        # Смещение от поверхности (избегаем self-intersection)
        EPSILON = 0.0001
        
        if np.random.random() < p_diffuse:
            # --- ДИФФУЗНОЕ ОТРАЖЕНИЕ (Ламберт) ---
            offset = normal * EPSILON
            
            # Прямое освещение (Next Event Estimation)
            if len(light_indices) > 0:
                # Выбираем случайную точку на источнике света
                light_point, light_normal, light_emission, pdf = sample_light_point(
                    vertices, normals, emission, material_ids,
                    light_indices, light_areas, total_light_area
                )
                
                # Вектор к источнику
                to_light = light_point - hit_point
                dist = length(to_light)
                light_dir = to_light / dist
                
                # Косинусы для геометрического фактора
                cos_theta = dot(normal, light_dir)
                cos_theta_light = dot(-light_dir, light_normal)
                
                if cos_theta > 0 and cos_theta_light > 0:
                    # Проверяем видимость (теневой луч)
                    shadow_t, _, _, shadow_mat = intersect_scene(
                        hit_point + offset, light_dir, 
                        vertices, normals, material_ids
                    )
                    
                    # Свет виден, если нет препятствий до него
                    if shadow_t < 0 or shadow_t > dist - 0.001:
                        # Вычисляем вклад прямого освещения (NEE)
                        # Геометрический фактор G = cos_theta * cos_theta_light / dist^2
                        geometry = cos_theta * cos_theta_light / (dist * dist)
                        brdf = mat_diffuse / np.pi  # BRDF Ламберта
                        # PDF выборки точки: pdf_total = (area_i / total_area) * (1 / area_i) = 1 / total_area
                        # Формула NEE: L_direct = (L_e * BRDF * G) / pdf_total = L_e * BRDF * G * total_area
                        direct = light_emission * brdf * geometry * total_light_area
                        color = color + throughput * direct
            
            # Продолжаем путь в случайном направлении
            new_dir = random_cosine_hemisphere(normal)
            throughput = throughput * mat_diffuse
            
            current_origin = hit_point + offset
            current_dir = new_dir
            
        else:
            # --- ЗЕРКАЛЬНОЕ ОТРАЖЕНИЕ ---
            new_dir = reflect(current_dir, normal)
            offset = new_dir * EPSILON
            throughput = throughput * mat_specular
            
            current_origin = hit_point + offset
            current_dir = new_dir
    
    return color


@njit(parallel=True, cache=True, fastmath=True)
def render_image(width, height, samples_per_pixel,
                 cam_position, cam_forward, cam_right, cam_up,
                 cam_half_width, cam_half_height,
                 vertices, normals, material_ids,
                 diffuse, specular, emission,
                 light_indices, light_areas, total_light_area,
                 max_depth, rr_depth):
    """
    Рендеринг изображения методом трассировки путей.
    
    Для каждого пикселя запускается samples_per_pixel лучей,
    результаты усредняются.
    
    Использует параллельную обработку строк для ускорения.
    """
    image = np.zeros((height, width, 3))
    
    # Параллельный цикл по строкам
    for y in prange(height):
        for x in range(width):
            pixel_color = np.zeros(3)
            
            # Усреднение по нескольким сэмплам
            for _ in range(samples_per_pixel):
                # Генерируем луч с jitter для антиалиасинга
                origin, direction = get_ray(
                    x, y, width, height,
                    cam_position, cam_forward, cam_right, cam_up,
                    cam_half_width, cam_half_height, True
                )
                
                # Трассируем путь
                sample_color = trace_path(
                    origin, direction,
                    vertices, normals, material_ids,
                    diffuse, specular, emission,
                    light_indices, light_areas, total_light_area,
                    max_depth, rr_depth
                )
                
                pixel_color = pixel_color + sample_color
            
            # Среднее значение
            image[y, x] = pixel_color / samples_per_pixel
    
    return image
