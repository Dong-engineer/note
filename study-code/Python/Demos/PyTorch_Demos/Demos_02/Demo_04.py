"""
张量的计算
上一节学习了 CUDA 平台是否可用，
其目的就是为了张量的计算，通过判断 CUDA 是否可用，
来调用 CUDA 完成张量的计算，但是俺没有 NVIDIA
这里直接用 cpu 计算
"""
import torch

# 生成 4 * 4 的全 1 张量
x_data = torch.ones(4, 4)
print(x_data)

# 切片（这和 Numpy 的切片一模一样）
x_data[:, 1] = 0  # 将索引为 1 的列全部置为 0
print(x_data)

# 张量的连接
'''
cat() 按照指定维度对张量进行拼接
param:
    Tensor[]：指定需要拼接的张量参数列表
    dim：拼接维度 => 两个值 0/1 ，0 是 y 轴方向，1 是 x 轴方向（可选）
'''
y_data = torch.cat([x_data, x_data, x_data], 1)
print(y_data)

# 张量分割
'''
split() 按照指定参数对张量进行分割
param:
    split_size：分割后的张量长度
    dim：分割维度
return:
    返回一个张量列表
'''
z_data = y_data.split(4, 1)
for data in z_data:
    print(data)
'''
chunk() 按照指定参数对张量进行分割
param:
    chunks：分割后的列表的长度
    dim：分割维度
return:
    返回一个张量列表
'''
w_data = y_data.chunk(3, 1)
for data in w_data:
    print(data)

# ============= 下面就是 +-*/ ================

shape = (3, 3)
# 全 1 张量
a = torch.ones(shape)
# 随机数张量
b = torch.randint(1, 5, shape)

print(f"a| {a}")
print(f"b| {b}")
print(f"张量相加| {a + b}")
print(f"张量相减| {a - b}")
print(f"张量相乘| {a * b}")
print(f"张量相除| {a / b}")
'''
函数名后带有_ ，都是原地操作
原地操作可以省一些变量空间，但相对应的，开发者会丢失原有的数据记录，如下。
所以官方并不建议开发者使用原地操作，没有必要为了一点内存空间，导致数据的错误
'''
print(f"张量自加| {a.add_(3)}")
print(f"a| {a}")
