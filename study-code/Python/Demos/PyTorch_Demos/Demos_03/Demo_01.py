"""
torch.autograd
是 PyTorch 的自动微分引擎，它为神经网络的训练提供了动力。
在官方文档中，开发者从这里开始就要进入实操了，有很多函数也许开发者并没有见过，
作者会添加注释的，如果说注释开发者也看不懂，那么请开发者明白本节你的学习目的是什么——了解 torch.autograd
并非掌握，后续会深度学习的。
检验理论的最好方式就是实践！
"""
import torch
# 这里要导入一个 Pytorch 所提供一个视觉模型——ResNet，后续开发者会在论文、深度学习中常常见到他
from torchvision.models import resnet18, ResNet18_Weights
import os

# 自定义指定路径（模型的加载路径默认是存储到 C 盘下的，建议更改）
model_path = "D:/Development/Models/ResNet18"
os.environ["TORCH_HOME"] = model_path

# 这段代码首次执行会去 Web 上下载一个模型
# 如果本地存有该模型权重文件，后面会直接使用
model = resnet18(weights=ResNet18_Weights.DEFAULT)

'''
创建一个随机数据张量来表示单个图像，它具有 3 个通道，高度和宽度均为 64，
以及相应的 label，初始化为一些随机值。预训练模型中的标签形状为 (1,1000)。
'''
# 随机生成张量，用于模型训练
data = torch.rand(1, 3, 64, 64)
# 标签值
labels = torch.rand(1, 1000)

computing_platform = data.device

# 检测计算平台
if not torch.cuda.is_available() and computing_platform == torch.device("cpu"):
    print(f"计算平台为{computing_platform}")
    print("===== 开始计算 =====")
    prediction = model(data)

    loss = (prediction - labels).sum()
    loss.backward()

    print(prediction)
else:
    print("此教程仅在 CPU 上运行，在 GPU 设备上（即使张量已移至 CUDA）也不起作用")
