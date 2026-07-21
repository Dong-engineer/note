"""
HTTP_Client 的学习
"""
import os
from urllib3.util import Retry, Timeout
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy
from selenium.webdriver.common.proxy import ProxyType
from selenium.webdriver.remote.client_config import ClientConfig


def test_start_remote_with_client_config(grid_server):
    # 选择代理模式，这是因为是远端操作，所以选自动代理就好
    proxy = Proxy({"proxyType": ProxyType.AUTODETECT})
    # 设置重新链接等配置，connect => 重新连接2次，read => 重新读取2次，redirect => 重新定向2次
    retries = Retry(connect=2, read=2, redirect=2)
    # 设置响应时间，connect => 链接超时为300s，read => 读取时间超时为6min
    timeout = Timeout(connect=300, read=3600)
    # 本地客户端配置
    """
    remote_server_addr => 远端控制的地址
    proxy => 远端代理地址
    init_args_for_pool_manager => 初始化管理配置，这里主要配置了 retries 属性和 timeout 属性
    ca_certs => TLS证书配置，如果有了 TLS 证书，那么本地和远端的通信会被加密
    username password => 本地的 Gird 用户身份验证
    """
    client_config = ClientConfig(remote_server_addr=grid_server,
                                 proxy=proxy,
                                 init_args_for_pool_manager={
                                     "init_args_for_pool_manager": {"retries": retries, "timeout": timeout}},
                                 ca_certs=_get_resource_path("tls.crt"),
                                 username="admin", password="myStrongPassword")
    options = get_default_chrome_options()
    driver = webdriver.Remote(command_executor=grid_server, options=options, client_config=client_config)
    driver.get("https://www.selenium.dev")
    driver.quit()


def _get_resource_path(file_name: str):
    if os.path.abspath("").endswith("tests"):
        path = os.path.abspath(f"resources/{file_name}")
    else:
        path = os.path.abspath(f"tests/resources/{file_name}")
    return path


def get_default_chrome_options():
    options = webdriver.ChromeOptions()
    options.add_argument("--no-sandbox")
    return options
