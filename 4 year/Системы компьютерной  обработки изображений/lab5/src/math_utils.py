"""
Математические утилиты для работы с 3D векторами.
Оптимизировано с помощью numba для ускорения.
"""

import numpy as np
from numba import njit


@njit(cache=True, fastmath=True)
def normalize(v):
    """Нормализация вектора (приведение к единичной длине)."""
    length = np.sqrt(v[0]**2 + v[1]**2 + v[2]**2)
    if length < 1e-10:
        return np.zeros(3)
    return v / length


@njit(cache=True, fastmath=True)
def dot(a, b):
    """Скалярное произведение двух векторов."""
    return a[0]*b[0] + a[1]*b[1] + a[2]*b[2]


@njit(cache=True, fastmath=True)
def cross(a, b):
    """Векторное произведение двух векторов."""
    return np.array([
        a[1]*b[2] - a[2]*b[1],
        a[2]*b[0] - a[0]*b[2],
        a[0]*b[1] - a[1]*b[0]
    ])


@njit(cache=True, fastmath=True)
def length(v):
    """Длина вектора."""
    return np.sqrt(v[0]**2 + v[1]**2 + v[2]**2)


@njit(cache=True, fastmath=True)
def reflect(direction, normal):
    """Зеркальное отражение вектора direction относительно нормали."""
    return direction - normal * (2.0 * dot(direction, normal))


@njit(cache=True, fastmath=True)
def random_cosine_hemisphere(normal):
    """
    Генерация случайного направления в полусфере с косинус-взвешенным 
    распределением (для диффузного отражения по Ламберту).
    """
    # Случайные числа для сферических координат
    r1 = np.random.random()
    r2 = np.random.random()

    # Косинус-взвешенное распределение
    phi = 2.0 * np.pi * r1
    cos_theta = np.sqrt(r2)
    sin_theta = np.sqrt(1.0 - r2)

    # Локальные координаты
    x = np.cos(phi) * sin_theta
    y = np.sin(phi) * sin_theta
    z = cos_theta

    # Строим локальный базис от нормали
    if abs(normal[0]) > 0.9:
        up = np.array([0.0, 1.0, 0.0])
    else:
        up = np.array([1.0, 0.0, 0.0])

    tangent = normalize(cross(up, normal))
    bitangent = cross(normal, tangent)

    # Преобразуем в мировые координаты
    return normalize(tangent * x + bitangent * y + normal * z)
