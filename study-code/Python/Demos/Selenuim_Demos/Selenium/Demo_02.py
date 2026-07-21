"""
Options 类的错误使用
"""
# 导入库
from selenium.webdriver.edge.options import Options

# 创建 Options 实例
edge_options = Options()

"""
提示：不要试图对任何属性进行赋值运算，会报异常。

这里其实逻辑上来讲是错误的，开发者应该通过 webdriver 来访问浏览器属性，
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
# print(edge_options.capabilities["browserVersion"])
print(edge_options.browser_version)  # None
