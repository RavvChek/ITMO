"""
Точечная (pinhole) камера для рендеринга.
"""

import numpy as np
from numba import njit
from .math_utils import normalize, cross


class Camera:
    """
    Точечная камера.
    
    Параметры:
        position: позиция камеры в пространстве
        look_at: точка, на которую смотрит камера
        up: вектор "вверх" (обычно [0, 1, 0])
        fov: угол обзора по вертикали в градусах
        width, height: размер изображения в пикселях
    """

    def __init__(self, position, look_at, up, fov, width, height):
        self.position = np.array(position, dtype=np.float64)
        self.width = width
        self.height = height

        # Вычисляем базис камеры (forward, right, up)
        self.forward = normalize(np.array(look_at, dtype=np.float64) - self.position)
        self.right = normalize(cross(self.forward, np.array(up, dtype=np.float64)))
        self.up = cross(self.right, self.forward)

        # Размер виртуального экрана (зависит от FOV)
        aspect = width / height
        self.half_height = np.tan(fov * np.pi / 360.0)
        self.half_width = self.half_height * aspect


@njit(cache=True, fastmath=True)
def get_ray(x, y, width, height, position, forward, right, up, 
            half_width, half_height, jitter=True):
    """
    Генерирует луч из камеры через пиксель (x, y).
    
    Параметры:
        x, y: координаты пикселя
        jitter: если True, добавляет случайное смещение для антиалиасинга
    
    Возвращает:
        (origin, direction) - начало и направление луча
    """
    # Случайное смещение внутри пикселя для антиалиасинга
    if jitter:
        px = x + np.random.random()
        py = y + np.random.random()
    else:
        px = x + 0.5
        py = y + 0.5

    # Нормализованные координаты экрана [-1, 1]
    u = (2.0 * px / width - 1.0) * half_width
    v = (1.0 - 2.0 * py / height) * half_height

    # Направление луча
    direction = forward + right * u + up * v
    direction = direction / np.sqrt(direction[0]**2 + direction[1]**2 + direction[2]**2)

    return position.copy(), direction
