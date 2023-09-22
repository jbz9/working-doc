# Git学习

参考：

https://ideepspace.gitbooks.io/git/content/fen-zhi-guan-li/fen-zhi-guan-li-ce-lve/gong-neng-fen-zhi-feature.html

## 1. 分支

### 1.1 Master-主分支

有且只有一个，所是提供给用户使用的正式版本都是从master上发布的。发布使用tag做版本标记。

- 拉取源：无需。
- 合并目标：无需。
- 修改：不允许。
- 生命期：持续

### 1.2 Develop-开发分支

Matser分支是稳定的，只用来分布版本，也不需要进行push，只能提PR进行Merge。日常开发工作应全部在dev开发分支。

同时每个人都在的自己的分支进行开发，代码完成之后将提PR,然后团队其它人进行代码审核Review,没有问题的话，就将代码合并到开发分支。

- 拉取源：master。
- 合并目标：无需。
- 修改：不允许。
- 生命期：持续。

### 1.3 feature-功能分支

是为了开发某个功能，从Develop分支上checkout出来的，功能开发完成后，再merge到Develop分支。

- 拉取源：develop。
- 合并目标：develop。
- 修改：允许。
- 生命期：合并后删除。

### 1.4 release-预发布分支

从开发分支Develop分支上checkout出来，然后进行QA测试，没有问题之后，Merge进入到master & develop分支，可以使用release-*的命名方式

- 拉取源：develop。
- 合并目标：master & develop。
- 修改：允许。
- 生命期：合并后删除。

### 1.5 bug-bug分支

bug修复分支（`hotfix`），用于修复线上问题。从`master`拉取，修复并测试完成`merge`回`master`和`develop`。如果修复期间，有其他版本合并入`master` ，需要同步到`hotfix`版本，并进行测试。它的命名，可以采用`fixbug-*`的形式。

- 拉取源：master 。
- 合并目标：master，develop 。
- 修改：允许。
- 生命期：合并后删除。

**线上bug修复流程**

当用户反馈系统有bug时，为了处理bug，需要从master中创建出保养分支；等到bug修复完成，需要合并回master。

1. 创建hotfix分支
2. 修改bug
3. 完成修复，合并到master发布
4. 打标签
5. 合并到develop

## 2. 标签

发布一个版本时，我们通常先在版本库中打一个标签（tag），这样就唯一确定了打标签时刻的版本。将来无论什么时候，取某个标签的版本，就是把那个打标签的时刻的历史版本取出来。

所以，标签也是版本库的一个快照。

Git 的标签虽然是版本库的快照，但其实它就是指向某个 commit 的指针（跟分支很像对不对？但是分支可以移动，标签不能移动），所以，创建和删除标签都是瞬间完成的。

## 3. 区域

- Workspace：工作区
- Index / Stage：暂存区
- Repository：仓库区（或本地仓库）
- Remote：远程仓库

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1651329802956bg2015120901.png" style="zoom:50%;" />



## 4. 常用命令

### **初始化**

```shell
# 在当前目录新建一个Git代码库
$ git init

# 新建一个目录，将其初始化为Git代码库
$ git init [project-name]

# 下载一个项目和它的整个代码历史
$ git clone [url]
```

### **配置**

Git的设置文件为.gitconfig，它可以在用户主目录下（全局配置），也可以在项目目录下（项目配置）。

```shell
# 显示当前的Git配置
$ git config --list

# 编辑Git配置文件
$ git config -e [--global]

# 设置提交代码时的用户信息
$ git config [--global] user.name "[name]"
$ git config [--global] user.email "[email address]"
```

### **增加/删除**

```shell
# 添加指定文件到暂存区
$ git add [file1] [file2] ...

# 添加指定目录到暂存区，包括子目录
$ git add [dir]

# 添加当前目录的所有文件到暂存区
$ git add .

# 添加每个变化前，都会要求确认
# 对于同一个文件的多处变化，可以实现分次提交
$ git add -p

# 删除工作区文件，并且将这次删除放入暂存区
$ git rm [file1] [file2] ...

# 停止追踪指定文件，但该文件会保留在工作区
$ git rm --cached [file]

# 改名文件，并且将这个改名放入暂存区
$ git mv [file-original] [file-renamed]
```

### **提交**

```shell
# 提交暂存区到仓库区
$ git commit -m [message]

# 提交暂存区的指定文件到仓库区
$ git commit [file1] [file2] ... -m [message]

# 提交工作区自上次commit之后的变化，直接到仓库区
$ git commit -a

# 提交时显示所有diff信息
$ git commit -v

# 使用一次新的commit，替代上一次提交
# 如果代码没有任何新变化，则用来改写上一次commit的提交信息
$ git commit --amend -m [message]

# 重做上一次commit，并包括指定文件的新变化
$ git commit --amend [file1] [file2] ...
```

### **分支**

```shell
# 列出所有本地分支
$ git branch

# 列出所有远程分支
$ git branch -r

# 列出所有本地分支和远程分支
$ git branch -a

# 新建一个分支，但依然停留在当前分支
$ git branch [branch-name]

# 新建一个分支，并切换到该分支
$ git checkout -b [branch]

# 新建一个分支，指向指定commit
$ git branch [branch] [commit]

# 新建一个分支，与指定的远程分支建立追踪关系
$ git branch --track [branch] [remote-branch]

# 切换到指定分支，并更新工作区
$ git checkout [branch-name]

# 切换到上一个分支
$ git checkout -

# 建立追踪关系，在现有分支与指定的远程分支之间
$ git branch --set-upstream [branch] [remote-branch]

# 合并指定分支到当前分支
$ git merge [branch]

# 选择一个commit，合并进当前分支
$ git cherry-pick [commit]

# 删除分支
$ git branch -d [branch-name]

# 删除远程分支
$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]
```

### **标签**

```shell
# 列出所有tag
$ git tag

# 新建一个tag在当前commit
$ git tag [tag]

# 新建一个tag在指定commit
$ git tag [tag] [commit]

# 删除本地tag
$ git tag -d [tag]

# 删除远程tag
$ git push origin :refs/tags/[tagName]

# 查看tag信息
$ git show [tag]

# 提交指定tag
$ git push [remote] [tag]

# 提交所有tag
$ git push [remote] --tags

# 新建一个分支，指向某个tag
$ git checkout -b [branch] [tag]
```

### **查看信息**

```shell
# 显示有变更的文件
$ git status

# 显示当前分支的版本历史
$ git log

# 显示commit历史，以及每次commit发生变更的文件
$ git log --stat

# 搜索提交历史，根据关键词
$ git log -S [keyword]

# 显示某个commit之后的所有变动，每个commit占据一行
$ git log [tag] HEAD --pretty=format:%s

# 显示某个commit之后的所有变动，其"提交说明"必须符合搜索条件
$ git log [tag] HEAD --grep feature

# 显示某个文件的版本历史，包括文件改名
$ git log --follow [file]
$ git whatchanged [file]

# 显示指定文件相关的每一次diff
$ git log -p [file]

# 显示过去5次提交
$ git log -5 --pretty --oneline

# 显示所有提交过的用户，按提交次数排序
$ git shortlog -sn

# 显示指定文件是什么人在什么时间修改过
$ git blame [file]

# 显示暂存区和工作区的差异
$ git diff

# 显示暂存区和上一个commit的差异
$ git diff --cached [file]

# 显示工作区与当前分支最新commit之间的差异
$ git diff HEAD

# 显示两次提交之间的差异
$ git diff [first-branch]...[second-branch]

# 显示今天你写了多少行代码
$ git diff --shortstat "@{0 day ago}"

# 显示某次提交的元数据和内容变化
$ git show [commit]

# 显示某次提交发生变化的文件
$ git show --name-only [commit]

# 显示某次提交时，某个文件的内容
$ git show [commit]:[filename]

# 显示当前分支的最近几次提交
$ git reflog
```

### **pull/push**

```shell
# 下载远程仓库的所有变动
$ git fetch [remote]

# 显示所有远程仓库
$ git remote -v

# 显示某个远程仓库的信息
$ git remote show [remote]

# 增加一个新的远程仓库，并命名
$ git remote add [shortname] [url]

# 取回远程仓库的变化，并与本地分支合并
$ git pull [remote] [branch]

# 上传本地指定分支到远程仓库
$ git push [remote] [branch]

# 强行推送当前分支到远程仓库，即使有冲突
$ git push [remote] --force

# 推送所有分支到远程仓库
$ git push [remote] --all
```

### **rebase**

```shell

```



### **撤销**

```shell
# 恢复暂存区的指定文件到工作区，放弃本地修改的代码，和远程保持一致
$ git checkout --[file]
git checkout --PomParseAnalyzer.java


# 恢复某个commit的指定文件到暂存区和工作区
$ git checkout [commit] [file]

# 恢复暂存区的所有文件到工作区
$ git checkout .

# 执行完commit后，撤回commit，但是保留更改的代码
# 进行了2次commit，想都撤回，可以使用HEAD~2
git reset --soft HEAD~1


# 重置暂存区的指定文件，与上一次commit保持一致，但工作区不变
$ git reset [file]

# 重置暂存区与工作区，与上一次commit保持一致
$ git reset --hard

# 重置当前分支的指针为指定commit，同时重置暂存区，但工作区不变
$ git reset [commit]

# 重置当前分支的HEAD为指定commit，同时重置暂存区和工作区，与指定commit一致
$ git reset --hard [commit]

# 重置当前HEAD为指定commit，但保持暂存区和工作区不变
$ git reset --keep [commit]

# 新建一个commit，用来撤销指定commit
# 后者的所有变化都将被前者抵消，并且应用到当前分支
$ git revert [commit]

# 暂时将未提交的变化移除，稍后再移入
$ git stash
$ git stash pop

Git 撤销已经push到远端的代码
其实是没有直接让远端代码回复到某次的指令，实现撤销push的思路如下：
1.先让代码恢复到想要恢复的前一次提交记录 2.重新提交代码，覆盖端上的代码，就相当于撤销了push 的提交
实现方式如下：
1.首先使用git log找到要回退版本的commit版本号；
2.git reset --hard <版本号>，撤回到需要的版本; 注意在执行命令之前先把当前工作拷贝一份，不然--hard会将修改全部丢失
3.使用git push --force覆盖之前的提交
```

### **其他**

```
# 生成一个可供发布的压缩包
$ git archive
```

## 5.版本管理

### **Git 提交准则** 

除了源码相关的东西之外，其他build产生的东西（如：maven的target文件夹，.idea文件夹等），均不能提交进入源码仓库，添加到.gitignore文件中忽略掉。 

撰写规范的提交说明。一份好的提交说明可以帮助协作者更轻松更有效地配合工作。 

要严格按照我们指定的流程切换到指定分支，开发相应的功能。

### **Git fLow** 

在多组员，多项目等环境进行协同工作时，如果没有统一规范、统一流程，则会导致额外的工作量，甚至会做无用功。所以要减少版本冲突，减轻不必要的工作，就需要规范化的工作流程。Git Flow 是前人经过探索总结出来的一套Git分支管理规范和流程。

https://note.youdao.com/yws/public/resource/4747a8564e6f3899b9a5bbb4bbac26d8/xmlnote/24588FF261AD48AD8C1DF949F2484364/3818

### **版本号**

版本格式：主版本号.次版本号.修订号，版本号递增规则如下：

- 主版本号：当你做了不兼容的 API 修改
- 次版本号：当你做了向下兼容的功能性新增
- 修订号：当你做了向下兼容的问题修正。

先行版本号及版本编译信息可以加到“主版本号.次版本号.修订号”的后面，作为延伸。 

主版本号为零（0.y.z）的软件处于开发初始阶段，一切都可能随时被改变。 

标准的版本号必须采用 XYZ 的格式，其中 X、Y 和 Z 为非负的整数，且禁止在数字前方补零。X 是主版本号、Y 是次版本号、而 Z 为修订号。每个元素必须以数值来递增。例如：1.9.1 -> 1.10.0 -> 1.11.0。