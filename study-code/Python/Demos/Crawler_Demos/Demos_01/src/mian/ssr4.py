import time
import concurrent.futures
from Project_One.src.spider import spider_ssr4

"""
爬取 ssr4 网页信息
ssr4 网页与之前 ssr1 网页结构并没有什么区别，但是 ssr4 添加了 5 秒的响应时间，
这也就导致了在爬取网页时特别慢，在实际开发时为了更高的效率，这就需要使用到线程的知识，
开发者可以对不同的 URL 创建一个不同的线程，然后进行爬取，但是这很有可能会发生线程冲突的
问题。
在 python 内部中含有线程池对象，开发者直接使用线程池即可
"""

"""
# 目标网站
target_url = "https://ssr4.scrape.center/"
# 请求头
header = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/"
                  "537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0"
}

# 测试程序，单个网站请求时间
start_time = time.perf_counter()
response = requests.request("GET", url=target_url, headers=header)
code = response.status_code

if code == 200:
    print(response.text)
else:
    print(f"请求失败，错误代码：{code}")
end_time = time.perf_counter()
print(f"耗时：{end_time - start_time}s")
"""

"""
这里开发者可多创建几个线程，然后同时执行线程即可，但是这样做并不合适，如果在实际开发中，
开发者需要将爬取的数据存储到本地文件中或数据库中，这很容易发生线程冲突，需要开发者自行维护线程
# 目标网站参数列表
target_urls = ["https://ssr4.scrape.center/page/1", "https://ssr4.scrape.center/page/2",
               "https://ssr4.scrape.center/page/3", "https://ssr4.scrape.center/page/4"]

header = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/"
                  "537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0"
}

page_one = threading.Thread(target=spider_ssr4.spider_URL(target_urls[0], header),
                            name="Thread-1")
page_one.start()
page_one.join()
"""

start_time = time.perf_counter()
# 使用线程池
target_urls = ["https://ssr2.scrape.center/page/1", "https://ssr2.scrape.center/page/2",
               "https://ssr2.scrape.center/page/3", "https://ssr2.scrape.center/page/4"]

header = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/"
                  "537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0"
}
MAX_WORKERS = 4

thread_group_one = concurrent.futures.ThreadPoolExecutor(MAX_WORKERS, "Thread_Group-1")


with thread_group_one as executor:
    results = executor.map(spider_ssr4.spider_URL, target_urls, [header]*len(target_urls))


end_time = time.perf_counter()
print(f"耗时：{end_time - start_time}")
