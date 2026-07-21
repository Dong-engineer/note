"""
张量
"""
import numpy as np
import torch

# 通过数组创建张量 => Tensor
data = [[1, 2], [4, 5]]
# x_data = torch.tensor(data)

# 通过 Numpy 数组创建张量
np_array = np.array(data)
x_data = torch.tensor(np_array)
print(x_data)

# 从另一张量创建新的张量，新的张量会保留原有张量的数据信息
'''
如下，tensor() 的接口源码，开发者可以通过以下参数访问张量信息
def tensor(
    data: Any,
    dtype: _dtype | None = None,
    device: DeviceLikeType | None = None,
    requires_grad: _bool = False,
    pin_memory: _bool = False,
) -> Tensor:
'''
y_data = torch.tensor(x_data)

print("===== 以 y_data 为例 =====")

# data: 张量里面的数据
print(f"y_data 的数据：\n{y_data.data}")

# dtype 张量中的数据类型
print(f"y_data 的数据类型：{y_data.dtype}")

'''
torch.device("cpu")：默认 CPU 设备
torch.device("cpu", 0)：指定 CPU 第 0 个设备（多 CPU 场景极少用）
GPU 设备（需有 NVIDIA 显卡且安装 CUDA）：
torch.device("cuda")：默认 GPU 设备（通常是 cuda:0）
torch.device("cuda", 0) / torch.device("cuda:0")：第 0 块 GPU
torch.device("cuda", 1) / torch.device("cuda:1")：第 1 块 GPU（多卡场景）
其他特殊设备（较少用）：
torch.device("mps")：苹果 Silicon 芯片的 GPU（M1/M2/M3 系列）
'''
print(f"y_data 的处理平台（计算机的哪一硬件）：{y_data.device}")

'''
True：该张量需要计算梯度（常用于模型参数、训练数据中的自变量）
False：该张量不需要计算梯度（默认值，常用于测试数据、标签数据）
'''
print(f"y_data 是否需要计算梯度：{y_data.requires_grad}")

# pin_memory 只读属性
print(f"y_data 的锁页内存状态：{y_data.pin_memory}")
