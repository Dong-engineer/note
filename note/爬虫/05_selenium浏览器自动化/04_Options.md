# Options

## 功能介绍

这是浏览器的选项配置信息，开发者可以通过该类获取浏览的信息。

每一个浏览器都有其自己的 `Options` 类，但这并不代表他们是杂乱的。`Options` 的顶层父类是 `BaseOptions` （注意是顶层父类）所有的浏览器内核、移动平台的浏览器都是间接继承 `BaseOptions` 类的。

所有 `Options` 的关系结构图：

```text
BaseOptions（顶层抽象类）
├── ChromiumOptions（Chromium 内核通用类）
│   ├── ChromeOptions（Chrome 专属）
│   ├── EdgeOptions（Edge 专属）
│   ├── OperaOptions（Opera 专属）
│   └── BraveOptions（Brave 专属）
├── GeckoOptions（Gecko 内核通用类）
│   └── FirefoxOptions（Firefox 专属）
├── WebKitOptions（WebKit 内核通用类）
│   └── SafariOptions（Safari 专属）
├── AndroidOptions（Android 平台通用类）
│   └── ChromeOptions（Android Chrome 专属，继承自 AndroidOptions + ChromiumOptions）
└── IOSEptions（iOS 平台通用类）
    └── SafariOptions（iOS Safari 专属，继承自 IOSEptions + WebKitOptions）
```

这些均有一个子类就是 `Options` 。

这些类并非每一个都要去学习，`Chrom` 的内核最为常见且使用也很多，然后是 `WebKite` （和 `Chrom` 内核有很多相似之处），最后是 `Gecko` （`FireFox` 内核，学习其一个特点即可）。

## 源码

### `edge.Options`

```python
# 每一个浏览驱动包中都有一个 Options 类，这里是从 selenium.webdriver.edge.options 引入的 Options
# 所以该 Options 均是 Edge 浏览器的配置选项
class Options(ChromiumOptions):
    KEY = "ms:edgeOptions"

    def __init__(self) -> None:
        """初始化浏览器默认的选项配置"""
        super().__init__()
        self._use_webview = False

    @property
    def use_webview(self) -> bool:
        """
        控制是否启用 WebView2 模式
        """
        return self._use_webview

    @use_webview.setter
    def use_webview(self, value: bool) -> None:
        """
        通过 setter 方法修改 use_webview 的值
        """
        self._use_webview = bool(value)

    def to_capabilities(self) -> dict:
        """
        将浏览器配置数据转化为 webdrive 所需要的字典形式
        如果开启了 webview 模式，那么浏览器的 browserName 属性改成 webview2
        """
        caps = super().to_capabilities()
        if self._use_webview:
            caps["browserName"] = "webview2"

        return caps

    @property
    def default_capabilities(self) -> dict:
        """
        返回浏览器默认的配置信息，返回类型为字典
        """
        return DesiredCapabilities.EDGE.copy()

```

这里补充一下什么是 `webview2` ，`webview2` 指的是**可嵌入的浏览器控件**。这这种控件可以对**桌面端客户程序**赋能，其最具有代表性的就是 Microsoft Office、Adobe Creative Cloud。

一般的桌面端客户程序一般都是传统的 `CS` 架构，其代码大部分都是由C++、.NET 等桌面编程技术完成的，但有些部分界面可能需要 HTML、CSS、JS 来完成，Steam 就是一个典型例子，用户在文件夹中不难发现其是一个 Internet 快捷方式。

### `BaseOptions`

```py
# 所有 Options 类的基类
class BaseOptions(metaclass=ABCMeta):
	
    # 浏览器版本
    browser_version = _BaseOptionsDescriptor("browserVersion")
	
    # 操作系统环境名称
    platform_name = _BaseOptionsDescriptor("platformName")
	
    # 严格控制文件上传
    strict_file_interactability = _BaseOptionsDescriptor("strictFileInteractability")

    # 窗口重定位
    set_window_rect = _BaseOptionsDescriptor("setWindowRect")

    # 双向文本支持
    enable_bidi = _BaseOptionsDescriptor("enableBidi")
	
    # 页面加载策略
    page_load_strategy = _PageLoadStrategyDescriptor("pageLoadStrategy")

    # 出现 User Prompt Handler 如何操作
    unhandled_prompt_behavior = _UnHandledPromptBehaviorDescriptor("unhandledPromptBehavior")

   	# 响应超时时长
    timeouts = _TimeoutsDescriptor("timeouts")
	
    # IP 代理
    proxy = _ProxyDescriptor("proxy")

    # 下载功能
    enable_downloads = _BaseOptionsDescriptor("se:downloadsEnabled")

    # WebSocket 连接地址
    web_socket_url = _BaseOptionsDescriptor("webSocketUrl")

    def __init__(self) -> None:
        """
        初始化方法
        """
        super().__init__()
        self._caps = self.default_capabilities
        self._proxy = None
        self.set_capability("pageLoadStrategy", PageLoadStrategy.normal)
        self.mobile_options: Optional[dict[str, str]] = None
        self._ignore_local_proxy = False

    @property
    def capabilities(self):
        """
        @property 语法糖修饰，表示开发者可以通过访问属性的方式来调用该函数
        """
        return self._caps

    def set_capability(self, name, value) -> None:
        """设置 capability"""
        self._caps[name] = value

    def enable_mobile(
        self,
        android_package: Optional[str] = None,
        android_activity: Optional[str] = None,
        device_serial: Optional[str] = None,
    ) -> None:
        """
        支持移动浏览器功能的浏览器启用该功能
        Args:
            android_package：要启动的安卓包的名称
			android_activity：安卓活动的名称
			device_serial：设备序列号
        """
        if not android_package:
            raise AttributeError("android_package must be passed in")
        self.mobile_options = {"androidPackage": android_package}
        if android_activity:
            self.mobile_options["androidActivity"] = android_activity
        if device_serial:
            self.mobile_options["androidDeviceSerial"] = device_serial

    @abstractmethod
    def to_capabilities(self):
        """ 将选项转换为功能字典，该方法时抽象方法，后面每个浏览的 Options 会去实现 """

    @property
    @abstractmethod
    def default_capabilities(self):
        """ 以字典形式返回必要的功能. """

    def ignore_local_proxy_environment_variables(self) -> None:
        """ 禁用 HTTP/HTTPS 代理功能（仅针对读取本地环境变量的场景） """
        self._ignore_local_proxy = True

```

在开发时常用的配置信息基本来自于 `BaseOptions` ，除了其类成员属性标识了浏览器信息，请将目光定格在 `__init()__` 初始化方法中，开发不难发其在初始化时，还初始化了一些其他属性，其中 `self.caps` 属性也是常用的属性之一，开发者可以通过 `def capabilities(self)` 该函数获取 `caps` 属性。

说到这里又要讲述一下 `Capabilities` ，官方文档是这样说的

> 在 Selenium 3 中, capabilities是借助"Desired Capabilities"类定义于会话中的. 从 Selenium 4 开始, 您必须使用浏览器选项类. 对于远程驱动程序会话, 浏览器选项实例是必需的, 因为它确定将使用哪个浏览器.
>
> 这些选项在 [Capabilities](https://w3c.github.io/webdriver/#capabilities) 的 w3c 规范中进行了描述.

简单来说 `Capabilities` 实际是一个字典类，其主要作用就是以键值对的形式存储浏览器的 `Options` 配置信息。而 `Capabilities` 在实际使用中常以属性的方式进行使用访问。开发者记住这两点即可。

## 使用示例

这里的使用示例并不会按照源码所示属性或函数的顺序来讲述，而是依据官方文档介绍来讲述。

```python
# 导入库
from selenium.webdriver.edge.options import Options

# 创建 Options 实例
edge_options = Options()

"""
提示：不要试图对任何属性进行赋值运算，会报异常。

这里逻辑上来讲是错误的操作，开发者应该通过 webdriver 来访问浏览器属性，
因为 Options 和浏览器是没有直接通信的
"""
# browserName => 浏览器名称
print(edge_options.capabilities["browserName"])

"""
这里会报异常，首先 browserVersion 属性不在 capabilities 字典中，
但在 BaseOptions 中不难发现 browserVersion 属性的身影，正如下面所示结果为 None，
也正如上面所说不应该通过 Options 直接访问浏览器配置信息，应当通过 webdriver 来访问。
"""
# browserVersion => 浏览器版本
print(edge_options.capabilities["browserVersion"])
print(edge_options.browser_version)  # None
```

正确示例：

```python
"""
Options 类的正确使用
"""
# 导入库
from selenium.webdriver.edge.options import Options
from selenium import webdriver
from selenium.webdriver import Proxy
from selenium.webdriver.common.proxy import ProxyType

# 创建 Options 实例
edge_options = Options()
# 页面加载策略（这里可以先跳过去）
edge_options.page_load_strategy = "normal"
# 网站信任（这里可以先跳过去）
edge_options.accept_insecure_certs = False
# 程序执行响应时间（这里可以先跳过去）
edge_options.timeouts = {"implicit": 0}
edge_options.timeouts = {"pageLoad": 30000}
edge_options.timeouts = {"script": 300000}
# 出现 User Prompt Handler 如何操作（这里可以先跳过去）
edge_options.unhandled_prompt_behavior = "dismiss"
# 是否开启浏览器窗口大小和位置重定位，4.0版本后浏览器是直接支持该功能的，无需再手动设置（这里可以先跳过去）
# edge_options.set_window_rect = True
# 是否严格控制文件上传
edge_options.strict_file_interactability = True
# IP 代理
edge_options.proxy = Proxy({"proxyType": ProxyType.AUTODETECT})

# 驱动实例，要将 Options 实例在 webdriver 初始化时重新配置
driver = webdriver.Edge(options=edge_options)

# browserName => 浏览器名称
print(driver.capabilities["browserName"])

# browserVersion => 浏览器版本
print(driver.capabilities["browserVersion"])

# pageLoadStrategy => 页面加载策略
"""
pageLoadStrategy    readyState          解释

normal	            complete	        默认值, WebDriver一直等到 load 事件触发并返回
eager	            interactive	        WebDriver一直等到 DOMContentLoaded 事件触发并返回
none	            Any	                WebDriver 仅等待初始页面已下载
"""
print(driver.capabilities["pageLoadStrategy"])

# platformName => 运行环境操作系统的名称
"""
在本地运行，如下面代码这样，并未在 Grid 或其他的远程操控平台上运行，会返回本地的操作系统名称。
如果开发者在远程操控平台上运行，那么请继续学习后面会讲述。
"""
print(driver.capabilities["platformName"])  # window

# acceptInsecureCerts => 是否信任网站证书
"""
类型：bool
默认 False => 不信任，遇见网站证书过期、有问题直接返回 insecure certificate error
True => 信任，于 False 相反
"""
print(driver.capabilities["acceptInsecureCerts"])

# timeouts => 响应时间是否超时
"""
类型：dict
该字典中含有三个值
implicit pageLoad script，三个值同时生效
implicit => 开发者在查找元素时，如果找不到元素所等待的时间
pageLoad => 页面加载时间，超时会抛出 TimeoutException
script   => 当开发者通过 selenium 执行 JS 脚本时，如果脚本在指定时间内未执行完成，会抛出 TimeoutException
其默认值如下
"""
print(driver.capabilities["timeouts"])  # {'implicit': 0, 'pageLoad': 300000, 'script': 30000}

# unhandledPromptBehavior => 当页面中出现了用户提示弹窗程序应当如何操作
"""
dismiss             默认行为，直接取消，既无论什么弹窗都会直接点击取消
accept              直接点击确认
dismiss and notify  与 dismiss 行为一样，但是会抛出 UnhandledAlertException
accept and notify   与 accept 行为一样，但是会抛出 UnhandledAlertException
ignore              直接忽略弹窗，继续执行程序，但大概率会抛出异常 UnhandledAlertException
"""
print(driver.capabilities["unhandledPromptBehavior"])  # dismiss and notify

# setWidthRect => 是否开启浏览器窗口大小和位置的重新定位
"""
类型：bool
4.0 版本后默认支持该功能，官方也就把该属性从 capabilities 中删除了
"""
# print(driver.capabilities["setWindowRect"])

# strictFileInteractability => 严格控制文件上传
"""
类型：bool
为 True 时，webdriver 会先检测 <input type="file"> 元素是否处于可交互状态。
为 False 时，webdriver 会直接向  <input type="file"> 元素上传文件路径，但是如果 <input type="file"> 元素处于不可交互或隐藏，那么会抛出 ElementNotInteractableException
"""
print(driver.capabilities["strictFileInteractability"])

# proxy => IP 地址代理
"""
这里并不会完全将 proxy 讲述完，开发者请自行搜寻
类型：dict
键值对：{proxyType: ProxyType.MANUAL, 'httpProxy' : '代理地址'}
ProxyType 的属性：
DIRECT =>   默认，本机直接链接
MANUAL =>   设置为手动代理
PAC    =>   PAC 表示使用 PAC 脚本代理
AUTODETECT =>   代理自动检测（大概使用 WPAD）
SYSTEM  =>   使用系统设置（在 Linux 上默认为开启）
UNSPECIFIED =>  未初始化（供内部使用）
"""

driver.quit()
```

