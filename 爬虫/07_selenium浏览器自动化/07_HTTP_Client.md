# HTTP_Client

`HTTP_Client` 主要用于远端控制操作。

这里说一下 `selenium` 的远端控制原理，如下：

本地自动化流程：

```tex
本地脚本 → 本地 HTTP_Client → 本地浏览器驱动 → 本地浏览器
```

远程控制流程：

```tex
本地脚本（控制端） → 本地 HTTP_Client → 网络 → 远程电脑（被控制端）的浏览器驱动（服务器模式） → 远程浏览器
```

`HTTP_Client` 核心职责：

1. **请求封装**：将 Selenium API 转化为符合 [W3C WebDriver 规范](https://www.w3.org/TR/webdriver/) 的 HTTP 请求（方法、URL、请求体）；
2. **网络传输**：通过 HTTP/HTTPS 协议与驱动服务器通信（默认是本地 `http://localhost:端口`）；
3. **响应解析**：解析驱动返回的 JSON 格式响应，转化为脚本可识别的对象（如 `WebElement`、`bool`、`str`）；
4. **异常处理**：捕获 HTTP 错误（如 404 驱动未启动、500 操作失败），转化为 Selenium 异常（如 `NoSuchElementException`、`WebDriverException`）；
5. **会话管理**：维护与驱动的会话（Session），每个 `WebDriver` 实例对应一个会话 ID，HTTP_Client 会自动在请求中携带该 ID 标识上下文。

## 使用实例

```python
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

```

以上代码不可以直接运行！

本节算是 `selenium` 的远端控制的开头。

对于参数 `grid_server` 和一些代码开发者可能还是有疑惑，在这里会得到解答。

`grid_server` 该参数实际是 `selenium` 希望接受 `selenium Grid` 的服务器地址（这段代码来自于 `selenium` 官方，至于什么是 `Grid` ，开发者可暂时理解为一个可以控制多台远端机器的工具），开发者在使用 `Grid` 是忘开发者处于安全的环境中，所以需要配 `ca_certs` 参数来对通信进行加密，且本地机在使用 `Grid` 时是需要身份验证的，这就需要配置 `username、password` 。

## 注意事项

在进行远端控制时对于本地、远端都是有环境要求的。

1. 本地环境配置要求：含有 `selenium` 即可且最好有 `Grid`

2. 远端环境配置要求：浏览器、浏览器驱动（无需含有 `selenium` ，本地的 `selenium` 会帮助开发者将代码通过 HTTP 传输过去）

3. 远端机还需要以服务器模式开启浏览器驱动并允许本地访问，具体操作如下：

   ```cmd
   cd C:\chromedriver
   ```

   ```cmd
   chromedriver --port=9515 --allowed-ips=0.0.0.0
   ```

   以上为示例，需要更换路径和约定好的端口号（`--port` ，浏览器驱动会监听该端口），`--allowed-ips` 允许访问的 IP （成功后会有提示）。

4. 机器需要在同一局域网中

以上本地与远端均为 windows 为准，其他操作系统操作也是如此，不过需要更改至其相应的配置信息。