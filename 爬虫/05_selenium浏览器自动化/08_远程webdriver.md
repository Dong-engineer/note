# 远程 webdriver

如果远程计算机上正在运行 [Selenium Grid](https://www.selenium.dev/zh-cn/documentation/grid/), 则 Selenium 允许您自动化远程计算机上的浏览器. 执行代码的计算机称为客户端计算机, 具有浏览器和驱动程序的计算机称为远程计算机, 有时也称为终端节点. 要将 Selenium 测试指向到远程计算机, 您需要使用  类并传递包含该机器上网格端口的URL. 

在上节 `HTTP_Client` 中已经有了 `Remote WebDriver` 的使用，不过在上节并没有详细讲述。

## 源码

`RemoteWebDriver` 在源码中是直接以 `WebDriver` 为名的，如下：

```python
class WebDriver(BaseWebDriver):

    _web_element_cls = WebElement
    _shadowroot_cls = ShadowRoot

    def __init__(
        self,
        command_executor: Union[str, RemoteConnection] = "http://127.0.0.1:4444",
        keep_alive: bool = True,
        file_detector: Optional[FileDetector] = None,
        options: Optional[Union[BaseOptions, list[BaseOptions]]] = None,
        locator_converter: Optional[LocatorConverter] = None,
        web_element_cls: Optional[type[WebElement]] = None,
        client_config: Optional[ClientConfig] = None,
    ) -> None
```

这仅仅是初始化的方法名和参数，并没有方法体（主要是因为太多了）。

一个一个来看。

`command_executor` ：远程服务的 URL 地址，默认为 "http://127.0.0.1:4444"（127.0.0.1 本地主机地址）。

`keep_alive` ：（已经弃用）是否将 `remote_connection.RemoteConnection` 配置为使用 HTTP 持久连接。默认值为 True。

`file_detector` ：在实例化期间传递一个自定义文件检测器对象。如果为 None，则将使用默认的 `LocalFileDetector ()`。

`locator_converter` ：自定义定位转换器，默认为 `None`。

`web_element_cls` ：用于网页元素的自定义类。默认为 `WebElement`。

剩下两个不再写了，前面都有用到。

在本节开发者只需要注意 `command_executor、file_detector、client_config` 这两个即可。

从源码不难看出 `RemoterWebDriver` 的层级非常高，所有的内核驱动类继承了他。

## 使用示例

```python
# 以下代码不能直接使用，要想知道如何使用请看上节
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

print(driver.command_executor)  # http://127.0.0.1:4443

```

这里的代码仅是示例代码（含有错误的），开发者应当请求其他目标主机而不是本地主机，这里主要是讲述 `command_executor` 和 `remote_server_addr` 。

二者均是目标主机的地址，代码中配置了两次，在源码中 `command_exector` 的值赋值给了 `remoter_server_addr` 所以请求目标主机的地址应以 `command_exector` 的值为主。

### 自定义 `file_detector`

大多数时候开发者需要自定义 `file_detector`，开发者需要自定义并继承 `selenium` 中的 `LocalFileDetector`  

#### 源码

```python
class FileDetector(metaclass=ABCMeta):
    @abstractmethod
    def is_local_file(self, *keys: AnyKey) -> Optional[str]:
        """
        判断传入的路径是否为“本地需要上传的文件”
        这里的参数可以接受多个参数，开发者
        param path: 传入的文件路径（可能是本地路径、网络路径等）
        return: True = 是本地文件（需要Selenium自动上传）；False = 不需要处理
        """
        raise NotImplementedError
```

```python
class LocalFileDetector(FileDetector):

    def is_local_file(self, *keys: AnyKey) -> Optional[str]:
        
        file_path = "".join(keys_to_typing(keys))

        with suppress(OSError):
            if Path(file_path).is_file():
                return file_path
        return None
```

#### 自定义文件检测器

```python
from selenium.webdriver.remote.file_detector import LocalFileDetector
import os

class MyLocalFileDetector(LocalFileDetector):
    
    def is_local_file(self, *keys: AnyKey) -> bool:
        # 处理路径（相对路径→绝对路径）
        absolute_path = os.path.abspath(path)
        # 校验是否为文件（不是目录）且存在
        return os.path.isfile(absolute_path) and os.path.exists(absolute_path)

    # 这里开发者可以再定义一个获取文件路径的方法
    def get_local_file_path(self, path: str) -> str:
        # 强制从项目根目录拼接路径
        project_root = os.path.dirname(os.path.abspath(__file__))
        return os.path.join(project_root, path)
```

#### 总结

`selenium` 官方原话是这么说的：

> 对于远程WebDriver会话, [上传文件](https://www.selenium.dev/zh-cn/documentation/webdriver/elements/file_upload/) 更为复杂, 因为要上传的文件可能在执行代码的计算机上, 但远程计算机上的驱动程序正在其本地文件系统上查找提供的路径. 解决方案是使用本地文件检测器. 设置一个后, Selenium将捆绑文件, 并将其发送到远程计算机, 以便驱动程序可以看到对它的引用. 默认情况下, 某些实现包含一个基本的本地文件检测器, 并且所有这些实现都允许自定义文件检测器

开发者可以将文件检测器理解为一个中间人，该中间人会将本地主机的文件打包成数据流传输给远端主机。然后进行上传文件的操作。
