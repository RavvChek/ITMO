#include <math.h>
#include <stdio.h>

typedef struct {
    float x;
    float y;
    float z;
} Vector3;

typedef struct {
    float r;
    float g;
    float b;
} Color;

// Вычитание векторов
Vector3 subtract(const Vector3 a, const Vector3 b) {
    Vector3 result;
    result.x = a.x - b.x;
    result.y = a.y - b.y;
    result.z = a.z - b.z;
    return result;
}

// Сложение векторов
Vector3 add(const Vector3 a, const Vector3 b) {
    Vector3 result;
    result.x = a.x + b.x;
    result.y = a.y + b.y;
    result.z = a.z + b.z;
    return result;
}

// Умножение вектора на скаляр
Vector3 multiply_scalar(const Vector3 v, const float scalar) {
    Vector3 result;
    result.x = v.x * scalar;
    result.y = v.y * scalar;
    result.z = v.z * scalar;
    return result;
}

// Скалярное произведение
float dot(const Vector3 a, const Vector3 b) {
    return a.x * b.x + a.y * b.y + a.z * b.z;
}

// Векторное произведение
Vector3 cross(const Vector3 a, const Vector3 b) {
    Vector3 result;
    result.x = a.y * b.z - a.z * b.y;
    result.y = a.z * b.x - a.x * b.z;
    result.z = a.x * b.y - a.y * b.x;
    return result;
}

// Длина вектора
float magnitude(const Vector3 v) {
    return sqrtf(dot(v, v));
}

// Нормализация вектора
Vector3 normalize(const Vector3 v) {
    const float mag = magnitude(v);
    if (mag == 0.0f) {
        const Vector3 zero = {0.0f, 0.0f, 0.0f};
        return zero;
    }
    return multiply_scalar(v, 1.0f / mag);
}

// Перевод в глобальные координаты
Vector3 compute_PT(const Vector3 P0, const Vector3 P1, const Vector3 P2, const float x, const float y) {
    const Vector3 dir1 = subtract(P1, P0);
    const Vector3 dir2 = subtract(P2, P0);
    const Vector3 offset = add(multiply_scalar(dir1, x), multiply_scalar(dir2, y));
    return add(P0, offset);
}

// Вычисление нормали к плоскости треугольника
Vector3 compute_N(const Vector3 P0, const Vector3 P1, const Vector3 P2) {
    const Vector3 v1 = subtract(P1, P0);
    const Vector3 v2 = subtract(P2, P0);
    const Vector3 cross_prod = cross(v1, v2);
    return normalize(cross_prod);
}

// Вычисление вектора s
Vector3 compute_s(const Vector3 PT, const Vector3 PL) {
    return subtract(PL, PT);
}

// Вычисление cos theta
float compute_cos_theta(const Vector3 s, const Vector3 O) {
    const Vector3 norm_O = normalize(O);
    const float mag_s = magnitude(s);
    if (mag_s == 0.0f) return 0.0f;
    return fmaxf(0.0f, dot(multiply_scalar(normalize(s), -1.0f), norm_O));
}

// Вычисление cos alpha
float compute_cos_alpha(const Vector3 s, const Vector3 N) {
    const float mag_s = magnitude(s);
    if (mag_s == 0.0f) return 0.0f;
    return fmaxf(0.0f, dot(normalize(s), N));
}


// Вычисление интенсивности
Color compute_I(const Color I0, const float cos_theta) {
    Color result;
    result.r = I0.r * cos_theta;
    result.g = I0.g * cos_theta;
    result.b = I0.b * cos_theta;
    return result;
}

// Вычисление освещенности
Color compute_E(const Color I, const float cos_alpha, const float R_squared) {
    if (R_squared <= 0.0f) return I;
    const float factor = cos_alpha / R_squared;
    Color result;
    result.r = I.r * factor;
    result.g = I.g * factor;
    result.b = I.b * factor;
    return result;
}

// Вычисление освещённости в точке
Color compute_illumination(const Vector3 P0, const Vector3 P1, const Vector3 P2,
                         const float x, const float y,
                         const Vector3 PL, const Vector3 O, const Color I0) {
    const Vector3 PT = compute_PT(P0, P1, P2, x, y);
    const Vector3 N = compute_N(P0, P1, P2);
    const Vector3 s = compute_s(PT, PL);
    const float R = magnitude(s);
    const float R_squared = R * R;

    const float cos_theta = compute_cos_theta(s, O);
    const float cos_alpha = compute_cos_alpha(s, N);

    const Color I = compute_I(I0, cos_theta);
    return compute_E(I, cos_alpha, R_squared);
}

int main(void) {
    // Вершины треугольника
    const Vector3 P0 = {0.0f, 0.0f, 0.0f};
    const Vector3 P1 = {4.0f, 0.0f, 0.0f};
    const Vector3 P2 = {2.0f, 4.0f, 2.0f};

    // Позиция источника света над треугольником
    const Vector3 PL = {-2.0f, 1.0f, 3.0f};

    // Начальная интенсивность света
    const Color I0 = {50.0f, 100.0f, 80.0f};

    // Направление оси света
    const Vector3 O = {0.0f, 0.0f, -1.0f};

    for (int y = 0; y <= 4; y++) {
        for (int x = 0; x <= 4; x++) {
            const Color E = compute_illumination(P0, P1, P2, (float)x/4, (float)y/4, PL, O, I0);
            printf("(%.2f, %.2f, %.2f) ", E.r, E.g, E.b);
        }
        printf("\n");
    }

    return 0;
}
