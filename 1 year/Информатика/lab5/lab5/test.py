


import matplotlib.pyplot as plt
import numpy as np

# задаем функцию f(x)
def f(x):
    return x**2

# задаем функцию w(x)
def w(x):
    return np.pi/2  + np.sin(x)

# создаем массив значений x на отрезке [-π,π]
x = np.linspace(-np.pi, np.pi, 100)

# создаем график
plt.plot(x, f(x), 'b', label='f(x)')
plt.plot(x, w(x), 'r', label='w(x)')
plt.legend(loc='best')
plt.show()