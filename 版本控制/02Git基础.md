# Git 基础

在日常的开发过程中尝试用的 Git 基本命令，都会在本文讲述。

## 建立 Git 仓库

`git init`，`git init` 会在指定的文件下初始一个 Git 仓库，以 .git 文件夹的形式存在，.git 文件夹就是开发者的本地仓库。

`git init` 只会建立一个空仓库，此时该仓库中是没有任何文件存在的，需要开发者使用其他命令向本地仓库中添加文件。

在 Git Bash 中使用命令建立仓库时，开发者要先进入指定目录文件下，假设开发者要对 `D:/projects/xxx` 建立一个 Git 仓库，在 Git Bash 中依次输入以下命令。

```bash
# 进入 d 磁盘
cd d:

# 查看该磁盘下的所有文件夹信息
ls

# 确定指定文件夹存在后，进入指定文件夹
cd projects/xxx

# 建立 Git 仓库
git init
```

建立仓库后，在 Git Bash 中，会有提示（在文件路径的末尾会有 master 字样，该字样为开发者安装 Git Bash 时所设置的字样，至于是哪一步设置的请开发者自行搜寻修相关信息）如下：

```bash
/d/projects/Web_projects/zgssd/code_resource/java-resource (master)
```

## 克隆仓库

开发者除了使用 `git init` 建立一个新仓库，开发者也可以克隆他人服务器上的仓库，从而达到在本地建立一个仓库的目的。

`git clone <url>` ，该命令会克隆他人服务器上的 Git 仓库。

假设开发者要克隆 github 上某个项目，项目地址为 `https://github.com/xxx/xxx` ，开发者可以在 Git Bash 中输入以下命令：

```bash
git clone https://github.com/xxx/xxx
```

`git clone <url>` 不仅支持 Http 协议，如：SSH 传输协议后者 git:// 传输协议等等。这使得开发者也可以在自己的服务器上配置 Git 。

## Git 工作流程

在正式往 Git 仓库添加文件时，先讲述一下 Git 是如何管理项目文件的，Git 会跟踪工作区的所有文件，并对这些文件进行标记，Git Bash 中查看这些文件时，文件会呈现不同的颜色。

Git 将文件分为四种状态： 

- 未跟踪文件 
- 修改文件 
- 已修改文件
- 已暂存文件

文件的状态的时许变化图如下：

![](./img/02_Git 工作流程图.png)

`git init` 开发者之前已经学习过了，这里不再讲述。

#### 跟踪文件

`git add <file>` ，使用该命令 Git 会跟踪指定文件，此时 Git 会持续监听文件的变化，但是 Git 仓库中此时并没有该文件。

#### 获取文件状态

`git status` ，改命令会详细展现 `git add` 所跟踪的所有文件信息，如下：

```bash
$ git status

On branch master

No commits yet

Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
        new file:   xxx/Test.java

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        .editorconfig
        .gitignore
        pom.xml
		...


```

on branch master 表示在主分支下。

No commits yet 表示没有一次提交记录。

Changes to be committed 表示已经暂存的文件，已暂存只有一个名为 `Test.java` 的文件。

Untracked files 表示暂未追踪的文件。

#### 取消文件的跟踪状态

在 Changes to be committed 后有一段 `(use "git rm --cached <file>..." to unstage)` 表示开发者可以使用 `git reset --cached <file>` 取消文件的跟踪状态。

```bash
$ git rm --cached xxx/Test.java

On branch master

No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        .editorconfig
        .gitignore
        pom.xml
        ...

nothing added to commit but untracked files present (use "git add" to track)
```

此时使用 `git status` Git 会提示开发者没有跟踪任何文件，请使用 `git add` 跟踪文件。

#### 提交文件至本地仓库

`git commite` ，使用该命令 Git 会将所跟踪的文件提交至本地仓库（注意是本地仓库，不是远程仓库，关于本地仓库和远程仓库的协作流程会在后续讲述）。

#### 跳过暂存区直接提交至本地仓库

`git commite -a` ，在前面的学习中，开发者知道如果想要将文件提交至本地仓库中，那么开发者必须先使用 `git add` 将文件提交至暂存区，然后使用 `git commite` 提交至本地仓库。但是开发者可以使用 `git commite -a` 跳过 `git add` 这一步（看似跳过实则没有跳过，只是简化了命令）。

在使用 `git commit -a` 这一命令时，开发者要注意，`git commite -a` 只会提交**所有已跟踪的文件**，在这些已跟踪的文件中，有些开发者并不想提交，那么请慎用 `git commite -a` 。

#### 移除文件

`git rm <fileName>` ，Git 不建议开发者使用该命令，该命令会删除本地文件 + 从暂存区移除，且该命令使用前提为：**如果文件本地有修改（和暂存区不一样），直接 `git rm` 会报错，不让删。**但是该命令并不会直接删除本地仓库中的文件，只有当开发者再次使用 `git commite` 提交操作时，文件会从本地仓库中删除。

`git rm -f` ，该命令是 `git rm <fileName>` 的强制版，该命令不会有使用前提（慎用）。

`git rm --cached` ，该命令只会将文件从暂存区移除，本地文件是不用有任何改动。

#### 查看提交历史

Git 记录开发者每一次提交的记录。当开发者从远程仓库克隆项目后，该项目会记录每一位提交人的信息以及相关操作。

开发者如果想要查看这些信息可以使用 `git log` 查看日志信息。`git log` 会按照提交日期的新旧从高到低罗列出来，具体信息如下：

```bash
git log

commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700
    changed the version number
commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700
    removed unnecessary test
commit a11bef06a3f659402fe7563abf99ad00de2209e6
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 10:31:28 2008 -0700
```

从以上信息开发者可以看到：
`commit ca82a6dff817ec66f44342007202690a93763949` ——SHA-1校验和；

`Author: Scott Chacon <schacon@gee-mail.com>` ——提交人的 Git 所配置的 username 和 mail；

`Data：Mon Mar 17 21:52:11 2008 -0700` ——提交日期；

`changed the version number` ——提交说明。

#### 查看提交历史文件的差异变化

`git log -p` / `git log -patch` ，该命令是最有用的命令，开发者可以查看提交历史中文件的差异变化，假设开发者想要查看最近两次提交的记录，可以命令末尾加 `-2`，具体信息如下：

```bash
git log -p -2

commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700
    changed the version number
diff --git a/Rakefile b/Rakefile
index a874b73..8f94139 100644--- a/Rakefile
+++ b/Rakefile
@@ -5,7 +5,7 @@ require 'rake/gempackagetask'
 spec = Gem::Specification.new do |s|
     s.platform  =   Gem::Platform::RUBY
     s.name      =   "simplegit"-    s.version   =   "0.1.0"
+    s.version   =   "0.1.1"
     s.author    =   "Scott Chacon"
     s.email     =   "schacon@gee-mail.com"
     s.summary   =   "A simple gem for using Git in Ruby code."
```

分段拆解：

```bash
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700
    changed the version number
```

以上信息不在讲述，其信息内容和 `git log` 的信息一样。

```bash
diff --git a/Rakefile b/Rakefile
index a874b73..8f94139 100644
--- a/Rakefile
+++ b/Rakefile
@@ -5,7 +5,7 @@ require 'rake/gempackagetask'
 spec = Gem::Specification.new do |s|
     s.platform  =   Gem::Platform::RUBY
     s.name      =   "simplegit"-    s.version   =   "0.1.0"
+    s.version   =   "0.1.1"
     s.author    =   "Scott Chacon"
     s.email     =   "schacon@gee-mail.com"
     s.summary   =   "A simple gem for using Git in Ruby code."
```

`diff --git a/Rakefile b/Rakefile` ——文件 `Rakefile` 的 diff 信息（差异块信息），前缀 a 表示旧文件，后缀 b 表示新文件。第一次提交旧文件不存在会出现 `/dev/null` 的提示

`index a874b73..8f94139 100644` ——从 `index` 分割，只看后面的信息 。`a874b73` ——旧文件内容的 blob 哈希（开发者可以理解为根据文件内容生成的哈希字符串）；`..` ——分隔符；`8f94139` ——新文件内容 blob 哈希。

`--- a/Rakefile +++ b/Rakefile`，`--- a/Rakefile` ——旧文件，`+++ b/Rakefile` 新增的文件。

`@@ -5,7 +5,7 @@ require 'rake/gempackagetask'` ，这一行只需要看 `@@ -5,7 +5,7 @@` ——表示修改代码的行数，如果是 ``@@ -5,7 +5,5 @@` ——表示代码行数少了两行；`require 'rake/gempackagetask'` ——字面意思，看不懂查英文意思（不重要）。

最后的部分就是修改代码的信息与痕迹了。

`git log` 还有一些其他的命令，如下：

| 命令            | 说明                                         |
| --------------- | -------------------------------------------- |
| `-p`            | 按补丁格式显示每个提交引入的差异。           |
| `--stat`        | 显示每次提交的文件修改统计信息。             |
| `--shortstat`   | 只显示 --stat 中最后的行数修改添加移除统计。 |
| `--name-only`   | 仅在提交信息后显示已修改的文件清单。         |
| `--name-status` | 显示新增、修改、删除的文件清单。             |
| `--graph `      | 在日志旁以 ASCII 图形显示分支与合并历史。    |

#### 小修改

`git commit --amend` ，把当前暂存区变更合并到上一次提交。

开发者知道，Git 会存储每一次的提交记录，每一个记录都会有自己的哈希值，`git commit --amend` 也不例外，`git commit --amend` 会销毁上次提交的记录的信息，重新生成一次新的提交。

假设开发者现在有一个名为 `test.md` 的文件忘记提交且上次提交的记录注释有错误，那么开发者可以如下操作：

```bash
git commit --amend -m '修改后的注释'
git add test.md
git commit --amend
```

#### 取消文件的暂存

`git reset HEAD <file>` ，取消文件的暂存，开发者知道 `git add <file>` 会将文件添加至暂存区并跟踪文件。如果开发者不希望文件暂存或者跟踪文件，开发者可以使用 `get reset HEAD <file>`。但是官方文档不建议开发者使用 `git reset` 命令（他很危险，但是危险的同时也有有趣，不是吗？），关于 `git reset` 的更多了解后续讲述。（至此对于 `git reset` 命令的使用就这么多，开发者不要盲目在生产环境中使用 `get reset` 的其他命令）

#### 撤销对文件的修改

`git checkout -- <file>`，将目标文件撤销至 Git 最近一次保存的记录信息。

`git checkout` 本质上也是一个很危险的操作，仅限于开发者的安全意识不足的情况下，因为 `git checkout` 会直接覆盖开发者本地的文件（如果开发者不习惯将文件记录在 Git 中，那么开发者所修改的文件内容会直接丢失），Git 官方建议开发者要习惯性的提交文件至 Git 中，Git 可以随时随地的回复开发者的提交内容（包括 `--amend` 所提交覆盖的信息和已被删除的分支的信息）。

#### 查看远程仓库

如果开发者想要对远程仓库进行一些操作，`git remote` 会帮助开发者解决这些问题。

`git remote -v` 查看所有的远程仓库。

假设开发者想要查看 Git 中有哪些远程仓库，如下：

```bash
git remote -v
bakkdoor  https://github.com/bakkdoor/grit (fetch)
bakkdoor  https://github.com/bakkdoor/grit (push)
cho45     https://github.com/cho45/grit (fetch)
cho45     https://github.com/cho45/grit (push)
defunkt   https://github.com/defunkt/grit (fetch)
defunkt   https://github.com/defunkt/grit (push)
koke      git://github.com/koke/grit.git (fetch)
koke      git://github.com/koke/grit.git (push)
origin    git@github.com:mojombo/grit.git (fetch)
origin    git@github.com:mojombo/grit.git (push)
```

开发者执行 `git remote -v` 会看到同一远程名称对应两条地址，这是 Git 的原生设计，将拉取（fetch）与推送（push）拆分为两套独立配置，以此支持读写分离的灵活部署方案。

团队协作场景下，常搭建内网源码镜像作为 fetch 拉取地址，所有成员从镜像拉取代码，能够提升下载速度、降低主仓库服务器压力；源码镜像拥有完整代码，无法限制成员读取源码。若要管控代码读取权限，需在代码仓库平台配置私有仓库与账号权限。

若未单独配置推送地址，push 会自动复用 fetch 地址，二者链接保持一致。

如：`bakkdoor、cho45` 这些时仓库的名称。在后续拉取和推送操作中，开发者可以直接使用名称代替地址。

#### 添加远程仓库

`git remote add <shortname> <url>` ，该命令会将指定的 `<url>` 添加到 Git 的配置中，并为该 `<url>` 起一个名称 `<shortname>`。后续开发者想要使用该 `<url>` 可以直接使用其 `<shortname>` 代替。

`git remote rename <shortname> <url>` ，该命令会对已经添加的远程仓库重新命名。

#### 从远程仓库拉取

`git fetch <remote>` ，该命令会从指定的远程仓库将数据**下载**到开发者的本地仓库。

`git pull <remote>` ，假设开发者现有的分支设置某个指定的远程仓库，开发者使用 `git pull <remote>` 命令后会将数据**下载并合并**到开发者本地仓库中。

`git clone <remote>`，该命令会直接将目标远程仓库添加到 Git 中，并将拉取的数据直接设置为 `master` 分支。

#### 推送到远程仓库

`git push <remote> <branch>` ，该命令会将开发者的本地数据推送到远程仓库的指定分支中（确保目标分支存在）。

#### 查看远程仓库

`git remote show <remote>` ，该命令会展现出远程仓库的 URL 与跟踪分支的信息。如下：

```bash
$ git remote show origin
* remote origin
  URL: https://github.com/my-org/complex-project
  Fetch URL: https://github.com/my-org/complex-project
  Push  URL: https://github.com/my-org/complex-project
  HEAD branch: master
  Remote branches:
    master                           tracked
    dev-branch                       tracked
    markdown-strip                   tracked
    issue-43                         new (next fetch will store in remotes/origin)
    issue-45                         new (next fetch will store in remotes/origin)
    refs/remotes/origin/issue-11     stale (use 'git remote prune' to remove)
  Local branches configured for 'git pull':
    dev-branch merges with remote dev-branch
    master     merges with remote master
  Local refs configured for 'git push':
    dev-branch                     pushes to dev-branch (up to date)
    markdown-strip                 pushes to markdown-strip (up to date)
    master                         pushes to master (up to date)
```

远端仓库基本信息：

```bash
URL: https://github.com/my-org/complex-project
Fetch URL: https://github.com/my-org/complex-project
Push  URL: https://github.com/my-org/complex-project
```

`HEAD branch: master` —— 默认远端的主分支。

远端所有分支状态（核心）：

```bash
Remote branches:
  master                           tracked
  dev-branch                       tracked
  markdown-strip                   tracked
  issue-43                         new (next fetch will store inremotes/origin)
  issue-45                         new (next fetch will store inremotes/origin)
  refs/remotes/origin/issue-11     stale (use 'git remote prune' to remove)
```

`tracked` 表示本地已经记录过当前分支的信息。

`new` 表示该分支本地未记录，可能是远程仓库新增分支。

`stale` 表示该分支已经删除，不再使用。

本地仓库中分支与远程仓库分支的对应关系：

```bash
Local refs configured for 'git push':
    dev-branch                     pushes to dev-branch (up to date)
    markdown-strip                 pushes to markdown-strip (up to date)
    master                         pushes to master (up to date)
```

`dev-branch` 表示本地分支，`pushes to dev-branch` 表示 `git push` 会将本地分支提交到对应远程仓库分支。

#### 删除远程仓库

`git remote remove/rm <remote>` 删除已有远程仓库，所有和这个远程仓库相关的远程跟踪分支以及配置信息也会一起被删除。

#### 列出打包标签

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

