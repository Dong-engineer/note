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


