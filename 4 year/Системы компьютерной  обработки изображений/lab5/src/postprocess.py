"""
Постобработка: тонемаппинг, гамма-коррекция, сохранение изображений.
"""

import numpy as np


def normalize_image(image, method='max', target_value=None, target_mean=0.5, clip_after_normalization=False):
    """
    Нормировка изображения на некоторое значение.
    
    Параметры:
        image: изображение в абсолютных яркостях (HDR)
        method: метод нормировки
            - 'max': нормировка на максимальную яркость
            - 'mean': нормировка на среднюю яркость (target_mean)
            - 'median': нормировка на медианную яркость (target_mean) - устойчива к выбросам
            - 'custom': нормировка на заданное значение (target_value)
        target_value: целевое значение для метода 'custom'
        target_mean: целевая средняя/медианная яркость для методов 'mean' и 'median' (по умолчанию 0.5)
        clip_after_normalization: обрезать значения выше 1 сразу после нормализации (по умолчанию False)
    
    Возвращает:
        Нормированное изображение в диапазоне [0, inf) или [0, 1] если clip_after_normalization=True
    """
    image = image.copy()
    
    if method == 'max':
        # Нормировка на максимальную яркость
        max_val = np.max(image)
        if max_val > 0:
            image = image / max_val
        else:
            print("Предупреждение: максимальная яркость равна 0, нормировка пропущена")
    
    elif method == 'mean':
        # Нормировка на среднюю яркость
        mean_val = np.mean(image)
        if mean_val > 0:
            image = image * (target_mean / mean_val)
        else:
            print("Предупреждение: средняя яркость равна 0, нормировка пропущена")
    
    elif method == 'median':
        # Нормировка на медианную яркость (более устойчива к выбросам)
        # Вычисляем медиану по всем каналам RGB
        median_val = np.median(image)
        if median_val > 0:
            image = image * (target_mean / median_val)
        else:
            print("Предупреждение: медианная яркость равна 0, нормировка пропущена")
    
    elif method == 'custom':
        # Нормировка на заданное значение
        if target_value is None:
            raise ValueError("Для метода 'custom' необходимо указать target_value")
        if target_value <= 0:
            raise ValueError("target_value должен быть положительным")
        max_val = np.max(image)
        if max_val > 0:
            image = image * (target_value / max_val)
        else:
            print("Предупреждение: максимальная яркость равна 0, нормировка пропущена")
    
    else:
        raise ValueError(f"Неизвестный метод нормировки: {method}. "
                        f"Доступные методы: 'max', 'mean', 'median', 'custom'")
    
    # Обрезание значений выше 1 после нормализации (если требуется)
    if clip_after_normalization:
        image = np.clip(image, 0, 1)
    
    return image


def reinhard_tonemap(image):
    """
    Тонемаппинг Рейнхарда: L_out = L / (1 + L)
    Преобразует HDR изображение в диапазон [0, 1].
    """
    return image / (1.0 + image)


def tonemap_and_gamma(image, gamma=2.2, exposure=1.5, 
                      normalization_method='max', normalization_target=None, 
                      normalization_target_mean=0.5, use_tonemap=True,
                      clip_after_normalization=True):
    """
    Постобработка изображения:
    1. Нормировка на некоторое значение (максимальная/средняя/медианная/заданная яркость)
    2. Опционально: тонемаппинг Рейнхарда (HDR -> LDR)
    3. Отсечение значений выше 1
    4. Гамма-коррекция
    
    Параметры:
        gamma: показатель гамма-коррекции (обычно 2.2)
        exposure: коэффициент экспозиции (яркость) - применяется после нормировки
        normalization_method: метод нормировки ('max', 'mean', 'median', 'custom')
        normalization_target: целевое значение для метода 'custom'
        normalization_target_mean: целевая средняя/медианная яркость для методов 'mean' и 'median' (по умолчанию 0.5)
        use_tonemap: использовать ли тонемаппинг Рейнхарда (по умолчанию True)
        clip_after_normalization: обрезать значения выше 1 сразу после нормализации (по умолчанию True)
    """
    image = image.copy()

    # 1. Нормировка на некоторое значение
    image = normalize_image(image, method=normalization_method, 
                           target_value=normalization_target,
                           target_mean=normalization_target_mean,
                           clip_after_normalization=clip_after_normalization)
    
    # Применяем экспозицию
    image = image * exposure

    # 2. Опционально: тонемаппинг Рейнхарда
    if use_tonemap:
        image = reinhard_tonemap(image)

    # 3. Отсечение значений выше 1 (если еще не было обрезано после нормализации)
    if not clip_after_normalization:
        image = np.clip(image, 0, 1)

    # 4. Гамма-коррекция: V_corr = V^(1/γ)
    image = np.power(image, 1.0 / gamma)

    return image


def save_ppm(filename, image):
    """
    Сохранение в формате PPM (P3 - текстовый).
    
    Формат PPM:
    - P3 - магическое число (текстовый RGB)
    - ширина высота
    - максимальное значение (255)
    - RGB значения пикселей
    """
    height, width = image.shape[:2]
    
    # Преобразуем в 8-бит
    image_8bit = (image * 255).astype(np.uint8)

    with open(filename, 'w') as f:
        f.write(f"P3\n{width} {height}\n255\n")
        for y in range(height):
            row = []
            for x in range(width):
                r, g, b = image_8bit[y, x]
                row.append(f"{r} {g} {b}")
            f.write(" ".join(row) + "\n")

    print(f"Сохранено: {filename}")


def save_png(filename, image):
    """Сохранение в формате PNG (требует PIL/Pillow)."""
    try:
        from PIL import Image
        image_8bit = (image * 255).astype(np.uint8)
        img = Image.fromarray(image_8bit, 'RGB')
        img.save(filename)
        print(f"Сохранено: {filename}")
    except ImportError:
        print("PIL не найден, сохраняю в PPM")
        save_ppm(filename.replace('.png', '.ppm'), image)
