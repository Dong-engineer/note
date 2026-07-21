__Author__ = "Dong"
__Date__ = "2026/3/20"
__Version__ = "1.0.0"
__Description__ = "Series 的学习 03"
__email__ = "jqd19838302431@163.com"

import pandas as pd
import numpy as np

demo_list = [1, 2, 3]
demo_array = np.array([4, 5, 6])

series_list = pd.Series(demo_list)
series_array = pd.Series(demo_array)

series_list.iloc[0] = 10
series_array.iloc[0] = 10

print(demo_list)  # [1, 2, 3]
print(demo_array)  # [10  5  6]

print("=== Series ===")
print(series_list)
"""
0    10
1     2
2     3
dtype: int64
"""
print(series_array)
"""
0    10
1     5
2     6
dtype: int64
"""

"""
经过上面的测验，总结如下：
1.首先我们并没有使用 copy 参数
2.对于 list 和 np 的 array 两种数据结构出现了两种不同的情况
原因：
在 Pandas 中其内部有封装 numpy ，其 data 参数接收的任何类型的参数都会优先被转换成 numpy 的 array 
所以对于 list 或其他非 numpy 数据结构类型的数据结构来说，其转化的过程就会创建一个副本，
所以通过 python 原生集合结构创建的 Series 对象和原数据的内存是不共享的
而 numpy 的 array 则没有转化这一过程，所以 numpy 的 array 的数据结构和 Series 的内存是共享的

在实际开发过程中，如果是哪种类型，开发者如果明确需要将 Series 的内存和原数据分割开，则一定指明参数 copy=True
"""
