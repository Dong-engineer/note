"""
Service 的学习
"""
from selenium import webdriver

# 创建服务实例
service = webdriver.EdgeService(executable_path="D:/Development/web_Driver/msedgedriver.exe", port=8080, )

# path => 驱动文件路径
print(service.path)

# port => 服务运行所在端口
print(service.port)

# 日志输出模式
"""
-3  丢弃日志
-2  日志输出至浏览器控制台
-1  日志输出到标准错误
非负数 日志输出到指定路径
"""
print(service.log_output)

# env => 系统环境变量
print(service.env)

# env_path => driver_path_env_key 所指定的路径或从环境变量名为 SE_EDGEDRIVER 中取值
print(service.env_path())

# 创建驱动实例
# driver = webdriver.Edge(service=service)
