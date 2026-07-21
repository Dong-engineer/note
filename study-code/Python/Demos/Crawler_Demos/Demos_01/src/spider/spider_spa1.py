"""
目标网站：https://spa1.scrape.center/
网站分析：数据通过 AJAX 动态渲染，需要使用 selenium 或者爬取其 json 数据
"""
import requests
from Demos_01.src.parsers import parser_spa1


def spider_URL(target_url, head):

    response = requests.get(target_url, headers=head)
    code = response.status_code
    if code == 200:
        print("===== 请求成功 =====")
        json_data = response.json()  # 这里会返回一个字典，开发者根据需求获取相对应的键值对即可

        # 解析 json 数据
        infos = parser_spa1.parser_spa1_json(json_data)

        # 打印电影信息
        for info in infos:
            print(info)
    else:
        print(f"===== 错误代码：{code} =====")
