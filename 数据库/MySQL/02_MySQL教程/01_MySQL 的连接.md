# MySQL 的连接

官方文档中所展现的指令的前提——配置好环境变量后，开发者才可以在命令行终端使用 `mysql` 指令。

## 连接服务器

开发者要连接服务器，则可以使用以下命令：

```cmd
mysql -h host -u user -P port -p password
```

示例：

假设开发者的服务器在 198.162.0.1 （内网）、RMD（这里是你的数据库内部的用户名）、 3306 端口

```cmd
mysql 198.162.0.1 RMD 3306
```

如果你的服务器运行在本地，则可以省略主机 IP 直接用户名加端口尝试连接，如下：

```cmd
mysql -u user -p port
```

### 连接成功

提示开发者，如下：

```cmd
Enter password:******
Welcome to the MySQL monitor...
```

### 连接失败

例如 ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/tmp/mysql.sock' (2)，这意味着 MySQL 服务器守护进程 (Unix) 或服务 (Windows) 未运行。

这里的详细错误信息不在解释，开发者可以自行去搜索信息，或者在官方文档的附录 B 中寻找错误。

## 断开连接

开发者进入数据库后，可使用以下命令退出（大小写都可以）：

```cmd
mysql> QUIT
Bye
```

