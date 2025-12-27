#include <stdio.h>
#include <math.h>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

#define INV_PI (1.0 / M_PI)

// Структуры
typedef struct {
    double R;
    double G;
    double B;
} RGBIntensity;

typedef struct {
    double x;
    double y;
    double z;
} Vector3D;

typedef struct {
    Vector3D s;       // Вектор ОТ точки К источнику
    double R_squared; // Квадрат расстояния (R^2)
    double cos_alpha; // Косинус угла падения (между N и S)
    double cos_theta; // Косинус угла прожектора (между direction_of_spot и direction_from_source_to_point)
} LightParameters;

// -------------------- Векторные операции --------------------
Vector3D vector_subtract(Vector3D A, Vector3D B) {
    Vector3D result = {A.x - B.x, A.y - B.y, A.z - B.z};
    return result;
}

double vector_length(Vector3D V) {
    return sqrt(V.x * V.x + V.y * V.y + V.z * V.z);
}

Vector3D vector_scale(Vector3D V, double scalar) {
    Vector3D result = {V.x * scalar, V.y * scalar, V.z * scalar};
    return result;
}

Vector3D vector_add(Vector3D A, Vector3D B) {
    Vector3D result = {A.x + B.x, A.y + B.y, A.z + B.z};
    return result;
}

Vector3D vector_cross(Vector3D A, Vector3D B) {
    Vector3D result = {
        A.y * B.z - A.z * B.y,
        A.z * B.x - A.x * B.z,
        A.x * B.y - A.y * B.x
    };
    return result;
}

double vector_dot(Vector3D A, Vector3D B) {
    return A.x * B.x + A.y * B.y + A.z * B.z;
}

Vector3D vector_normalize(Vector3D V) {
    double len = vector_length(V);
    if (len > 1e-12) {
        return vector_scale(V, 1.0 / len);
    } else {
        return (Vector3D){0.0, 0.0, 0.0};
    }
}

// -------------------- RGB операции --------------------
RGBIntensity rgb_multiply(RGBIntensity A, RGBIntensity B) {
    RGBIntensity result;
    result.R = A.R * B.R;
    result.G = A.G * B.G;
    result.B = A.B * B.B;
    return result;
}

RGBIntensity rgb_add(RGBIntensity A, RGBIntensity B) {
    RGBIntensity result = {A.R + B.R, A.G + B.G, A.B + B.B};
    return result;
}

RGBIntensity rgb_scale(RGBIntensity V, double scalar) {
    RGBIntensity result = {V.R * scalar, V.G * scalar, V.B * scalar};
    return result;
}

// -------------------- Освещение --------------------
RGBIntensity calculate_directional_intensity(RGBIntensity I0_RGB, double cos_theta) {
    return rgb_scale(I0_RGB, fmax(0.0, cos_theta));
}

// Цветная освещённость точки (E) от одного источника
RGBIntensity calculate_color_illuminance(RGBIntensity I_RGB_directional, double cos_alpha, double R_squared) {
    if (R_squared <= 1e-12) {
        return (RGBIntensity){0.0, 0.0, 0.0};
    }
    double factor = fmax(0.0, cos_alpha) / R_squared;
    return rgb_scale(I_RGB_directional, factor);
}

// -------------------- Геометрия --------------------
Vector3D local_to_global_coords(Vector3D P0, Vector3D P1, Vector3D P2, double local_x, double local_y) {
    Vector3D V10 = vector_subtract(P1, P0);
    Vector3D V20 = vector_subtract(P2, P0);
    Vector3D Displacement1 = vector_scale(V10, local_x);
    Vector3D Displacement2 = vector_scale(V20, local_y);
    Vector3D TotalDisplacement = vector_add(Displacement1, Displacement2);
    Vector3D PT = vector_add(P0, TotalDisplacement);
    return PT;
}

// Нормаль треугольника (единичная)
Vector3D calculate_triangle_normal(Vector3D P0, Vector3D P1, Vector3D P2) {
    Vector3D A = vector_subtract(P1, P0);
    Vector3D B = vector_subtract(P2, P0);
    Vector3D N_unnormalized = vector_cross(A, B);
    return vector_normalize(N_unnormalized);
}

// Центроид треугольника
Vector3D triangle_centroid(Vector3D P0, Vector3D P1, Vector3D P2) {
    Vector3D sum = vector_add(P0, vector_add(P1, P2));
    return vector_scale(sum, 1.0 / 3.0);
}

// Параметры света
LightParameters calculate_light_params(Vector3D P_T, Vector3D P_L, Vector3D N_plane, Vector3D O_spot) {
    LightParameters params;
    params.s = vector_subtract(P_L, P_T);
    params.R_squared = vector_dot(params.s, params.s);

    if (params.R_squared < 1e-24) {
        params.cos_alpha = 0.0;
        params.cos_theta = 0.0;
        return params;
    }

    Vector3D s_normalized = vector_normalize(params.s);
    Vector3D O_normalized = vector_normalize(O_spot);
    params.cos_alpha = vector_dot(s_normalized, N_plane);
    params.cos_theta = -vector_dot(s_normalized, O_normalized);

    return params;
}

// -------------------- BRDF --------------------
RGBIntensity calculate_brdf_blinn_phong(RGBIntensity K_RGB, double kd, double ks, double ke, Vector3D h, Vector3D N) {
    double NdH = fmax(0.0, vector_dot(N, h));
    double diffuse = kd * INV_PI;
    double spec = ks * ((ke + 8.0) * INV_PI * 0.125) * pow(NdH, ke);
    return rgb_scale(K_RGB, diffuse + spec);
}

Vector3D calculate_halfway_vector(Vector3D v, Vector3D s) {
    Vector3D V_sum = vector_add(v, s);
    return vector_normalize(V_sum);
}

// -------------------- Функции высокого уровня --------------------
// Освещённость от одного источника (для таблиц E1/E2)
RGBIntensity calculate_E_RGB_PT(
    Vector3D P0, Vector3D P1, Vector3D P2,
    double local_x, double local_y,
    RGBIntensity I0_RGB,
    Vector3D PL, Vector3D O_spot)
{
    Vector3D PT = local_to_global_coords(P0, P1, P2, local_x, local_y);
    Vector3D N = calculate_triangle_normal(P0, P1, P2);
    if (vector_length(N) < 1e-12) {
        return (RGBIntensity){0.0, 0.0, 0.0};
    }

    LightParameters params = calculate_light_params(PT, PL, N, O_spot);
    RGBIntensity I_dir = calculate_directional_intensity(I0_RGB, params.cos_theta);
    return calculate_color_illuminance(I_dir, params.cos_alpha, params.R_squared);
}

// Яркость в направлении камеры с учётом двух источников
RGBIntensity calculate_L_RGB_PT(
    Vector3D P0, Vector3D P1, Vector3D P2,
    double local_x, double local_y,
    RGBIntensity I0_1_RGB, RGBIntensity I0_2_RGB,
    Vector3D PL1, Vector3D PL2,
    Vector3D O1_spot, Vector3D O2_spot,
    Vector3D V_camera,
    RGBIntensity K_RGB,
    double kd, double ks, double ke)
{
    Vector3D PT = local_to_global_coords(P0, P1, P2, local_x, local_y);
    Vector3D N = calculate_triangle_normal(P0, P1, P2);
    if (vector_length(N) < 1e-12) {
        return (RGBIntensity){0.0, 0.0, 0.0};
    }

    Vector3D V_cam_n = vector_normalize(V_camera);
    if (vector_dot(N, V_cam_n) <= 1e-12) {
        return (RGBIntensity){0.0, 0.0, 0.0};
    }

    RGBIntensity total = {0.0, 0.0, 0.0};
    if (I0_1_RGB.R != 0.0 || I0_1_RGB.G != 0.0 || I0_1_RGB.B != 0.0) {
        LightParameters p1 = calculate_light_params(PT, PL1, N, O1_spot);
        Vector3D S1_n = vector_normalize(p1.s);
        RGBIntensity I1_dir = calculate_directional_intensity(I0_1_RGB, p1.cos_theta);
        double f1 = fmax(0.0, p1.cos_alpha) / p1.R_squared;
        RGBIntensity E1 = rgb_scale(I1_dir, f1);
        Vector3D H1 = calculate_halfway_vector(V_cam_n, S1_n);
        RGBIntensity BRDF1 = calculate_brdf_blinn_phong(K_RGB, kd, ks, ke, H1, N);
        total = rgb_add(total, rgb_multiply(E1, BRDF1));
    }

    if (I0_2_RGB.R != 0.0 || I0_2_RGB.G != 0.0 || I0_2_RGB.B != 0.0) {
        LightParameters p2 = calculate_light_params(PT, PL2, N, O2_spot);
        Vector3D S2_n = vector_normalize(p2.s);
        RGBIntensity I2_dir = calculate_directional_intensity(I0_2_RGB, p2.cos_theta);
        double f2 = fmax(0.0, p2.cos_alpha) / p2.R_squared;
        RGBIntensity E2 = rgb_scale(I2_dir, f2);
        Vector3D H2 = calculate_halfway_vector(V_cam_n, S2_n);
        RGBIntensity BRDF2 = calculate_brdf_blinn_phong(K_RGB, kd, ks, ke, H2, N);
        total = rgb_add(total, rgb_multiply(E2, BRDF2));
    }

    return total;
}

int main() {
    Vector3D V_camera_static = {0.0, 0.0, 1.0};
    RGBIntensity K_RGB = {0.7, 0.7, 0.1};
    double kd = 0.8;
    double ks = 0.2;
    double ke = 10.0;

    Vector3D P0 = {0.0, 0.0, 0.0};
    Vector3D P1 = {1.0, 0.0, 0.0};
    Vector3D P2 = {0.0, 1.0, 0.0};
    Vector3D C = triangle_centroid(P0, P1, P2);

    RGBIntensity I0_1_RGB = {8, 6, 4};
    RGBIntensity I0_2_RGB = {3, 5, 7};

    Vector3D PL1 = {0.20, 0.85, 0.60};
    Vector3D PL2 = {0.85, 0.20, 0.70};
    Vector3D O1_spot = {0.166055, -0.643464, -0.747248};
    Vector3D O2_spot = {-0.586999, 0.151484, -0.795289};

    double x_vals[5] = {0.1, 0.5, 0.8, 0.2, 0.4};
    double y_vals[5] = {0.1, 0.2, 0.3, 0.7, 0.4};

    printf("------------------------------------------------------------\n");
    printf("1. E1(RGB, P_T):\n\n");

    printf("         ");
    for (int xi = 0; xi < 5; xi++) {
        printf("  x%d=%0.2f   ", xi + 1, x_vals[xi]);
    }
    printf("\n");

    for (int yi = 0; yi < 5; yi++) {
        printf("y%d=%0.2f  ", yi + 1, y_vals[yi]);
        for (int xi = 0; xi < 5; xi++) {
            double x = x_vals[xi];
            double y = y_vals[yi];
            RGBIntensity E = {0.0, 0.0, 0.0};
            // if (x >= 0 && y >= 0 && (x + y) <= 1.0) {
                E = calculate_E_RGB_PT(
                    P0, P1, P2,
                    x, y,
                    I0_1_RGB,
                    PL1, O1_spot
                );
            // }
            printf("(%0.3f,%0.3f,%0.3f) ", E.R, E.G, E.B);
        }
        printf("\n");
    }

    printf("------------------------------------------------------------\n");
    printf("2. E2(RGB, P_T):\n\n");

    printf("         ");
    for (int xi = 0; xi < 5; xi++) {
        printf("  x%d=%0.2f   ", xi + 1, x_vals[xi]);
    }
    printf("\n");

    for (int yi = 0; yi < 5; yi++) {
        printf("y%d=%0.2f  ", yi + 1, y_vals[yi]);
        for (int xi = 0; xi < 5; xi++) {
            double x = x_vals[xi];
            double y = y_vals[yi];
            RGBIntensity E = {0.0, 0.0, 0.0};
            // if (x >= 0 && y >= 0 && (x + y) <= 1.0) {
                E = calculate_E_RGB_PT(
                    P0, P1, P2,
                    x, y,
                    I0_2_RGB,
                    PL2, O2_spot
                );
            // }
            printf("(%0.3f,%0.3f,%0.3f) ", E.R, E.G, E.B);
        }
        printf("\n");
    }

    printf("------------------------------------------------------------\n");
    printf("3. L(RGB, P_T, v):\n\n");

    printf("         ");
    for (int xi = 0; xi < 5; xi++) {
        printf("  x%d=%0.2f   ", xi + 1, x_vals[xi]);
    }
    printf("\n");

    for (int yi = 0; yi < 5; yi++) {
        printf("y%d=%0.2f  ", yi + 1, y_vals[yi]);
        for (int xi = 0; xi < 5; xi++) {
            double x = x_vals[xi];
            double y = y_vals[yi];
            RGBIntensity L = {0.0, 0.0, 0.0};
            // if (x >= 0 && y >= 0 && (x + y) <= 1.0) {
                L = calculate_L_RGB_PT(
                    P0, P1, P2,
                    x, y,
                    I0_1_RGB, I0_2_RGB,
                    PL1, PL2,
                    O1_spot, O2_spot,
                    V_camera_static,
                    K_RGB,
                    kd, ks, ke
                );
            // }
            printf("(%0.3f,%0.3f,%0.3f) ", L.R, L.G, L.B);
        }
        printf("\n");
    }

    printf("------------------------------------------------------------\n");

    return 0;
}
