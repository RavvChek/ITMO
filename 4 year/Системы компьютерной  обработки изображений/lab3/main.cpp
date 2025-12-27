#include <iostream>
#include <random>
#include <iomanip>
#include <cmath>

const std::string EXACT_VALUE_INTEGRAL = "Точное значение интеграла: ";
const std::string SIMPLE_MONTE_CARLO = "Простой метод Монте-Карло";
const std::string STRATIFIED_MONTE_CARLO = "Метод Монте-Карло со стратификацией";
const std::string IMPORTANCE_SAMPLING = "\n=== Метод Монте-Карло с выборкой по значимости ===\n";
const std::string MIS = "\n=== Метод MIS ===\n";
const std::string METHOD_SAMPLING_X = "Выборка по p(x) ∝ x";
const std::string METHOD_SAMPLING_X2 = "Выборка по p(x) ∝ x^2";
const std::string METHOD_SAMPLING_X3 = "Выборка по p(x) ∝ x^3";
const std::string METHOD_MIS_BALANCE = "MIS: средняя плотность (w = p/(p+q))";
const std::string METHOD_MIS_SQUARE = "MIS: средний квадрат (w = p²/(p²+q²))";
const std::string RUSSIAN_ROULETTE_05 = "Русская рулетка (R=0.5)";
const std::string RUSSIAN_ROULETTE_075 = "Русская рулетка (R=0.75)";
const std::string RUSSIAN_ROULETTE_095 = "Русская рулетка (R=0.95)";

// Функция, которую интегрируем
double f(double x) {
    return x * x;
}

// Метод Монте-Карло для интегрирования
double monteCarloIntegral(double a, double b, int N) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_real_distribution<double> dis(a, b);

    double sum = 0.0;
    for (int i = 0; i < N; ++i) {
        double x = dis(gen);
        sum += f(x);
    }

    return (b - a) * (sum / N);
}

// Метод Монте-Карло со стратификацией
double stratifiedMonteCarloIntegral(double a, double b, int N, double stride) {
    std::random_device rd;
    std::mt19937 gen(rd());

    double total_integral = 0.0;
    std::vector<double> strata;

    for (double x = a; x < b; x += stride) {
        strata.push_back(x);
    }
    strata.push_back(b);

    int num_strata = strata.size() - 1;
    int points_per_stratum = N / num_strata;
    int total_used = points_per_stratum * num_strata;

    if (total_used < N) {
        points_per_stratum = (N + num_strata - 1) / num_strata;
    }

    for (int k = 0; k < num_strata; ++k) {
        double x_low = strata[k];
        double x_high = strata[k + 1];
        double width = x_high - x_low;

        std::uniform_real_distribution<double> dis(x_low, x_high);
        double sum = 0.0;
        int actual_points = (k == num_strata - 1) ?
                            (N - (num_strata - 1) * points_per_stratum) : points_per_stratum;

        for (int i = 0; i < actual_points; ++i) {
            double x = dis(gen);
            sum += f(x);
        }

        double avg = sum / actual_points;
        total_integral += width * avg;
    }

    return total_integral;
}


// Универсальная функция для тестирования метода интегрирования
void runExperiment(
        const std::vector<int> &N_values,
        const std::function<double(int)> &integrationMethod,
        double exactValue,
        const std::string &methodName) {
    std::cout << "\n=== Метод: " << methodName << " ===\n";
    std::cout << std::setw(8) << "N "
              << std::setw(14) << std::fixed << std::setprecision(6) << "Интеграл"
              << std::setw(14) << std::setprecision(6) << " Ошибка" << "\n";
    std::cout << std::string(36, '-') << "\n";

    for (int N: N_values) {
        double result = integrationMethod(N);
        double error = std::abs(result - exactValue);

        std::cout << std::setw(8) << N
                  << std::setw(14) << std::fixed << std::setprecision(6) << result
                  << std::setw(14) << std::setprecision(6) << error << "\n";
    }
}

// Генерация случайной величины по плотности p(x) ∝ x^k на [2, 5]
double sampleFromPk(double a, double b, int k, std::mt19937 &gen) {
    std::uniform_real_distribution<> dis(0.0, 1.0);
    double u = dis(gen);

    double lower_power = std::pow(a, k + 1);
    double upper_power = std::pow(b, k + 1);
    double value = u * (upper_power - lower_power) + lower_power;
    return std::pow(value, 1.0 / (k + 1));
}

// Оценка нормировочной константы Z_k = ∫ₐᵇ x^k dx
double computeZk(double a, double b, int k) {
    return (std::pow(b, k + 1) - std::pow(a, k + 1)) / (k + 1);
}

// Метод Монте-Карло с выборкой по значимости для p(x) ∝ x^k
double importanceSamplingIntegral(double a, double b, int N, int k) {
    std::random_device rd;
    std::mt19937 gen(rd());

    double Zk = computeZk(a, b, k);  // нормировочная константа
    double sum = 0.0;

    for (int i = 0; i < N; ++i) {
        double x = sampleFromPk(a, b, k, gen);
        double px = std::pow(x, k) / Zk;  // плотность p(x)
        sum += f(x) / px;
    }

    return sum / N;  // ≈ ∫ f(x) dx
}

const double Z1 = computeZk(2.0, 5.0, 1);
const double Z3 = computeZk(2.0, 5.0, 3);

// Плотности вероятности (нормированные)
double p1(double x) {
    return x / Z1;
}

double p2(double x) {
    return std::pow(x, 3) / Z3;
}

// MIS с двумя плотностями и двумя типами весов
enum WeightType {
    BALANCE, SQUARE
};

double misIntegral(int N, WeightType weightType) {
    std::random_device rd;
    std::mt19937 gen(rd());

    int N1 = N / 2;
    int N2 = N - N1;
    double sum1 = 0.0;
    double sum2 = 0.0;

    // Выборка из p1(x) ∝ x (k=1)
    for (int i = 0; i < N1; ++i) {
        double x = sampleFromPk(2.0, 5.0, 1, gen);
        double p1_val = p1(x);
        double p2_val = p2(x);

        double w1;
        if (weightType == BALANCE) {
            w1 = p1_val / (p1_val + p2_val);
        } else {
            double p1_sq = p1_val * p1_val;
            double p2_sq = p2_val * p2_val;
            w1 = p1_sq / (p1_sq + p2_sq);
        }

        sum1 += (f(x) / p1_val) * w1;
    }

    // Выборка из p2(x) ∝ x^3 (k=3)
    for (int i = 0; i < N2; ++i) {
        double x = sampleFromPk(2.0, 5.0, 3, gen);
        double p1_val = p1(x);
        double p2_val = p2(x);

        double w2;
        if (weightType == BALANCE) {
            w2 = p2_val / (p1_val + p2_val);
        } else {
            double p1_sq = p1_val * p1_val;
            double p2_sq = p2_val * p2_val;
            w2 = p2_sq / (p1_sq + p2_sq);
        }

        sum2 += (f(x) / p2_val) * w2;
    }
    return sum1 / N1 + sum2 / N2;
}

// Генерация равномерной точки на [a, b]
double sampleUniform(double a, double b, std::mt19937 &gen) {
    std::uniform_real_distribution<> dis(0.0, 1.0);
    return a + (b - a) * dis(gen);
}

// Оценка интеграла методом Монте-Карло с русской рулеткой
double russianRouletteIntegral(int N, double continueProb) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_real_distribution<> dis(0.0, 1.0);

    double sum = 0.0;
    double a = 2.0, b = 5.0;
    double p_uniform = 1.0 / (b - a);

    for (int i = 0; i < N; ++i) {
        double x = sampleUniform(a, b, gen);
        double unweighted_contribution = f(x) / p_uniform;

        // Русская рулетка
        double xi = dis(gen);
        if (xi < continueProb) {
            double weight = 1.0 / continueProb;
            sum += unweighted_contribution * weight;
        }
    }

    return sum / N;
}


int main() {

    const double a = 2.0;
    const double b = 5.0;
    const double exact = 39.0; // Точное значение интеграла

    std::vector<int> N_values = {100, 1000, 10000, 100000};

    // Аналитическое решение
    std::cout << EXACT_VALUE_INTEGRAL << exact << "\n";

    // Простой метод Монте-Карло
    auto simpleMonteCarlo = [a, b](int N) {
        return monteCarloIntegral(a, b, N);
    };
    runExperiment(N_values, simpleMonteCarlo, exact, SIMPLE_MONTE_CARLO);


    // Метод Монте-Карло со стратификацией
    auto stratifiedMonteCarlo = [a, b, exact](int N) {
        double stride = (b - a) / 10.0;
        return stratifiedMonteCarloIntegral(a, b, N, stride);
    };
    runExperiment(N_values, stratifiedMonteCarlo, exact, STRATIFIED_MONTE_CARLO);

    // Метод Монте-Карло с выборкой по значимости
    std::cout << IMPORTANCE_SAMPLING;
    // Выборка по значимости: p(x) ∝ x
    auto impMethod1 = [a, b](int N) {
        return importanceSamplingIntegral(a, b, N, 1);
    };
    runExperiment(N_values, impMethod1, exact, METHOD_SAMPLING_X);

    // Выборка по значимости: p(x) ∝ x^2
    auto impMethod2 = [a, b](int N) {
        return importanceSamplingIntegral(a, b, N, 2);
    };
    runExperiment(N_values, impMethod2, exact, METHOD_SAMPLING_X2);

    // Выборка по значимости: p(x) ∝ x^3
    auto impMethod3 = [a, b](int N) {
        return importanceSamplingIntegral(a, b, N, 3);
    };

    runExperiment(N_values, impMethod3, exact, METHOD_SAMPLING_X3);

    // MIS
    std::cout << MIS;
    // MIS: средняя плотность (balance heuristic)
    auto misBalance = [](int N) {
        return misIntegral(N, BALANCE);
    };
    runExperiment(N_values, misBalance, exact, METHOD_MIS_BALANCE);

    // MIS: средний квадрат (square heuristic)
    auto misSquare = [](int N) {
        return misIntegral(N, SQUARE);
    };
    runExperiment(N_values, misSquare, exact, METHOD_MIS_SQUARE);

    // Русская рулетка с тремя порогами
    auto rr_05 = [](int N) { return russianRouletteIntegral(N, 0.5); };
    auto rr_075 = [](int N) { return russianRouletteIntegral(N, 0.75); };
    auto rr_095 = [](int N) { return russianRouletteIntegral(N, 0.95); };

    runExperiment(N_values, rr_05, exact, RUSSIAN_ROULETTE_05);
    runExperiment(N_values, rr_075, exact, RUSSIAN_ROULETTE_075);
    runExperiment(N_values, rr_095, exact, RUSSIAN_ROULETTE_095);

    return 0;
}