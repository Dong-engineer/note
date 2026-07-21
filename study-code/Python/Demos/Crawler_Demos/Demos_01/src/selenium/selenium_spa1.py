"""
通过 selenium 自动化测试获取 spa1 的电影数据信息
需求分析：
该网页限流，所以多线程同一 IP 进行效果很差
1.检测程序运行环境
2.无条件信任网站
3.设置浏览器全屏
4.设置浏览器加载页面策略
5.设置浏览器响应时间
6.隐藏自动化控制标签
7.爬取页面电影数据，需求信息：电影名称，网页地址，地区，时长，上映时间，评分
8.使用日志记录程序操作。日志要求：级别为：输出至 selenium/sqa1 文件中、级别 Debug（日志多的要命，慢慢看。。。）
"""
# 导入 selenium webdriver 驱动类
from selenium import webdriver
# 导入 Edge 浏览器选项配置信息
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
# 导入 python 内置系统信息模块
import platform
import os

# 获取操作系统名称
system_name = platform.system().lower()

# 创建浏览器选项配置
options = Options()

# 日志存储路径
log_path = "../selenium_log/spa1/spa.log"
# 检查日志路径是否存在，存在删除原有文件
if os.path.exists(log_path):
    os.remove(log_path)
    print("===== 旧日志已删除 =====")
# 日志级别
service_args = ["--log-level=DEBUG"]
# 创建服务实例
service = webdriver.EdgeService(log_output=log_path, service_args=service_args)

# 创建驱动变量，未初始化
driver = None

if system_name == "windows":
    # 信任网站证书
    options.accept_insecure_certs = True
    # 浏览器全屏
    options.add_argument("--start-maximized")
    # 禁用自动化检测，规避反爬
    options.add_argument("--disable-blink-features=AutomationControlled")
    # 隐藏自动化检测标记
    options.add_experimental_option("excludeSwitches", ["enable-automation"])
    # 浏览器加载策略
    options.page_load_strategy = "normal"
    # 浏览器响应时间
    options.timeouts = {"implicit": 2000}
    # 保持浏览器打开状态
    options.add_experimental_option("detach", True)

    # 创建驱动实例
    driver = webdriver.Edge(options=options, service=service)

try:
    # 请求
    url = "https://spa1.scrape.center/"
    driver.get(url)

    # 卡片元素列表
    card_elements = driver.find_elements(By.CLASS_NAME, "el-card__body")
    # 电影信息列表
    infos = []

    for card_element in card_elements:
        info = {}
        title = card_element.find_element(By.CLASS_NAME, "m-b-sm").text
        info.setdefault("电影名称", title)
        url = card_element.find_element(By.CSS_SELECTOR, "a[href*='/detail/']").get_attribute("href")
        info.setdefault("网址", f"{url}")
        # 原页面中有在卡片元素中有两个 class="m-v-sm" 的元素
        area_time = card_element.find_elements(By.CLASS_NAME, "m-v-sm")[0].text.split("/")
        info.setdefault("地区", area_time[0])
        info.setdefault("时长", area_time[1])
        publish = card_element.find_elements(By.CLASS_NAME, "m-v-sm")[1].text
        info.setdefault("上映时间", publish)
        score = card_element.find_element(By.CLASS_NAME, "score").text
        info.setdefault("评分", score)

        infos.append(info)

    for info in infos:
        print(info)

except Exception as e:
    print(f"程序异常：{e}")
finally:
    driver.quit()
