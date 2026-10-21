import os
import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("results/tables/results.csv")
os.makedirs("results/plots", exist_ok=True)

plots = [
    ("vertices", "time_vs_vertices.png"),
    ("edges", "time_vs_edges.png"),
]

for column, file_name in plots:
    plt.figure()
    for name, group in data.groupby("algorithm"):
        plt.plot(group[column], group["avgTimeMs"], marker="o", label=name)
    plt.xlabel(column)
    plt.ylabel("Average time (ms)")
    plt.yscale("log")
    plt.legend()
    plt.savefig("results/plots/" + file_name)
    plt.close()