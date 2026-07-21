import requests
from Project_One.src.parsers.parser_ssr3 import parsers_html


# python 对字符进行 base64 有点麻烦，需要用到 base64 库，而且 base64 只能对字节进行编码，所以还需要将字符串进行解码
# 这里直接使用 requests 库自带参数认证即可
def spider_URL(target_url, header):
    response = requests.request("GET", target_url, headers=header, auth=("admin", "admin"))
    code = response.status_code

    if code == 200:
        print("请求成功")
        try:
            infos = parsers_html(response.text)
            for info in infos:
                print(info)
                dict
        except Exception as e:
            print(f"错误信息：{e}")
    else:
        print("请求失败")
        print(f"错误代码：{code}")  # 预测401，前往目标网站观察网站结构
        print(f"响应标头：{response.headers}")
