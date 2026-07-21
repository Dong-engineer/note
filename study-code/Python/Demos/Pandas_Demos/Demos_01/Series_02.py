__Author__ = "Dong"
__Date__ = "2026/3/20"
__Version__ = "1.0.0"
__Description__ = "Series 的学习 02"
__email__ = "jqd19838302431@163.com"

import pandas as pd


demo_list = [1, 2, 3]
demo_tuple = (4, 5, 6)
demo_dict = {"a": 7, "b": 8, "c": 9}

# 创建 Series 对象
# 这里传输 index 参数后，开发者会发现，Pandas 自动填充的序号会以开发者定义的 index 为准
# 而对于 dict 结构的数据，Pandas 则是会以 index 为键，在 demo_dict 中寻找其对应的值，找不到则赋值为 NaN
series_list = pd.Series(demo_list, index=[1, 2, 3])
series_tuple = pd.Series(demo_tuple, index=["I", "II", "III"])
series_dict = pd.Series(demo_dict, index=["x", "y", "z"])

print(series_list)
print(series_tuple)
print(series_dict)
