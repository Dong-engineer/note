"""
检测 CUDA 是否可用
"""
import torch

data = [1, 2, 3, 4, 5, 6, 7]

# 创建张量
x_data = torch.Tensor(data)

print(f"CUDA 是否可用：{torch.cuda.is_available()}")
print(f"x_data 计算位置：{x_data.device}")
