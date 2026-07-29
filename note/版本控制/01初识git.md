# 初次运行 Git 前的配置

本文档默认开发者已经阅读过 Git 官方所提供的 PDF，所以本文档不会对 Git 做任何介绍。只会对 Git 官方的文档的内容进行简化（默认开发者对 Git 有了一定的了解）。

## git config --system

开发者初次使用 Git 时，需要进行一些初始配置，开发者所要用到的命令—— `git config`。

该命令会修改 Git 的 `/etc/gitconfig` 文件（即 Git 的系统配置文件）中的信息，开发者想要查看或修改 `/etc/gitconfig` 该文件则需要用到 `--system` 指明查看或修改的文件为系统文件（但是通常开发者不会去修改该文件的任何信息，且修改该文件信息是需要管理员权限的）。

开发者可以使用 `git config --system --list` 查看该配置文件下有哪些信息，如下：

```bash
git config --system --list

# 系统级全局diff配置，纯文本类文件用astextplain工具对比
diff.astextplain.textconv=astextplain

# Git LFS大文件过滤：提交时清理转换大文件
filter.lfs.clean=git-lfs clean -- %f
# Git LFS大文件过滤：拉取时还原大文件
filter.lfs.smudge=git-lfs smudge -- %f
# Git LFS标准处理流程
filter.lfs.process=git-lfs filter-process
# 开启强制要求仓库必须启用LFS才能正常使用
filter.lfs.required=true

# HTTP请求SSL底层使用OpenSSL库
http.sslbackend=openssl
# SSL根证书文件路径，Git HTTPS连接校验证书用
http.sslcainfo=D:/Development/Git/mingw64/etc/ssl/certs/ca-bundle.crt

# 换行符自动转换：检出时Windows用CRLF，提交统一转LF
core.autocrlf=true
# 开启文件系统缓存，提升Git查询文件状态速度
core.fscache=true
# 禁用符号链接，Windows环境默认关闭
core.symlinks=false

# git pull 默认采用合并merge方式，不自动变基rebase
pull.rebase=false

# 凭据存储助手，调用系统Git凭据管理器保存账号令牌
credential.helper=manager
# Azure仓库凭据匹配时带上完整http路径区分多账号
credential.https://dev.azure.com.usehttppath=true

# 新建仓库默认初始分支名称为master
init.defaultbranch=master
```

## git config --global

Git 除了系统级别的配置信息，还有 `~/.gitconfig` 或 `~/config/git/config` 用户级别的配置信息，只针对 Git 的某一用户（默认当前用户）。

开发者想要查看或修改这一类文件，可以使用 `git config --global` 查看或修改该文件，修改后的配置信息会对该用户系统的所有仓库生效（开发者可以认为该文件为全局配置文件）。

```bash
git config --global --list

# 全局配置：提交代码时使用的邮箱地址
user.mail=jqd19838302431@163.com
# 全局配置：提交代码时显示的用户名
user.name=Dong
# 全局配置：另一个邮箱配置项，作用同user.mail，部分工具兼容识别
user.email=jqd19838302431@163.com
# Gitee平台凭证认证方式，generic代表通用账号密码/令牌认证模式
credential.https://gitee.com.provider=generic
# 安全目录配置，* 表示信任所有本地工作目录，规避git安全目录拦截报错
safe.directory=*
```

开发者想要查看某个单独配置的信息，可以使用 `git config --global <key>` ，`<key>` 表示为变量名称。

```bash
# 查看信息
git config --global user.name

Dong
```

开发者想要修改该配置的信息，可以使用 `git config --global <key> <value>` ，`<key>` 表示为变量名称，`<value>` 表示为值。

```bash
# 修改信息
git config --global user.name D

# 查看信息
git config --global user.name
D
```

## git config --local

当开发者想要修改当前所使用的仓库的配置信息，开发者可以使用 `--local` 进行指定，该命令有一个前提——开发者需要先进入目标仓库所在的路径下。

```bash
# 查看信息
git config --local --list

# 修改信息
git config --local <key> <value>
```

 
