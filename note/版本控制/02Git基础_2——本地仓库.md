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