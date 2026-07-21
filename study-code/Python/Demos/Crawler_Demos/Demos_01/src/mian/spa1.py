"""
spa1 的运行程序
在此次爬虫程序中，开发者可以通过调整请求参数：limit=10&offset=0 来获取更多的数据
"""
from Demos_01.src.spider import spider_spa1


if __name__ == "__main__":
    """
    Args:
        limit: 数据条数
        offset: 数据偏移量，即从哪里开始
    """
    url = "https://spa1.scrape.center/api/movie/?limit=20&offset=0"

    head = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                      "(KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0"
    }

    spider_spa1.spider_URL(url, head=head)
