__Author__ = "Dong"
__Date__ = "2026/3/20"
__Version__ = "1.0.0"
__Description__ = ""
__email__ = "jqd19838302431@163.com"

import pandas as pd

demo_list = [1, 2, 3]

series_list = pd.Series(demo_list, dtype=str)

print(series_list.values)  # ['1' '2' '3']
