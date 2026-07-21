from selenium import webdriver
from selenium.webdriver import ActionChains
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By


option = Options()
option.add_experimental_option("detach", True)
driver = webdriver.Edge(options=option)

# 访问百度
driver.get("https://www.baidu.com")

# 获取输入框
search_input = driver.find_element(By.ID, "kw")
# 获取搜索按钮
search_but = driver.find_element(By.ID, "su")

# 元素交互
search_input.send_keys("水果")
ActionChains(driver).send_keys_to_element()
search_but.click()
