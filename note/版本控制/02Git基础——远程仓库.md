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
