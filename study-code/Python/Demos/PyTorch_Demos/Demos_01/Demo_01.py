import torch

i = torch.rand(5, 3)
print(i)
b = torch.cuda.is_available()
print(b)
