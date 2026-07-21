from selenium import webdriver
from selenium.webdriver.common.by import By  # 元素定位方式
from selenium.webdriver.edge.options import Options

options = Options()
# 保持浏览器打开
options.add_experimental_option("detach", True)
# 禁用自动检测提示条
options.add_experimental_option("excludeSwitches", ["enable-automation"])
options.add_experimental_option('useAutomationExtension', False)

driver = webdriver.Edge(options=options)

driver.get("https://www.baidu.com")

element = driver.find_element(By.ID, "kw")

print(element)
