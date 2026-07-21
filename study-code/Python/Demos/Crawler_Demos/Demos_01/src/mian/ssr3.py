from Project_One.src.spider import spider_ssr3

"""
srr3 案例是爬虫时需要通过 Http Basic Authentication 验证,这里简称 HBA

什么是 HBA?
简单来说就是需要用户认证。
专业来讲——即 Http 认证，当客户端访问服务器时，服务其会返回一个 401 ，
当客户端完成认证后即可正常访问页面（和用户登录类似），但 HBA 会对客户端
传输的认证信息进行 Base64 编码，Base64 只是一种编码方式，并非加密。
除此之外还有一些其他的高级认证（安全性更上一层楼），都是国际科技龙头企业常用的一些 Http 认证。
Digest Authentication（摘要认证）

OAuth 1.0/2.0 => 一种授权框架

Bearer Authentication（承载认证）

NTLM Authentication

Negotiate Authentication（协商认证）

AWS Signature Version 4
"""

# 目标网站
target_url = "https://ssr2.scrape.center/"

# 请求表头
header = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                  "Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0"
}

spider_ssr3.spider_URL(target_url, header)
