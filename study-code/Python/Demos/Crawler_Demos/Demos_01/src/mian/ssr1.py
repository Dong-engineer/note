from Project_One.src.spider import spider_ssr1

"""
本文件算第一次爬虫实战，很简单，并没有使用面向对象编程范式，
后面遇见负责且规模庞大的工程再使用面向对象，对于这种简单的
爬虫程序来讲，函数式编程更简单一些

文件名为 ssr1，实际爬取 ssr2，二者网站并没有很大区别，仅有 Http Https 的区别，
故写在一个文件中。
"""

target_url = "https://ssr2.scrape.center/"

# 请求表头
header = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0"
}

spider_ssr1.spider_URL(target_url, header)
