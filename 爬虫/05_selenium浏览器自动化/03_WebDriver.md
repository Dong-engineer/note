# WebDriver

## 功能介绍

`webdriver` 是 `selenium` 最为核心的接口，可以说后面要学习的内容几乎都是 `webdriver` 的 `API` 接口。

其主要功能是创建/关闭浏览器会话。

## 源码

```python
# 在上一节说过所有的代码内容均以 Edge 浏览器为主，这里开发者不难发现 WebDriver 继承了 ChromiumDriver
# 明明使用的是 Edge 浏览器，为什么继承了谷歌浏览的驱动呢？（请带着疑问继续学习吧）
class WebDriver(ChromiumDriver):

    # webdriver 的初始化，使用了类型参数提示，其在初始化时接受的两个参数类型分别为 Options、Service
    def __init__(
        self,
        options: Optional[Options] = None,
        service: Optional[Service] = None,
        keep_alive: bool = True,
    ) -> None:

        service = service if service else Service()
        options = options if options else Options()

        """
        创建一个新的 Chrome 驱动实例。
		启动服务，然后创建新的 Chrome 驱动实例。
		参数：
		options：接收 ChromeOptions 的一个实例。
		service：用于处理浏览器驱动的服务对象，若需要传递额外详细信息时使用。
		keep_alive：是否配置 ChromeRemoteConnection 以使用 HTTP 长连接。
        """

        # 这里直接使用父类的初始化方法
        super().__init__(
            browser_name=DesiredCapabilities.EDGE["browserName"],
            vendor_prefix="ms",
            options=options,
            service=service,
            keep_alive=keep_alive,
        )
```

## 使用示例

```python
"""
第一个 selenium 脚本
"""
from selenium import webdriver
from selenium.webdriver.edge.options import Options

# 驱动设置
option = Options()
option.add_experimental_option("detach", True)
driver = webdriver.Edge(options=option)

driver.get("https://www.baidu.com")

```

