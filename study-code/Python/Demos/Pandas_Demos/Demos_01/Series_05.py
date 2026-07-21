__Author__ = "Dong"
__Date__ = "2026/3/20"
__Version__ = "1.0.0"
__Description__ = "Series 的属性"
__email__ = "jqd19838302431@163.com"

import pandas as pd

demo_list = [1, 2, 3, 4]

series_list = pd.Series(demo_list)

print(series_list.index)
print(series_list.array)
print(series_list.dtype)
print(series_list.shape)
print(series_list.nbytes)
print(series_list.ndim)
print(series_list.size)
print(series_list.T)
print(series_list.memory_usage())

