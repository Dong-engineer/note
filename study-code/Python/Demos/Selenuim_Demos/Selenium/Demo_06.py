from urllib3.util import Retry, Timeout
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy
from selenium.webdriver.common.proxy import ProxyType
from selenium.webdriver.remote.client_config import ClientConfig

proxy = Proxy({"proxyType": ProxyType.AUTODETECT})
retries = Retry(connect=2, read=2, redirect=2)
timeout = Timeout(connect=3000, read=3600)
client_config = ClientConfig(remote_server_addr="http://127.0.0.1:4444",
                             proxy=proxy,
                             init_args_for_pool_manager={"init_args_for_pool_manager": {"retries": retries,
                                                                                        "timeout": timeout}},
                             ca_certs="",
                             username="admin", password="myStrongPassword")
option = webdriver.ChromeOptions()
driver = webdriver.Remote(command_executor="http://127.0.0.1:4443", options=option, client_config=client_config)

print(driver.command_executor)
