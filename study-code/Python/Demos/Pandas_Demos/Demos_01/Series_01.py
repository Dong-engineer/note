__Author__ = "Dong"
__Date__ = "2026/3/20"
__Version__ = "1.0.0"
__Description__ = "Series 的学习 01"
__email__ = "jqd19838302431@163.com"

import pandas as pd


demo_list = [1, 2, 3]
demo_tuple = (4, 5, 6)
demo_dict = {"a": 7, "b": 8, "c": 9}

# 创建 Series 对象
# 这里不对 index、copy、name 等参数进行传参看效果
series_list = pd.Series(demo_list)
series_tuple = pd.Series(demo_tuple)
series_dict = pd.Series(demo_dict)

print(series_list)
print(series_tuple)
print(series_dict)
