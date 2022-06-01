# Docker

## 基础

### 安装

（1）查看Linux内核版本，最低3.10 ；如果低于，后面将安装不成功

执行：uname -r

![image-20191126191731579](C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126191731579.png)

（2）安装依赖: docker依赖于系统的一些必要的工具，可以提前安装

``` shell
#如果安装了，则移除之前docker
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-selinux \
                  docker-engine-selinux \
                  docker-engine
                  
                  
安装依赖
yum install -y yum-utils device-mapper-persistent-data lvm2
```

（3）设置yum源:如何源过期了，更换地址

```shell
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

 yum makecache fast
```

（4）安装最新版本docker

``` shell
yum install docker-ce
```

（5）启动docker并查看版本

```shell
#启动
systemctl start docker  或者  service docker start
#查看版本
docker version
#开机自动启动
systemctl enable docker 
#安装时使用了root用户安装，普通用户没权权限使用docker命令；将普通用户加入docker组即可
#创建 docker 用户组
groupadd docker
#加入组
usermod -aG docker 用户名

安装一个mysql
docker pull mysql:5.7
docker run -p 3306:3306 --name mysql -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7

或
docker run --ulimit nofile=65100:65100 --restart unless-stopped --name sugar-mysql -p 3306:3306 -v ~/mysqlData:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=12345 -d mysql:5.7 --max_allowed_packet=100M --max_connections=1000

docker run -it --link sugar-mysql:mysql --rm mysql:5.7 sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
```

（5）查看

<img src="C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126193326253.png" alt="image-20191126193326253" style="zoom:50%;" />

客户端和服务端都已经启动。

（6）设置阿里云加速镜像

```shell
执行：vim /etc/docker/daemon.json
加入：
{
  "registry-mirrors": ["https://898il2ar.mirror.aliyuncs.com"]
}
#镜像加速地址
# 登录阿里云控制台 搜索 容器镜像服务
# 进入容器镜像服务， 左侧最下方容器镜像服务中复制加速器地址
#重启docker
systemctl daemon-reload  
systemctl restart docker
#查看docker状态
systemctl status docker

#拉取镜像 拉取镜像地址为：https://hub.docker.com/search?q=&type=image
docker pull nginx
#查看所有镜像
docker images
```

![image-20191126195944594](C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126195944594.png)

### 简介

**Docker 由镜像(Image)、容器(Container)、仓库(Repository) 三部分组成。**Docker 把应用程序及其依赖，打包成image，image 是二进制文件,然后通过镜像，去能启动容器，image 文件可以看作是容器的模板。Docker 根据 image 文件生成容器的实例。同一个 image 文件，可以生成多个同时运行的容器实例。image—1:n—容器

优点：

* 轻量级，容器运行占用内存少
* 启动快速
* 容器隔离

组件：

* docker client 客户端 ：向docker服务器进程发起请求，如创建、停止、销毁容器等
* docker service 服务端：处理docker请求，管理容器
* docker registry 镜像仓库

#### 镜像

镜像是分层的、可复用的

#### 容器

从根本上说，容器是一个进程，一个Docker容器包含了所有的某个应用运行所需要的环境，容器是根据镜像来创建，Docker 容器可以运行、开始、停止、移动和删除。每一个 Docker 容器都是独立和安全的应用平台，Docker 容器是 Docker 的运行部分。

* 可移植性：程序可直接迁移

##### 使用 Volume

除了数据库，您还可以使用 Volume 保存您在容器中产生或使用的文件。一个 Volume 就是在一个或者多个容器里有特殊用途的目录。它绕过了容器内部的文件系统，为持久化数据、共享数据提供了下面这些有用的特性：

- 容器可以通过把数据写在 Volume 上来实现数据持久化
- Volume 可以在不同的容器之间共享和重用数据
- 容器数据的备份、恢复和迁移都可以通过 Volume 实现
- 通过 Volume 实现多容器共享数据，从而实现应用的横向扩展

您可以在写 Dockerfile 时，把需要持久化、或者频繁更改的文件保存在 Volume 中；在程序运行时，可以通过 Volume 控制器直接修改这些文件（如替换博客主题、或修改程序的 config 文件等），避免重新构建和发布，非常方便。



#### 常见命令

```shell
#查看
docker info

#查找镜像
docker search redis

#拉取镜像文件到本地
docker pull <镜像名:tag> 如：
docker pull redis:5.0  或 docker pull redis 不写标签，默认下载最新

#查看所有镜像文件
# image是二进制文件，docker把应用程序及依赖打包在这个文件中，然后通过这个image文件生成多个容器实例
docker image ls   或者 docker images

#删除镜像文件
docker image rm <image ID> 或者 docker image rm <imageName> 
docker image rm dcf9ec9265e0  如果删除不了，可能是容器的原因，先删容器

删除所有镜像
docker rmi -f $(docker images -aq)

通过镜像image生成的容器实例，也是一个文件，称为容器文件，docker kill 只是停止容器，没有删除容器文件
#通过镜像文件，启动一个容器实例
docker run <image id 或者 镜像名称(repository)>  例如： 
docker run -d -p 9090:80 --restart=always -v ~/docker-demo/nginx-htmls:/usr/share/nginx/html/   231d40e811cd
-d 后台运行 -p端口映射 绑定容器端口80到本机端9090
docker run -d -P nginx 
-P 容器内部端口随机映射到主机的高端口。本机端口随机
--restart=always 重启docker时，也都会启动 231d40e811cd 容器
-v ~/docker-demo/nginx-htmls:/usr/share/nginx/html/    在当前目录下生成 docker-demo/nginx-htmls文件目录，对应的是容器中的/usr/share/nginx/html/目录

# 查看正在运行的容器
docker ps 
#查看所有容器
docker ps  -a
#通过一个镜像创建一个容器吗，但是不启动
docker create --name 容器名称 镜像ID
# 启动 容器
docker start <names容器名 或者 id>
docker start 99c58e89d9b5
#进入容器内部 更改文件
docker exec -it 容器ID bash
参数 -i 表示这是一个交互容器，会把当前标准输入重定向到容器的标准输入中，而不是终止程序运行，-t 指为这个容器分配一个终端。 

#查看 容器日志
docker logs -f  [container ID] 
# 停止容器
docker stop <names容器名 或者 id>
docker stop 99c58e89d9b5
#杀死 镜像
docker kill 99c58e89d9b5
#删除容器
docker rm <容器名 or ID> 
#删除所有停止的容器
docker rm $(docker ps -a -q)
#导出容器 成为一个文件
docker export 容器ID > 文件名
例如
docker export 166c6133cc7c > nginx.tar
#导入一个文件成为镜像 ，到文件当前目录下执行
cat 导入的文件名称 |  docker import - 镜像名称:标签
例如
cat nginx.tar | docker import - dockertest:1.0
#查看容器内部详情
docker inspect 容器ID
#修改容器后将容器打成一个新镜像
docker commit 容器ID 镜像名称:标签

#复制容器内文件到宿主机
docker cp 容器id:文件路径 复制到的宿主机路径
```

```shell
#构建镜像 需要有Dockerfile文件  -t 指定要创建的目标镜像名
# . 代表上下文路径；此路径下不要放无用的文件，因为会一起打包发送给 docker引擎
# ./do/eip-admin-starter-rest-2.0.jar 复制 上下文（context） 目录下的文件；源文件路径不能使用绝对路径，需要使用./  相对路径
docker build -t 镜像名称:标签 Dockerfile文件目录
例如 docker build -t runoob/centos:6.7 .
例如：Dockerfile 构建jar成为镜像
FROM java:8
COPY ./do/eip-admin-starter-rest-2.0.jar eip-admin-starter-rest-2.0.jar
```



![image-20191126205443232](C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126205443232.png)

### Dockerfile构建镜像

用它来制作镜像，Dockerfile文件是用来制作image文件，Docker根据Dockerfile生成二进制的image文件。

Dockerfile中包含了from、maintainer(维护者)、run、cmd、expose、env、add、copy、entryport(入口点)、volume（体积）、user、workdir、onbuild等命令

![image-20191226095616112](typora-user-images/image-20191226095616112.png)

1. FROM
   Dockerfile中第一条指令必须是FROM指令，且在同一个Dockerfile中创建多个镜像时，可以使用多个FROM指令。
   格式：FROM image 或者 FROM image:tag

2. MAINTAINER

   维护者信息

   格式：MAINTAINER user_name  user_email

3. RUN

   运行命令

   格式：RUN command   在shell终端运行命令；或者 RUN  ["executable","",""] 使用exec执行，可以指定其它终端使用

   例如：RUN ["/bin/bash","-c","echo hello"]   

4. CMD

   支持三种格式：
   CMD ["executable","param1","param2"]，使用exec执行，这是推荐的方式。
   CMD command param1 param2 在/bin/sh中执行。
   CMD ["param1","param2"] 提供给ENTERYPOINT的默认参数。
   CMD用于指定容器启动时执行的命令，每个Dockerfile只能有一个CMD命令，多个CMD命令只执行最后一个。若容器启动时指定了运行的命令，则会覆盖掉CMD中指定的命令。

5. EXPOSE

   用来暴露端口，供外部使用

   格式：EXPOSE port

6. ENV

   格式为：EVN key value 。用于指定环境变量，这些环境变量，后续可以被RUN指令使用，容器运行起来之后，也可以在容器中获取这些环境变量。
   例如
   ENV word hello
   RUN echo $word

7. ADD

   格式：ADD src dest
   
   复制本地目录的文件到docker容器的dest中，src可以是一个URL或者tar文件，tar文件会自动解压为目录。

8. COPY

   格式：COPY src desc 

   复制本地主机src目录或者文件到容器的desc目录，desc不存在时会自动创建。从上下文目录（本地）中复制文件或者目录到容器里指定路径。容器内的指定路径，该路径不用事先建好，路径不存在的话，会自动创建。

   使用 COPY 指令，源文件的各种元数据都会保留。比如读、写、执行权限、文件变更时间等。

9. ENTRYPORT

   enrtyport用于配置容器启动后执行的命令，这些命令不能被docker run提供的参数覆盖。和CMD一样，每个Dockerfile中只能有一个ENTRYPOINT，当有多个时最后一个生效。

   格式有两种：
   ENTRYPOINT ["executable","param1","param2"]
   ENTRYPOINT command param1,param2 会在shell中执行。

10. VOLUME

    volume体积，作用是创建在本地主机或其他容器可以挂载的数据卷，用来存放数据。

    格式为 VOLUME  ["/data"]

11. USER

    指定容器运行的用户名

    格式：USER username

12. WORKDIR

    指定容器的一个目录， 容器启动时执行的命令会在该目录下执行。为后续的run 命令指定执行目录，可以使用多个WORKDIR指令，若后续指令用得是相对路径，则会基于之前的命令指定路径。

    格式：WORKDIR  /path

    ```shell
    #test
    FROM ubuntu
    MAINTAINER hello
    RUN mkdir /mydir
    RUN echo hello world > /mydir/test.txt
    WORKDIR /mydir
    CMD ["more" ,"test.txt"]
    ```

13. ONBUILD

    onbuild，指定当所创建的镜像作为其他新建镜像的基础镜像时所执行的指令。

14. docker build

    通过docker build命令来创建镜像，该命令首先会上传Dockerfile文件给Docker服务器端，服务器端将逐行执行Dockerfile中定义的指令。
    通常建议放置Dockerfile的目录为空目录。另外可以在目录下创建.dockerignore文件，让Docker忽略路径下的文件和目录，这一点与Git中的配置很相似。

    通过 -t 指定镜像的标签信息，例如：docker build -t regenzm/first_image . 

    "."指定的是Dockerfile所在的路径

编写Dockerfile

```
-f标志with docker build指向文件系统中任何位置的Dockerfile
docker build -f /path/to/a/Dockerfile .
```



### docker使用

#### 部署nginx

```shell
#拉取nginx
docker pull nginx
#创建容器
docker create --name 容器名称 镜像ID
#从容器拷贝文件到 当前目录
docker cp nginx:/etc/nginx/nginx.conf  nginx.conf
#拷贝nginx.conf 文件到 nginx 容器
docker cp nginx.conf  nginx:/etc/nginx/nginx.conf
#重启容器
docker restart nginx
```

#### 安装rocketmq

```
nohup ./mqnamesrv &
nohup ./mqbroker -n 192.168.52.190:9876  -c ../conf/broker.conf &

brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1 = 192.168.52.190
namesrvAddr = 192.168.52.190:9876
autoCreateTopicEnable = true

docker安装

docker run -d -p 9876:9876 -v /home/open/kafka/rocketmq-docker/data/namesrv/logs:/root/logs -v /home/open/kafka/rocketmq-docker/data/namesrv/store:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq:4.4.0 sh mqnamesrv

docker run -d -p 10911:10911 -p 10909:10909 -v  /home/open/kafka/rocketmq-docker/data/broker/logs:/root/logs -v  /home/open/kafka/rocketmq-docker/data/broker/store:/root/store -v  /home/open/kafka/rocketmq-docker/conf/broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf --name rmqbroker --link rmqnamesrv:namesrv -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq:4.4.0 sh mqbroker -c /opt/rocketmq-4.4.0/conf/broker.conf

docker run -d --name rmqconsole -p 9800:8080 --link rmqnamesrv:namesrv -e "JAVA_OPTS=-Drocketmq.namesrv.addr=namesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -t styletang/rocketmq-console-ng

docker start rmqnamesrv
docker start rmqbroker
docker start rmqbroker
http://192.168.52.190:9800


```

