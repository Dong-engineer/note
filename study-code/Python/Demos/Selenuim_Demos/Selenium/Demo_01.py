"""
第一个 selenium 脚本
"""
from selenium import webdriver
from selenium.webdriver.edge.options import Options

# 驱动路径
# driver_url = "D:/Development/web_Driver/msedgedriver.exe"
# service = webdriver.EdgeService(executable_path=driver_url)
# 驱动设置
option = Options()
option.add_experimental_option("detach", True)
driver = webdriver.Edge(options=option)

driver.get("https://www.baidu.com")
