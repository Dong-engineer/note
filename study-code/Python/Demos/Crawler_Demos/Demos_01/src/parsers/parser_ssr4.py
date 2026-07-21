from bs4 import BeautifulSoup
"""
解析 srr4 网页
"""


# 解析方法
def parsers_html(html):
    soup = BeautifulSoup(html, "lxml")
    items = soup.find_all("div", class_="el-card__body")
    infos = []

    for item in items:

        info = {}
        info.setdefault("电影名称", item.select_one("a.name h2").get_text(strip=True))
        info.setdefault("网页地址", f"https://ssr4.scrape.center{item.select_one('a.name')['href']}")
        info.setdefault("地区", item.select_one("div.info span:nth-child(1)").get_text(strip=True))
        info.setdefault("时长", item.select_one("div.info span:nth-child(3)").get_text(strip=True))

        # 有的电影并没有上映时间，这里三目运算判断一下
        release_time = item.select("div.info span:nth-child(1)")[1].get_text(strip=True) \
            if len(item.select("div.info span:nth-child(1)")) > 1 \
            else "未知"
        info.setdefault("上映时间", release_time)
        info.setdefault("评分", item.select_one("p.score").get_text(strip=True))
        infos.append(info)

    return infos
