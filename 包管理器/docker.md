## Dockerfile

Dockerfile是用于构建Docker镜像的文本文件，它包含了一系列构建Docker镜像所需的指令和参数。

在Dockerfile中，可以指定要基于哪个Docker镜像构建镜像，以及需要添加哪些文件、安装哪些软件包、运行哪些命令等等。Dockerfile使用一系列指令，例如FROM、RUN、COPY、CMD、ENTRYPOINT等来描述镜像的构建过程。

例如：

```
# 指定要基于的镜像
FROM ubuntu:latest

# 安装Python和依赖项
RUN apt-get update && apt-get install -y python3 python3-pip
RUN pip3 install flask

# 复制应用程序文件
COPY app.py /app/

# 指定工作目录
WORKDIR /app

# 暴露端口
EXPOSE 5000

# 指定容器启动命令
CMD ["python3", "app.py"]
```

组件引入

Docker Hub上的镜像页面上列出的Packages是该镜像中预安装的组件列表，通常由镜像作者在Dockerfile中进行配置。

1、使用Linux软件包管理器

Dockerfile中可以使用Linux软件包管理器，例如apt-get、yum、apk等，来安装需要的组件，通过 `RUN` 命令手动下载和安装第三方组件

```
RUN apt-get update && apt-get install -y nodejs

```

2、使用Python包管理器

```
RUN pip install flask
```

3、From 基础镜像

使用第三方组件的官方 Docker 镜像作为基础镜像，所有 Dockerfile 都必须以 `FROM` 命令开始。`FROM` 命令会指定镜像基于哪个基础镜像创建，接下来的命令也会基于这个基础镜像。`FROM` 命令可以多次使用，表示会创建多个镜像。

```
# 基于 nginx:1.21.0 镜像构建
FROM nginx:1.21.0
```

注：

如果当前镜像from了某个镜像，那么它也会继承了基础镜像的组件和漏洞。

如nginx:1.21.0镜像，是基于 debian:bullseye-slim 镜像，其中debian:bullseye-slim的组件和漏洞，在nginx:1.21.0也存在。

#### 私服

Docker私服是存放镜像的本地仓库，类似于docker hub。私服是本地的仓库，用于保存公司内部上传的Docker镜像。配置了私有仓库服务器之后，拉取镜像就会先去私服中拉取，找不到则去中央仓库拉

**harbor**

#### Docker Hub

Docker Hub是Docker的官方镜像仓库，提供了一个中央化的平台，供用户存储、分享、管理Docker镜像。

地址：https://hub.docker.com/

Docker Hub上镜像页面上列出的组件和漏洞信息是由一个名为"Anchore Engine"的工具提供的。 Anchore Engine是一个开源的容器安全扫描器，可以对Docker镜像进行扫描，检测镜像中使用的操作系统、库、语言等组件是否存在漏洞。通过Docker Hub与Anchore Engine的集成，用户可以在镜像页面上查看该镜像的组件列表和漏洞信息。在镜像页面上，用户可以看到该镜像所使用的各个层（Layer）的组件和漏洞信息，以及Anchore Engine对该镜像的总体评分和安全建议。

例如：

https://hub.docker.com/layers/library/nginx/1.21.0/images/sha256-2f1cd90e00fe2c991e18272bb35d6a8258eeb27785d121aa4cc1ae4235167cfd?context=explore

#### Anchore Engine

官网 ：https://anchore.com/

源码（python）：https://github.com/anchore/anchore-engine

Anchore Engine 是一个功能强大的容器安全工具，可以分析 Docker 镜像的内容，检测其中存在的安全漏洞和配置问题，并为 Docker 镜像提供安全评估报告。Anchore Engine 使用了一系列开源组件来实现其功能，其中包括Docker Registry v2, PostgreSQL数据库以及各种开源漏洞扫描引擎（例如Clair, Trivy等）。

Anchore Engine可以分析dockerfile文件，根据其内容来构建镜像并进行安全扫描。在扫描过程中，Anchore Engine将检测镜像中的各种组件以及它们的版本号，并与已知的漏洞数据库进行比对，以查找其中存在的安全漏洞。因此，如果dockerfile中包含所有用于构建镜像的必需组件信息，Anchore Engine将能够从中获取镜像组件的信息。

