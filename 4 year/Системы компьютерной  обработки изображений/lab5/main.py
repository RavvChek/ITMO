
import time
import numpy as np
from src.camera import Camera
from src.cornell_box import create_cornell_box
from src.renderer import render_image
from src.postprocess import tonemap_and_gamma, save_ppm, save_png


CONFIG = {
    # Рендеринг
    'width': 600,
    'height': 600,
    'samples_per_pixel': 512,
    'max_depth': 6,
    'russian_roulette_depth': 2,

    # Камера
    'camera_position': [1.5, 2.5, -7],
    'camera_look_at': [0, 2.0, 0],
    'camera_fov': 50,

    # Сцена
    'room_size': 5.0,
    'left_wall_color': [0.75, 0.25, 0.25],
    'right_wall_color': [0.25, 0.25, 0.75],
    'wall_color': [0.75, 0.75, 0.75],

    # Источник света
    'light_intensity': 10.0,
    'light_color': [1.0, 0.95, 0.9],
    'light_size': 2.0,

    # Куб 1 (диффузный)
    'box1_position': [1.2, 0, -1.0],
    'box1_size': 1.1,
    'box1_rotation': 25,
    'box1_diffuse': [0.75, 0.75, 0.75],
    'box1_specular': [0.0, 0.0, 0.0],

    # Куб 2 (зеркальный)
    'box2_position': [-1.5, 0, 1.2],
    'box2_size': 1.4,
    'box2_rotation': -30,
    'box2_diffuse': [0.05, 0.05, 0.05],
    'box2_specular': [0.9, 0.9, 0.9],

    # Постобработка
    'gamma': 2.2,
    'exposure': 1.5,
    'normalization_method': 'median',  # 'max', 'mean', 'median', 'custom' - медиана устойчива к выбросам
    'normalization_target': None,
    'normalization_target_mean': 0.5,
    'use_tonemap': True,
    'clip_after_normalization': True,  # Обрезать яркие выбросы сразу после нормализации
}


def validate_config(config):
    """Проверка корректности параметров конфигурации."""
    errors = []
    warnings = []
    
    width = config.get('width', 600)
    height = config.get('height', 600)
    if width < 500 or width > 1000:
        errors.append(f"Ширина изображения должна быть в диапазоне 500-1000, получено: {width}")
    if height < 500 or height > 1000:
        errors.append(f"Высота изображения должна быть в диапазоне 500-1000, получено: {height}")
    
    if config.get('samples_per_pixel', 512) <= 0:
        errors.append("samples_per_pixel должен быть положительным")
    if config.get('max_depth', 6) <= 0:
        errors.append("max_depth должен быть положительным")
    if config.get('russian_roulette_depth', 2) < 0:
        errors.append("russian_roulette_depth должен быть неотрицательным")
    if config.get('room_size', 5.0) <= 0:
        errors.append("room_size должен быть положительным")
    if config.get('light_intensity', 15.0) <= 0:
        errors.append("light_intensity должен быть положительным")
    if config.get('light_size', 1.8) <= 0:
        errors.append("light_size должен быть положительным")
    if config.get('gamma', 2.2) <= 0:
        errors.append("gamma должен быть положительным")
    if config.get('exposure', 1.5) <= 0:
        errors.append("exposure должен быть положительным")
    
    fov = config.get('camera_fov', 45)
    if fov <= 0 or fov >= 180:
        errors.append("camera_fov должен быть в диапазоне (0, 180)")
    
    color_params = ['left_wall_color', 'right_wall_color', 'wall_color', 
                    'light_color', 'box1_diffuse', 'box1_specular', 
                    'box2_diffuse', 'box2_specular']
    for param in color_params:
        color = config.get(param)
        if color is not None:
            if len(color) != 3:
                errors.append(f"{param} должен быть массивом из 3 элементов")
            else:
                for i, c in enumerate(color):
                    if c < 0 or c > 1:
                        warnings.append(f"{param}[{i}] = {c} выходит за диапазон [0, 1]")
    
    box1_diffuse = np.array(config.get('box1_diffuse', [0.75, 0.75, 0.75]))
    box1_specular = np.array(config.get('box1_specular', [0.0, 0.0, 0.0]))
    box1_total = box1_diffuse + box1_specular
    if np.any(box1_total > 1.0):
        warnings.append(f"box1 нарушает физичность: diffuse + specular = {box1_total} > 1.0")
    
    box2_diffuse = np.array(config.get('box2_diffuse', [0.05, 0.05, 0.05]))
    box2_specular = np.array(config.get('box2_specular', [0.9, 0.9, 0.9]))
    box2_total = box2_diffuse + box2_specular
    if np.any(box2_total > 1.0):
        warnings.append(f"box2 нарушает физичность: diffuse + specular = {box2_total} > 1.0")
    
    if warnings:
        print("\nПредупреждения конфигурации:")
        for warning in warnings:
            print(f"  ⚠ {warning}")
    
    if errors:
        print("\nОшибки конфигурации:")
        for error in errors:
            print(f"  ✗ {error}")
        raise ValueError("Конфигурация содержит ошибки. Исправьте их перед запуском.")
    
    return True


def main():
    print("=" * 60)
    print("Path Tracer - Трассировка путей")
    print("=" * 60)

    print("\n[0/5] Валидация конфигурации...")
    validate_config(CONFIG)
    print("      Конфигурация корректна")

    print("\n[1/5] Создание сцены...")
    scene = create_cornell_box(CONFIG)

    print("[2/5] Настройка камеры...")
    camera = Camera(
        position=CONFIG['camera_position'],
        look_at=CONFIG['camera_look_at'],
        up=[0, 1, 0],
        fov=CONFIG['camera_fov'],
        width=CONFIG['width'],
        height=CONFIG['height']
    )

    print(f"[3/5] Рендеринг {CONFIG['width']}x{CONFIG['height']}, "
          f"{CONFIG['samples_per_pixel']} сэмплов/пиксель...")

    start_time = time.time()
    image = render_image(
        CONFIG['width'], CONFIG['height'], CONFIG['samples_per_pixel'],
        camera.position, camera.forward, camera.right, camera.up,
        camera.half_width, camera.half_height,
        scene.vertices, scene.normals, scene.material_ids,
        scene.diffuse, scene.specular, scene.emission,
        scene.light_indices, scene.light_areas, scene.total_light_area,
        CONFIG['max_depth'], CONFIG['russian_roulette_depth']
    )
    elapsed = time.time() - start_time
    print(f"      Завершено за {elapsed:.1f} секунд")

    print("[4/5] Постобработка и сохранение...")
    image = tonemap_and_gamma(
        image, 
        gamma=CONFIG['gamma'], 
        exposure=CONFIG['exposure'],
        normalization_method=CONFIG.get('normalization_method', 'median'),
        normalization_target=CONFIG.get('normalization_target'),
        normalization_target_mean=CONFIG.get('normalization_target_mean', 0.5),
        use_tonemap=CONFIG.get('use_tonemap', True),
        clip_after_normalization=CONFIG.get('clip_after_normalization', True)
    )

    save_ppm("output/result.ppm", image)
    save_png("output/result.png", image)

    print("\n" + "=" * 60)
    print("Готово!")
    print("=" * 60)


if __name__ == "__main__":
    main()
