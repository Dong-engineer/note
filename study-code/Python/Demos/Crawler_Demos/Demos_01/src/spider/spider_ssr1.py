import requests
from Project_One.src.parsers import parser_ssr1


def spider_URL(target_url, header):
    response = requests.get(target_url, header)
    code = response.status_code

    if code == 200:
        print("<<<======网页请求成功!======>>>")
        print("<<<======解析网页中======>>>")

        try:
            infos = parser_ssr1.parsers_html(response.text)
            for info in infos:
                print(info)
        except Exception as e:
            print(f"错误信息：{e}")

        print("<<<======解析完成======>>>")
    else:
        print(f"<<<======网页请求失败，响应码为{code}======>>>")


