# Wait

## 等待策略

在对网页进行测试时，最大的挑战就是能够准确的执行每一个 `selenium` 命令，不同的页面可能会有不同的加载策略 `readyState` 。

在整个自动化测试过程中，所有的导航命令（可以理解为 `selenium` 命令）都会等待页面加载完成后再去执行，准确来说页面加载完成后浏览器驱动会拿到控制权，这时代码才可以和浏览器驱动正常通信。然而页面的加载策略仅限 `HTML` 内容并不包含 `JavaScript` ，也就是说页面加载完成后仅有 `HTML` 内容， `HTML` 内容加载完成后会将控制权交给浏览器驱动，然后 `selenium` 命令就开始执行，此时页面上的内容并不一定加载完成，因为部分数据很有可能是通过 `JavaScript` 来渲染的。所以 `selenium` 就需要等待一段时间再执行。

`selenium` 提供了两种等待方式，一种是隐式等待，一种是显示等待。

> 注意：这两种等待不要一起使用，一起使用会出现误差。

## 源码

```python
class WebDriverWait(Generic[D]):
    def __init__(
        self,
        driver: D,
        timeout: float,
        poll_frequency: float = POLL_FREQUENCY,
        ignored_exceptions: Optional[WaitExcTypes] = None,
    ):
        """
        WebDriverWait 初始化方法
        param：
        	driver: 接受一个 WebDriver 驱动实例
        	timeout: 显示等待总时长
        	poll_frequency: 轮询频率，即每次调用预期函数的间隔时间
        	ignored_exception: 忽略的异常。默认情况下，只有 NosuchElementException
        """
        self._driver = driver
        self._timeout = float(timeout)
        self._poll = poll_frequency
        
        if self._poll == 0:
            self._poll = POLL_FREQUENCY
        exceptions: list = list(IGNORED_EXCEPTIONS)
        if ignored_exceptions:
            try:
                exceptions.extend(iter(ignored_exceptions))
            except TypeError:
                exceptions.append(ignored_exceptions)
        self._ignored_exceptions = tuple(exceptions)

    def __repr__(self) -> str:
        return f'<{type(self).__module__}.{type(self).__name__} (session="{self._driver.session_id}")>'

    
    def until(self, method: Callable[[D], Union[Literal[False], T]], message: str = "") -> T:
        """
        等待预期函数条件满足，获取元素
        param：
        	method: 接受一个函数
        	massage: 抛出的信息
        return：
        	返回 method 的返回值
        throws:
        	TimeoutException
        """
        screen = None
        stacktrace = None

        end_time = time.monotonic() + self._timeout
        while True:
            try:
                value = method(self._driver)
                if value:
                    return value
            except self._ignored_exceptions as exc:
                screen = getattr(exc, "screen", None)
                stacktrace = getattr(exc, "stacktrace", None)
            if time.monotonic() > end_time:
                break
            time.sleep(self._poll)
        raise TimeoutException(message, screen, stacktrace)

    def until_not(self, method: Callable[[D], T], message: str = "") -> Union[T, Literal[True]]:
        """
        等待预期函数条件不满足，获取元素，这里开发者一定要注意，until_not() 和 util() 二者代码逻辑一样，都是判断预期函数是否为 True，但是 until_not() 的目的时为了去判断某个元素从页面消失（也就说元素在页面上这个条件为 False）
        param：
        	method: 接受一个函数
        	massage: 抛出的信息
        return：
        	返回 method 的返回值
        throws:
        	TimeoutException
        """
        end_time = time.monotonic() + self._timeout
        while True:
            try:
                value = method(self._driver)
                if not value:
                    return value
            except self._ignored_exceptions:
                return True
            if time.monotonic() > end_time:
                break
            time.sleep(self._poll)
        raise TimeoutException(message)

```



## 隐式等待

隐式等待，这是一种全局设置，是 `selenium` 内置自动等待元素出现的方式。

在 `Options` 一节中的 `timeouts` 属性就是定义隐式等待的时长。

```python
options.timeouts = {"implicit": 300}
```

开发者还可以直接通过驱动实例

```python
driver.implicitly_wait(2)
```

## 显示等待

显示等待，在定义显示等待之前，开发者应当先定义一个预期函数 ，开发者在使用 `WebDriverWait` 时，会进入一个轮询等待，直到预期函数评估为真时，才退出循环并继续执行代码中的下一个命令.。如果在指定的超时值之前条件未满足, 代码将给出超时错误。

```python
wait = WebDriverWait(driver, timeout=2)
```

预期函数开发者也无需手动实现，`selenium` 官方提供了 EC 模块去判断页面元素是否出现。

如下：

| 函数名                                                       | 功能                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `EC.visibility_of_element_located((定位方式, 定位值))`       | 元素**可见**（存在于 DOM + 非隐藏，宽高 > 0）（最常用）      |
| `EC.presence_of_element_located((定位方式, 定位值))`         | 元素**存在于 DOM 中**（无需可见，适用于元素加载慢但不立即显示） |
| `EC.element_to_be_clickable((定位方式, 定位值))`             | 元素**可点击**（可见 + 启用状态，如按钮未禁用）（最常用）    |
| `EC.text_to_be_present_in_element((定位方式, 定位值), text)` | 元素文本中**包含指定字符串**（如验证提示信息）               |
| `EC.text_to_be_present_in_element_value((定位方式, 定位值), value)` | 元素值（value 属性）中**包含指定字符串**（如输入框）         |
| `EC.title_contains(title)`                                   | 页面标题**包含指定字符串**                                   |
| `EC.title_is(title)`                                         | 页面标题**完全等于指定字符串**                               |
| `EC.invisibility_of_element_located((定位方式, 定位值))`     | 元素**不可见**或**不存在于 DOM 中**（如加载弹窗消失）        |
| `EC.element_to_be_selected(element)`                         | 元素**被选中**（适用于复选框、单选框）                       |
| `EC.frame_to_be_available_and_switch_to_it((定位方式, 定位值))` | iframe 可用并**切换到该 iframe**（处理嵌套页面）             |
| `EC.alert_is_present()`                                      | 页面**出现弹窗（alert/confirm/prompt）**（返回弹窗对象）     |

## 使用实例

```python
# 等待输入框可见并输入文本
input_box = wait.until(EC.visibility_of_element_located((By.NAME, "username")))
input_box.send_keys("test_user")

# 等待密码框可点击并输入
pwd_box = wait.until(EC.element_to_be_clickable((By.ID, "password")))
pwd_box.send_keys("123456")

```

