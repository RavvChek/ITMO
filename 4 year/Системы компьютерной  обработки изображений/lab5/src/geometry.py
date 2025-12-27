"""
Геометрические примитивы: пересечение луча с треугольником.
"""

import numpy as np
from numba import njit
from .math_utils import dot, cross, normalize


@njit(cache=True, fastmath=True)
def ray_triangle_intersect(ray_origin, ray_dir, v0, v1, v2):
    """
    Пересечение луча с треугольником (алгоритм Мёллера-Трумбора).
    
    Параметры:
        ray_origin: начало луча
        ray_dir: направление луча (нормализованное)
        v0, v1, v2: вершины треугольника
    
    Возвращает:
        t - параметр луча (точка = origin + t * dir), или -1.0 если нет пересечения
    """
    EPSILON = 1e-7

    # Рёбра треугольника
    edge1 = v1 - v0
    edge2 = v2 - v0

    # Вычисляем детерминант
    h = cross(ray_dir, edge2)
    a = dot(edge1, h)

    # Луч параллелен плоскости треугольника
    if abs(a) < EPSILON:
        return -1.0

    f = 1.0 / a
    s = ray_origin - v0
    u = f * dot(s, h)

    # Точка вне треугольника по u
    if u < 0.0 or u > 1.0:
        return -1.0

    q = cross(s, edge1)
    v = f * dot(ray_dir, q)

    # Точка вне треугольника по v
    if v < 0.0 or u + v > 1.0:
        return -1.0

    # Вычисляем t
    t = f * dot(edge2, q)

    # Пересечение позади начала луча
    if t < EPSILON:
        return -1.0

    return t


@njit(cache=True, fastmath=True)
def compute_triangle_normal(v0, v1, v2):
    """Вычисляет нормаль треугольника (направлена против часовой стрелки)."""
    edge1 = v1 - v0
    edge2 = v2 - v0
    return normalize(cross(edge1, edge2))


@njit(cache=True, fastmath=True)
def triangle_area(v0, v1, v2):
    """Вычисляет площадь треугольника."""
    edge1 = v1 - v0
    edge2 = v2 - v0
    cross_prod = cross(edge1, edge2)
    return 0.5 * np.sqrt(cross_prod[0]**2 + cross_prod[1]**2 + cross_prod[2]**2)


@njit(cache=True, fastmath=True)
def sample_triangle_point(v0, v1, v2):
    """Случайная точка на треугольнике (равномерное распределение)."""
    r1 = np.random.random()
    r2 = np.random.random()

    # Барицентрические координаты
    sqrt_r1 = np.sqrt(r1)
    u = 1.0 - sqrt_r1
    v = r2 * sqrt_r1

    return v0 + (v1 - v0) * u + (v2 - v0) * v
