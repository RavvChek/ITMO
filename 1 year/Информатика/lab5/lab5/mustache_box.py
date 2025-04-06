import pandas as pd
import matplotlib.pyplot as plt
data = pd.read_csv('mustache_box.csv', delimiter=";", encoding='utf-8')
data.boxplot()
plt.show()


