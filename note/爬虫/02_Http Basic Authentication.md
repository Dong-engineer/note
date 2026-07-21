# Http Basic Authentication

HTTP Basic Authentication（HTTP 基本认证）是一种简单的 HTTP 认证方式。这里简称 HBA。

## 一、如何查看该网站是否含有 HBA

在客户端找到其响应标头，如果响应标头中含有 **`WWW-Authenticate`** 标识，则证明该网站需要 HTTP 基本认证。

## 二、工作原理

当客户端（如浏览器）向需要认证的服务器发起请求时，服务器会返回一个 401 Unauthorized 响应，并在响应头中包含`WWW-Authenticate: Basic realm="领域名称"`的信息，提示客户端需要进行基本认证。客户端收到该响应后，会弹出一个对话框，要求用户输入用户名和密码。用户输入后，客户端会将用户名和密码用冒号连接（如`admin:admin`），然后进行 Base64 编码，将编码后的字符串放在请求头的`Authorization: Basic 编码后的字符串`中发送给服务器。服务器收到后，对该字符串进行 Base64 解码，验证用户名和密码是否正确，若正确则返回请求的资源，否则继续返回 401 响应。

## 三、如何解决

知道工作原理后，就不难解决爬虫时遇见这种网站该如何进行请求访问了。

开发者只需要将含有目标网站认证过的账号和密码进行 Base64 编码，然后在请求标头中加上如下内容即可。

```json
authorization         Basic YWRtaW46YWRtaW4=
```

python 代码如下：

```python
import requests

# 用户名和密码
username = 'admin'
password = 'admin'

# 目标 URL
url = 'https://你的目标网址.com'

# 发送带认证的请求
response = requests.get(url, auth=(username, password))

# 查看响应
print(response.status_code)
print(response.text)
```

java代码，如下：

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HttpBasicAuthExample {
    public static void main(String[] args) throws Exception {
        String username = "admin";
        String password = "admin";
        String url = "https://你的目标网址.com";

        // 对用户名和密码进行 Base64 编码
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置认证头
        con.setRequestProperty("Authorization", "Basic " + encodedAuth);

        // 读取响应
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }
}
```

那么这就有兄弟说了，我要是有账号还用这样吗？那么作者这里表示如果你想绕开 HTTP 一类的认证访问网站，这里不教。

## 四、一些企业常用认证

| 认证方式                                 | 特点                                                         | 安全性                               | 应用场景                                       |
| ---------------------------------------- | ------------------------------------------------------------ | ------------------------------------ | ---------------------------------------------- |
| **Digest Authentication（摘要认证）**    | 客户端不直接发送明文密码，而是发送密码的哈希摘要；服务器会返回一个随机数（nonce），客户端结合 nonce、用户名、密码、请求方法和 URI 等生成哈希值发送给服务器 | 比基本认证高（避免明文密码传输）     | 对安全性要求稍高，但不想使用复杂认证的场景     |
| **OAuth 1.0/2.0**                        | 一种授权框架，不是直接的 HTTP 认证方式，但常用于 API 授权。OAuth 2.0 更简洁，通过令牌（Token）授权，无需暴露用户密码 | 高                                   | 第三方应用授权（如社交平台登录、API 访问授权） |
| **Bearer Authentication（承载认证）**    | 属于 OAuth 2.0 的一种应用，客户端在请求头中携带 `Authorization: Bearer <令牌>`，服务器验证令牌有效性 | 高（令牌需安全传输，通常配合 HTTPS） | 现代 API 服务（如 RESTful API）                |
| **NTLM Authentication**                  | 微软开发的认证协议，基于挑战 - 响应机制，过程较复杂          | 较高                                 | Windows 环境下的网络应用（如内部企业系统）     |
| **Negotiate Authentication（协商认证）** | 允许客户端和服务器协商使用哪种认证协议（如 NTLM 或 Kerberos） | 高                                   | 企业级应用，尤其是 Windows 域环境              |
| **AWS Signature Version 4**              | 亚马逊云服务的认证方式，通过对请求参数、时间戳等进行签名，验证请求的合法性和完整性 | 高                                   | AWS 云服务 API 访问                            |