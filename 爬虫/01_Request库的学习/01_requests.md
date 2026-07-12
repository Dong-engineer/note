# Requests

### **一、请求方法**

| 方法                                                 | 说明              | 示例                                                         |
| ---------------------------------------------------- | ----------------- | ------------------------------------------------------------ |
| `requests.get(url, params=None, **kwargs)`           | 发送 GET 请求     | `response = requests.get('https://api.example.com/data')`    |
| `requests.post(url, data=None, json=None, **kwargs)` | 发送 POST 请求    | `response = requests.post('https://api.example.com/submit', json={'key': 'value'})` |
| `requests.put(url, data=None, **kwargs)`             | 发送 PUT 请求     | `response = requests.put('https://api.example.com/update', data={'key': 'new_value'})` |
| `requests.delete(url, **kwargs)`                     | 发送 DELETE 请求  | `response = requests.delete('https://api.example.com/delete/1')` |
| `requests.head(url, **kwargs)`                       | 发送 HEAD 请求    | `response = requests.head('https://api.example.com')`        |
| `requests.options(url, **kwargs)`                    | 发送 OPTIONS 请求 | `response = requests.options('https://api.example.com')`     |

### **二、常用参数**

| 参数              | 说明                                                         |
| ----------------- | ------------------------------------------------------------ |
| `params`          | URL 参数（字典或字节），会自动编码到 URL 中。 示例：`params={'key1': 'value1', 'key2': 'value2'}` |
| `data`            | 表单数据（字典、元组列表、字节或文件对象），会被编码为表单格式（`application/x-www-form-urlencoded`）。 示例：`data={'key': 'value'}` |
| `json`            | JSON 数据（字典），会自动转换为 JSON 并设置 `Content-Type: application/json`。 |
| `headers`         | 请求头（字典）。 示例：`headers={'User-Agent': 'Mozilla/5.0'}` |
| `cookies`         | Cookies（字典或 `CookieJar` 对象）。                         |
| `files`           | 文件上传（字典）。 示例：`files={'file': open('test.jpg', 'rb')}` |
| `auth`            | 认证信息（元组或自定义认证对象）。 示例：`auth=('user', 'pass')` |
| `timeout`         | 超时时间（秒），防止请求挂起。 示例：`timeout=5`（连接和读取均超时）或 `timeout=(3, 5)`（连接超时 3 秒，读取超时 5 秒） |
| `allow_redirects` | 是否允许重定向（布尔值），默认为 `True`。                    |
| `proxies`         | 代理服务器（字典）。 示例：`proxies={'http': 'http://proxy.example.com:8080'}` |
| `verify`          | 是否验证 SSL 证书（布尔值或证书路径），默认为 `True`。       |

### **三、响应对象属性**

| 属性                   | 说明                                               |
| ---------------------- | -------------------------------------------------- |
| `response.status_code` | HTTP 状态码（如 `200`, `404`）。                   |
| `response.text`        | 响应内容（字符串，自动解码）。                     |
| `response.content`     | 响应内容（字节），适合二进制数据（如图像、文件）。 |
| `response.json()`      | 解析 JSON 响应（返回字典）。                       |
| `response.headers`     | 响应头（字典）。                                   |
| `response.cookies`     | 响应的 Cookies（`CookieJar` 对象）。               |
| `response.url`         | 最终请求的 URL（处理重定向后）。                   |
| `response.history`     | 重定向历史（响应对象列表）。                       |
| `response.encoding`    | 响应内容的编码（如 `'utf-8'`）。                   |
| `response.elapsed`     | 请求耗时（`datetime.timedelta` 对象）。            |

### **四、异常处理**

| 异常类                                 | 说明                                |
| -------------------------------------- | ----------------------------------- |
| `requests.exceptions.RequestException` | 所有请求异常的基类。                |
| `requests.exceptions.Timeout`          | 请求超时。                          |
| `requests.exceptions.TooManyRedirects` | 重定向次数过多。                    |
| `requests.exceptions.HTTPError`        | HTTP 错误（如 404、500）。          |
| `requests.exceptions.ConnectionError`  | 连接错误（如 DNS 失败、拒绝连接）。 |

**当请求返回非 200 状态码时，requests 库默认不会抛出异常**。你需要显式调用 `response.raise_for_status()` 来触发异常。