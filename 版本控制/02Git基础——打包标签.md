#### 打包标签

`git tag` ，该命令会列出所有所有标签名称。

```bash
$ git tag
v0.1
v1.3
v1.4
```

#### 创建标签

Git 中的标签分为两种，一种为**轻量标签**，一种为**附注标签**。

轻量标签，开发者可以理解为它只是某个特定提交的引用。

附注标签，它是可以被校验的，其中包含打标签者的名字、电子邮件 地址、日期时间， 此外还有一个标签信息，并且可以使用 GNU Privacy Guard （GPG）签名并验证。

#### 附注标签

`git tag -a <tagName> -m <message>` ，该命令会创建一个辅助标签，标签会绑定当前分支最新提交；附注标签强制要求附带说明信息，`-m` 参数用于直接填写标签备注，不加 `-m` 会自动弹出编辑器填写备注。

`git show <tag>` 会展示标签信息。

```bash
$ git tag -a v1.4 -m "my version 1.4"
$ git tag
v0.1
v1.3
v1.4
```

```bash
$ git show v1.4
tag v1.4
Tagger: Ben Straub <ben@straub.cc>
Date:   Sat May 3 20:19:12 2014 -0700
my version 1.4
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700
    changed the version number
```

#### 轻量标签

`git tag <tagname>` ，该命令会创建一个轻量标签。

```bash
$ git tag v1.4-lw
$ git show v1.4-lw
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700
    changed the version number
```

