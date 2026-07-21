"""
创建张量
创建张量，在 Demo_01 中已经学习过一小部分
在这里，开发者将会学习从其他类型来创建张量
"""
import numpy as np
import torch

# 从 numpy 的数组创建
'''
核心区别：torch.from_numpy 与原数组共享内存，torch.tensor 不共享
也就是说二者底层数据部分的指针指向同一地址，二者数据部分一旦有一个发生变化另一个也随之变化
'''
data = [1, 2, 34, 45, 5, 43, 6, 7]
np_arr = np.array(data)
x_data = torch.from_numpy(np_arr)
print(x_data)
np_arr[2] = 35
print(x_data)

# 从一个张量创建另一个张量
'''
ones_like() 生成和输入张量同形状的全 1 张量，继承输入张量的 dtype（输入整型则生成 int，输入浮点则生成 float）
rand_like() 生成和输入张量同形状的随机数张量，浮点型（float32），若输入是整型会隐式转换
'''
y_ones = torch.ones_like(x_data)
print(y_ones)

# 规范使用rand_like：先确保输入是浮点型，或显式指定dtype（避免隐式转换警告）
y_rand = torch.rand_like(x_data.to(torch.float))  # 先转浮点型，再生成随机数
print(y_rand)

# 根据形状创建张量
shape = (2, 3)
'''
创建随机张量
param:
    generator（:class:torch.Generator，可选）：用于采样的伪随机数生成器
    out（Tensor，可选）：输出张量
    dtype（:class:torch.dtype，可选）：返回张量的期望数据类型 | 默认值：如果为 None，则使用全局默认值（参见 :func:torch.set_default_dtype）
    layout（:class:torch.layout，可选）：返回张量的期望布局 | 默认值：torch.strided
    device（:class:torch.device，可选）：返回张量的期望设备 | 默认值：如果为 None，则使用默认张量类型的当前设备
    requires_grad（bool，可选）：是否自动求导应记录返回张量上的操作 | 默认值：False。
    pin_memory（bool，可选）：如果设置，返回的张量将在锁定内存中分配。仅适用于 CPU 张量。默认值：False。
'''
rand_tensor = torch.rand(shape)
'''
创建全 1 张量
param:
    out（Tensor，可选）：输出张量
    dtype（:class:torch.dtype，可选）：返回张量的期望数据类型 | 默认值：如果为 None，则使用全局默认值（参见 :func:torch.set_default_dtype）
    layout（:class:torch.layout，可选）：返回张量的期望布局 | 默认值：torch.strided
    device（:class:torch.device，可选）：返回张量的期望设备 | 默认值：如果为 None，则使用默认张量类型的当前设备
    requires_grad（bool，可选）：是否自动求导应记录返回张量上的操作 | 默认值：False。
'''
one_tensor = torch.ones(shape)
'''
创建全 0 张量
param:
    out（Tensor，可选）：输出张量
    dtype（:class:torch.dtype，可选）：返回张量的期望数据类型 | 默认值：如果为 None，则使用全局默认值（参见 :func:torch.set_default_dtype）
    layout（:class:torch.layout，可选）：返回张量的期望布局 | 默认值：torch.strided
    device（:class:torch.device，可选）：返回张量的期望设备 | 默认值：如果为 None，则使用默认张量类型的当前设备
    requires_grad（bool，可选）：是否自动求导应记录返回张量上的操作 | 默认值：False。
'''
zero_tensor = torch.zeros(shape)

print(rand_tensor)  # 2行3列
print(one_tensor)  # 2行3列
print(zero_tensor)  # 2行3列
