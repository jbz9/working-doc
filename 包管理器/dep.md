# Dep包管理器

官网：https://golang.github.io/dep/docs/introduction.html

Dep（Golang dependency management tool）是一种 Go 语言的依赖管理工具，用于管理项目的依赖项。Dep 以 Gopkg.toml 和 Gopkg.lock 文件的形式管理依赖项，可以实现快速、简单和可靠的依赖项管理。

Dep 的工作方式很简单。它会从 Gopkg.toml 文件中读取依赖项列表，并使用 Gopkg.lock 文件确定每个依赖项的版本。Gopkg.lock 文件包含依赖项的确切版本和依赖项的依赖项版本。这样就可以确保项目中的每个依赖项都使用正确的版本，从而提高了项目的稳定性和可靠性。

Dep和go mod

Dep 是在 Go modules 出现之前被广泛使用的包管理器，它的主要特点是快速、简单易用，支持 vendor 目录。Dep 在管理依赖时，使用 Gopkg.lock 文件来记录精确的依赖版本，以确保团队中所有人使用的依赖版本都是一致的。

go官方已经宣布弃用dep包管理器[。从Go 1.16版本开始，官方建议使用Go Modules进行依赖管理，而不是使用dep。因此，开发者应该尽可能地迁移到Go Modules，以获得更好的支持和最新的功能。不过，已经使用dep的项目仍然可以继续使用它，只是不会再有官方支持。

## 特征文件

### Gopkg.toml

用于列出当前项目的依赖项及其版本。在这个文件中，你可以列出每个依赖项的版本要求和存储库信息。

```

# 必需包
required = ["github.com/astaxie/beego"]
# 忽略包
ignored = ["golang.org/x/crypto"]
# 项目元数据
[metadata]
homepage = "https://github.com/qiangmzsx"
license = "MIT"
owners_name_1 = "qiangmzsx"
owners_email_1 = "qiangmzsx@hotmail.com"
owners_homepage_1 = "https://github.com/qiangmzsx"

# 约束条件
[[constraint]]
  name = "github.com/astaxie/beego"
  # 可选：版本
  version = "=1.8.0"
  # 分支
  #branch = "master"
  # 修订
  #revision = "beego 1.8.0"
  # 可选：指定来源
  source = "github.com/astaxie/beego"
```





###  Gopkg.lock 

用于锁定所有依赖项及其版本。在这个文件中，看到每个依赖项的确切版本。`Gopkg.lock` 是由 Dep 自动生成的文件，包含所有依赖项的版本和元数据。这个文件的作用是确保每个依赖项的版本都是可重复的，并且所有的开发者都在使用相同的依赖项版本。通过这种方式，可以避免依赖项之间的版本冲突，从而确保项目的稳定性。

## 命令解

dep提供了一个命令可以列出当前项目的所有依赖项，包括直接和间接依赖项。可以使用以下命令：

```
dep status
```

结果：

```
PROJECT                     CONSTRAINT  VERSION   REVISION  LATEST    PKGS USED
github.com/BurntSushi/toml  branch      (none)    7d964bc   v0.3.1    1
github.com/go-sql-driver/mysql
                            branch      (none)    b14aa68   v1.4      1
github.com/gorilla/mux     branch      (none)    620c412   v1.6.2    1
github.com/lib/pq          branch      (none)    6d7dc50   v1.0.0    1
github.com/satori/go.uuid  branch      (none)    f80cee0   v1.2.0    2
```

## 范围版本

## 代理

## 

