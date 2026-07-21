import requests
import time
import threading
from Project_One.src.parsers.parser_ssr4 import parsers_html


# 爬虫函数
def spider_URL(target_url, header):

    # 线程日志
    thread_name = threading.current_thread().name
    print(f"[{thread_name}] 开始爬取：{target_url}")

    start = time.time()
    response = requests.request("GET", target_url, headers=header)
    end = time.time()
    code = response.status_code
    print(f"[{thread_name}] 完成爬取：{target_url}，耗时：{end - start:.2f} 秒")

    if code == 200:
        print("请求成功")
        # 解析网页
        print("正在解析网页,请稍等...")
        infos = parsers_html(response.text)
        for info in infos:
            print(info)
        print("解析完成")
    else:
        print(f"请求失败，错误代码：{code}")

