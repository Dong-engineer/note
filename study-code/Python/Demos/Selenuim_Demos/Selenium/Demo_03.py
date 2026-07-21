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
PAC    =>   PAC 表示使用 PAC 脚本（）代理
AUTODETECT =>   代理自动检测（大概使用 WPAD）
SYSTEM  =>   使用系统设置（在 Linux 上默认为开启）
UNSPECIFIED =>  未初始化（供内部使用）
"""

driver.quit()
