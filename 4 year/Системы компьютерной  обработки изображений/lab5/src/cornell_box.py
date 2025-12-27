"""
Создание сцены Cornell Box.
"""

import numpy as np
from .scene import Scene


def add_box(scene: Scene, position, width: float, height: float,
            depth: float, material_id: int, rotation: float = 0):
    """Добавляет параллелепипед с возможностью поворота по Y."""
    angle = rotation * np.pi / 180
    cos_a, sin_a = np.cos(angle), np.sin(angle)

    def rotate_y(v):
        return np.array([
            v[0] * cos_a - v[2] * sin_a,
            v[1],
            v[0] * sin_a + v[2] * cos_a
        ])

    hw, hh, hd = width/2, height/2, depth/2
    pos = np.array(position)

    # Локальные вершины
    local_verts = [
        np.array([-hw, 0, -hd]),
        np.array([hw, 0, -hd]),
        np.array([hw, 0, hd]),
        np.array([-hw, 0, hd]),
        np.array([-hw, height, -hd]),
        np.array([hw, height, -hd]),
        np.array([hw, height, hd]),
        np.array([-hw, height, hd]),
    ]

    # Применяем поворот и смещение
    verts = [rotate_y(v) + pos for v in local_verts]

    # Грани куба
    faces = [
        (0, 1, 2, 3),  # низ
        (4, 7, 6, 5),  # верх
        (0, 4, 5, 1),  # дальняя
        (2, 6, 7, 3),  # ближняя
        (0, 3, 7, 4),  # левая
        (1, 5, 6, 2),  # правая
    ]

    for face in faces:
        v0, v1, v2, v3 = [verts[i] for i in face]
        scene.add_quad(v0, v1, v2, v3, material_id)


def create_cornell_box(config: dict) -> Scene:
    """
    Создаёт классическую сцену Cornell Box.
    Параметры настраиваются через словарь config.
    """
    scene = Scene()
    size = config.get('room_size', 5.0)

    # Цвета стен
    left_color = config.get('left_wall_color', [0.75, 0.25, 0.25])
    right_color = config.get('right_wall_color', [0.25, 0.25, 0.75])
    white = config.get('wall_color', [0.75, 0.75, 0.75])

    # Параметры кубов
    box1_diffuse = config.get('box1_diffuse', [0.75, 0.75, 0.75])
    box1_specular = config.get('box1_specular', [0.0, 0.0, 0.0])
    box2_diffuse = config.get('box2_diffuse', [0.05, 0.05, 0.05])
    box2_specular = config.get('box2_specular', [0.9, 0.9, 0.9])

    light_intensity = config.get('light_intensity', 15.0)
    light_color = config.get('light_color', [1.0, 1.0, 1.0])

    # Создаём материалы
    mat_white = scene.add_material(diffuse=white)
    mat_red = scene.add_material(diffuse=left_color)
    mat_blue = scene.add_material(diffuse=right_color)
    mat_light = scene.add_material(
        emission=[c * light_intensity for c in light_color]
    )
    mat_diffuse_box = scene.add_material(diffuse=box1_diffuse, specular=box1_specular)
    mat_mirror_box = scene.add_material(diffuse=box2_diffuse, specular=box2_specular)

    # Пол
    scene.add_quad(
        [-size/2, 0, -size/2], [size/2, 0, -size/2],
        [size/2, 0, size/2], [-size/2, 0, size/2],
        mat_white
    )

    # Потолок
    scene.add_quad(
        [-size/2, size, -size/2], [-size/2, size, size/2],
        [size/2, size, size/2], [size/2, size, -size/2],
        mat_white
    )

    # Задняя стена
    scene.add_quad(
        [-size/2, 0, size/2], [size/2, 0, size/2],
        [size/2, size, size/2], [-size/2, size, size/2],
        mat_white
    )

    # Левая стена (красная)
    scene.add_quad(
        [-size/2, 0, -size/2], [-size/2, 0, size/2],
        [-size/2, size, size/2], [-size/2, size, -size/2],
        mat_red
    )

    # Правая стена (синяя)
    scene.add_quad(
        [size/2, 0, size/2], [size/2, 0, -size/2],
        [size/2, size, -size/2], [size/2, size, size/2],
        mat_blue
    )

    # Источник света на потолке
    light_size = config.get('light_size', 1.5)
    y = size - 0.01
    scene.add_quad(
        [-light_size/2, y, -light_size/2], [-light_size/2, y, light_size/2],
        [light_size/2, y, light_size/2], [light_size/2, y, -light_size/2],
        mat_light
    )

    # Диффузный куб (слева)
    box1_pos = config.get('box1_position', [-1.0, 0, 0.5])
    box1_size = config.get('box1_size', 1.5)
    box1_rot = config.get('box1_rotation', -18)
    add_box(scene, box1_pos, box1_size, box1_size * 2, box1_size,
            mat_diffuse_box, rotation=box1_rot)

    # Зеркальный куб (справа)
    box2_pos = config.get('box2_position', [1.2, 0, -0.5])
    box2_size = config.get('box2_size', 1.3)
    box2_rot = config.get('box2_rotation', 15)
    add_box(scene, box2_pos, box2_size, box2_size, box2_size,
            mat_mirror_box, rotation=box2_rot)

    scene.compile()
    return scene
