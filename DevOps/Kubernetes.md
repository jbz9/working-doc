###                                                                                                                Kubernetes中的概念 

##### 简介

参考：

http://docs.kubernetes.org.cn/227.html

 https://jimmysong.io/kubernetes-handbook/concepts/ 

 https://www.bookstack.cn/read/Kubernetes-zh/135.md 

 https://blog.51cto.com/3241766/2405624 

1.  Kubernetes是什么

   简称k8s，它是一个容器集群管理系统，容器英文单词container，有包含的意思。 是一个开源的平台，可以实现容器集群的自动化部署、自动扩缩容、维护等功能。 

   容器技术： 起源于Linux开源平台， 简单地理解容器，容器中运行的就是一个或者多个应用程序，以及应用运行所需要的环境。容器直接运行在操作系统内核之上的用户空间。容器技术可以让多个独立的用户空间运行在同一台宿主机上。容器既可以运行在物理机也可以运行在虚拟机上，当然也可以运行在公有云主机上。
##### k8s组件

   k8s是由一系列组件构成。

1. **Master**组件

   它提供了集群的管理控制中心，Master组件可以在集群的任何节点上运行， 但是为了简单起见，通常在一台VM/机器上启动所有Master组件，并且不会在此VM/机器上运行用户容器。 

2. **kub-apiserver**

对外提供API，任何的资源请求/调用操作都是通过kube-apiserver提供的接口进行。提供了资源操作的唯一入口，并提供认证、授权、访问控制、API 注册和发现等机制。 

3. **ETCD**

k8s的默认存储系统，保存所有集群数据，使用时需要为etcd数据提供备份计划。

4. **kube-controller-manager**

运行管理控制器， 它们是集群中处理常规任务的后台线程。逻辑上，**每个控制器是一个单独的进程**，但为了降低复杂性，它们都被编译成单个二进制文件，并在单个进程中运行。 

控制器包含：

* 节点（note）控制器
* 路由（route）控制器
* service控制器
* 卷（Volume）控制器

5. **kube-schedule**

 监视新创建没有分配到Node的Pod，为Pod选择一个Node。 schedule调度器

6. **addons**

插件（addon）是实现集群pod和Services功能的 。Pod由Deployments，ReplicationController等进行管理。Namespace 插件对象是在kube-system Namespace中创建。

7. **DNS**

虽然不严格要求使用插件，但Kubernetes集群都应该具有集群 DNS，群集 DNS是一个DNS服务器，能够为 Kubernetes services提供 DNS记录，由Kubernetes启动的容器自动将这个DNS服务器包含在他们的DNS searches中。

8. **用户界面**

kube-ui提供集群状态基础信息查看。

9. **容器资源管理监控**

提供UI浏览监控数据

10. **Cluster-level-Logging**

负责保存容器日志，搜索/查看日志

11. **节点（note）组件**

提供Kubernetes运行时的环境，以及维护Pod。

12. **kubelet**

 节点代理，它会监视已分配给节点(note)的pod ,负责Pod生命周期管理，具体功能：

* 下载Pod所需要的volume（容量）
* 下载Pod的secrets（秘钥）
* Pod中运行的docker容器
* 定期执行容器健康检查
* 通过在必要时创建镜像容器，将容器的状态报告回系统的其余部分
* 将节点的状态报告回系统的其余部分

13. **kube-proxy**

kube-proxy通过在主机上维护网络规则并执行连接转发来实现Kubernetes服务抽象，proxy:代理

14. **docker**

运行容器

15. **RKT**

 rkt运行容器，作为docker工具的替代方案

16. **supervisord**

 一个轻量级的监控系统，用于保障kubelet和docker运行 

17. **fluentd**

fluentd是一个守护进程，可提供cluster-level logging

##### k8s对象

1. k8s对象是k8s中的持久实体，k8s中使用这些实来表示集群的状态。可以描述的信息有：

* 容器化应用正在运行（以及在哪些节点上）
* 这些应用的可用资源
* 关于这些应用如何运行的策略，如重新策略、升级、容错

对象可以创建、修改、删除，k8s中对象是record of intent（意向记录） ，一旦创建了对象，k8s会确保对象的存在，**通过创建对象，可以有效地告诉k8s系统你希望集群的工作负载是什么样的**。要使用Kubernetes对象（无论是创建，修改还是删除），都需要使用Kubernetes API。例如，当使用kubectl命令管理工具时，CLI会为提供Kubernetes API调用。

2. k8s对象的规范（Spec）和状态（Status）。

Object对象的配置包含了规范和状态两部分，Spec描述了对象的规范，Status描述了对象的实际状态， 并由Kubernetes系统提供和更新。 

3.**描述k8s对象**

在Kubernetes中创建对象时，必须提供描述其所需Status的对象Spec，以及关于对象（如name）的一些基本信息。当使用Kubernetes API创建对象（直接或通过kubectl）时，该API请求必须将该信息作为JSON包含在请求body中。通常，可以将信息提供给kubectl .yaml文件，在进行API请求时，kubectl将信息转换为JSON。Deployment：（de p la ment）部署

使用yaml文件创建一个应用

```yaml
apiVersion: apps/v1beta1
kind: Deployment        		 #种类：部署
metadata:						#元数据
  name: nginx-deployment  		 #名称
spec:						    #期望创建的规范
  replicas: 3                     #复制品，集群节点数量
  template:						#模板
    metadata:      
      labels:
        app: nginx
    spec:
      containers:				#容器
      - name: nginx
        image: nginx:1.7.9		#镜像
        ports:
        - containerPort: 80		#容器端口
```

**yaml必填信息**

- apiVersion - 创建对象的Kubernetes API 版本

- kind - 要创建什么样的对象？

- metadata- 具有唯一标示对象的数据，包括 name（字符串）、UID和Namespace（可选项）

  还需要提供对象Spec字段，对象Spec的精确格式（对于每个Kubernetes 对象都是不同的），以及容器内嵌套的特定于该对象的字段。Kubernetes API reference可以查找所有可创建Kubernetes对象的Spec格式。

执行：kubectl create -f docs/user-guide/nginx-deployment.yaml --record

创建成功返回：deployment "nginx-deployment" created

##### k8s Namespaces

Namespaces：命名空间，它可以用来创建多个虚拟集群

1. 创建

```yaml
(1) 命令行直接创建
$ kubectl create namespace new-namespace

(2) 通过文件创建
$ cat my-namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: new-namespace

#执行命令：kubectl create -f ./my-namespace.yaml
```

2.删除

kubectl delete namespaces new-namespace

* 删除一个namespace会自动删除所有属于该namespace的资源。
* 删除一个namespace会自动删除所有属于该namespace的资源。
* default和kube-system命名空间不可删除。
* PersistentVolumes(持久卷)是不属于任何namespace的，但PersistentVolumeClaim(持久卷申明)是属于某个特定namespace的。
* Events是否属于namespace取决于产生events的对象。

3. 查看

```shell
$ kubectl get namespaces
NAME          STATUS    AGE
default       Active    1d
kube-system   Active    1d
# Kubernetes从两个初始的Namespace开始：
# default
# kube-system 由Kubernetes系统创建的对象的Namespace
```

3. 修改

```shell
#临时设置request的Namespace，使用--namespace 标志
$ kubectl --namespace=<insert-namespace-name-here> run nginx --image=nginx
$ kubectl --namespace=<insert-namespace-name-here> get pods
#永久设置 使用kubectl命令创建的Namespace可以永久保存在context中
$ kubectl config set-context $(kubectl config current-context) --namespace=<insert-namespace-name-here>
# Validate it
$ kubectl config view | grep namespace:
```

##### k8s Pod

Pod是k8s对象模型中，**可创建/部署的最小基本单位（即是最小对象）**，一个Pod代表一个集群上一个正在的进程， **一个Pod封装一个**应用容器（也可以有多个容器），存储资源、一个独立的网络IP以及管理控制容器运行方式的策略选项。Pod代表部署的一个单位：Kubernetes中单个应用的实例，它可能由单个容器或多个容器共享组成的资源。 Docker是Kubernetes Pod中最常见的runtime ，Pods也支持其他容器runtimes。

**使用Pod**

- **Pod中运行一个容器**。“one-container-per-Pod”模式是Kubernetes最常见的用法; 在这种情况下，你可以将Pod视为单个封装的容器，但是Kubernetes是直接管理Pod而不是容器。
- **Pods中运行多个需要一起工作的容器**。Pod可以封装紧密耦合的应用，它们需要由多个容器组成，它们之间能够共享资源，这些容器可以形成一个单一的内部service单位 - 一个容器共享文件，另一个“sidecar”容器来更新这些文件。Pod将这些容器的存储资源作为一个实体来管理。

每个Pod都是运行应用的单个实例，如果需要水平扩展应用（例如，运行多个实例），则应该使用多个Pods，每个实例一个Pod。在Kubernetes中，这样通常称为Replication（复写）。Replication的Pod通常由Controller创建和管理

**一个Pod中如何管理多个容器**

 单个Pod中共同管理多个容器是一个相对高级的用法，应该只有在容器紧密耦合的特殊实例中使用此模式。 Pods提供两种共享资源：网络和存储。 

* 网络

每一个Pod被分配一个独立的IP地址，一个Pod中的所有容器共享网络命令空间，包括IP和网络端口，Pod中的容器可以使用localhost进行通信，但是当Pod中的容器与外部Pod进行通信时，它们必须协调如何使用共享网络资源（如端口）。 

* 储存

Pod可以指定一组共享存储volumes。一个Pod中的所有容器都可以访问共享volumes，允许这些容器共享数据。volumes 还用于Pod中的数据持久化，以防其中一个容器需要重新启动而丢失数据。

**注意**

重启Pod中的容器跟重启Pod不是一回事。Pod只提供容器的运行环境并保持容器的运行状态，重启容器不会造成Pod重启，**pod位于note中**。

 Pod不会自愈。如果Pod运行的Node故障，或者是调度器本身故障，这个Pod就会被删除。同样的，如果Pod所在Node缺少资源或者Pod处于维护状态，Pod也会被驱逐。Kubernetes使用更高级的称为Controller的抽象层，来管理Pod实例。虽然可以直接使用Pod，但是在Kubernetes中通常是使用Controller来管理Pod的。 

**Pod和Controller**

Controller可以创建和管理多个Pod，提供副本管理、滚动升级和集群级别的自愈能力。例如，如果一个Node故障，Controller就能自动将该节点上的Pod调度到其他健康的Node上。

k8s note

note：节点，即k8s中的工作节点，一个node可以是vm（虚拟机）或者物理机，它是集群中单个机器的表示。在大多数生产系统中，节点很可能是数据中心中的物理机器，或者是托管在像谷歌云平台这样的云供应商上的虚拟机。每个note具有运行Pod的一些必要服务，有Master组件进行管理, Node节点上的服务包括Docker、kubelet（节点代理）和kube-proxy（代理）等组件。

**创建**

 与 [pods](http://docs.kubernetes.org.cn/312.html) 和 services 不同，**节点不是由Kubernetes 系统创建，它是由Google Compute Engine等云提供商在外部创建的，或使用物理和虚拟机。这意味着当Kubernetes创建一个节点时，它只是创建一个代表节点的对象，创建后，Kubernetes将检查节点是否有效。**

Kubernetes将在内部创建一个节点对象，并通过基于metadata.name字段的健康检查来验证节点，如果节点有效，即所有必需的服务会同步运行，则才能在上面运行pod。请注意，Kubernetes将保留无效节点的对象（除非客户端有明确删除它）并且它将继续检查它是否变为有效。

例如，如果使用以下内容创建一个节点： 

```yaml
{
  "kind": "Node",
  "apiVersion": "v1",
  "metadata": {
    "name": "10.240.79.157",
    "labels": {
      "name": "my-first-k8s-node"
    }
  }
}
```

虚拟机和物理机区别：

物理机有实体，虚拟机没有，即物理机是有实体的硬件系统，比如服务器，而虚拟机是借助物理机虚拟出虚拟的硬件系统。

**note的信息包含：**

- Addresses

  包含了以下字段信息，这些字段的使用取决于云提供商或裸机配置。 

  - HostName：可以通过kubelet 中 --hostname-override参数覆盖。
  - ExternalIP：可以被集群外部路由到的IP。
  - InternalIP：只能在集群内进行路由的节点的IP地址。

- ~~Phase~~ (已弃用)

- Condition（状态）

  描述所有运行（Running）节点的状态

  | Node Condition | Description(描述)                                            |
  | :------------: | :----------------------------------------------------------- |
  |   OutOfDisk    | True：如果节点上没有足够的可用空间来添加新的pod；否则为：False |
  |     Ready      | True：如果节点是健康的并准备好接收pod；False：如果节点不健康并且不接受pod；Unknown：如果节点控制器在过去40秒内没有收到node的状态报告。 |
  | MemoryPressure | True：如果节点存储器上内存过低; 否则为：False。              |
  |  DiskPressure  | True：如果磁盘容量存在压力 - 也就是说磁盘容量低；否则为：False。 |

  ```json
  #condition  是一个 JSON 对象
  "conditions": [
    {
      "kind": "Ready",
      "status": "True"
    }
  ]
  ```

- Capacity（容量）

  描述note节点上可用的资源：cpu、内存和可用调节到note上的最大pod数

- Info

关于节点的一些基础信息，如内核版本、Kubernetes版本（kubelet和kube-proxy版本）、Docker版本（如果有使用）、OS名称等。信息由Kubelet从节点收集。

目前，有三个组件与Kubernetes节点（note）接口进行交互：**节点控制器（node controller）、kubelet和kubectl。**

**note Controller**

节点控制器是管理节点的Master组件，如何管理节点：

1. 节点注册时，将CIDR块(l路由)分给节点
2. 当节点不健康时，会询问该节点VM是否可用，不可用则删除该节点
3.  监测节点的健康状况。当节点变为不可访问时，节点控制器负责将NodeStatus的NodeReady条件更新为ConditionUnknown，随后从节点中卸载所有pod ，如果节点继续无法访问，（默认超时时间为40 --node-monitor-period秒，开始报告ConditionUnknown，之后为5m开始卸载）。节点控制器按每秒来检查每个节点的状态。 

**手动管理节点**

集群管理员可以创建和修改节点对象，如果管理员希望手动创建节点对象，请设置kubelet flag --register-node=false。

##### Deployment

* 定义一组Pod的副本数量、版本等
* 通过控制器维持Pod的数目
  * 自动恢复失败的Pod

* 通过控制器来使用指定的策略控制版本
  * 滚动升级、重新生成、回滚等

一个 Deployment 可能有两个甚至更多个完全相同的 Pod。对于一个外部的用户来讲，访问哪个 Pod 其实都是一样的，所以它希望做一次负载均衡，在做负载均衡的同时，我只想访问某一个固定的 VIP，也就是 Virtual IP 地址，而不希望得知每一个具体的 Pod 的 IP 地址。

##### Service

* 提供访问一个pod或者多个pod实例的稳定访问地址
* 支持多种访问方式实现
  * ClusterIP
  * NodePort
  * LoadBalancer

##### 集群

 在Kubernetes中，节点汇聚资源，形成更强大的机器，即集群， 应该将集群看作一个整体，而无需担心单个节点的状态。 

##### 持久卷-Persistent Volumes

 因为在集群上运行的程序不能保证在特定的节点上运行，所以无法将数据保存到文件系统中的任意位置。 节点简单看做单一机器， 如果一个程序试图将数据保存到一个文件中，但随后又被转移到一个新的节点上， 那么程序就找不到这个文件了， 由于这个原因，与每个节点相关的传统本地存储被当作临时缓存来保存程序，但本地保存的任何数据都不能持久， 为了永久存储数据，Kubernetes使用持久卷(Persistent  Volumes)。虽然所有节点的CPU和RAM资源都被集群有效地汇集和管理，但持久的文件存储却不是。相反，本地或云驱动器可以作为持久卷附加到集群上。这可以看作是将外部硬盘插入到集群中。持久卷提供了可以挂载到集群的文件系统，而不与任何特定节点相关联。

##### 服务发现 

 在 pod 的生命周期过程中，比如它创建或销毁，它的 IP 地址都会发生变化，这样就不能使用传统的部署方式，不能指定 IP 去访问指定的应用。  deployment 的应用部署模式，需要创建一个 pod 组，然后这些 pod 组需要提供一个统一的访问入口。

##### helm

k8s的包管理器，

### k8s整体架构

分为Matser和Note两部分，

**全局架构：**

<img src="C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126105848736.png" alt="image-20191126105848736" style="zoom: 33%;" />

Kubernetes 有两个不同的部分构成，一个是 Master，一个是 Node。Master 负责调度资源和为客户端提供 API，客户端可以是 UI 界面或者 CLI 工具，在 Kubernetes 中 CLI 工具通常为 kubectl。 Kubernetes Master 接受使用 YAML 定义的配置文件，根据配置文件中相关信息将容器分配到其中一个 Node 上。另外，镜像库在 Kubernetes 中也起到一个很重要的角色，Kubernetes 需要从镜像库中拉取镜像基于这个镜像的容器才能成功启动。常用的镜像库有 dockerhub、阿里云镜像库等。

**Master架构**

<img src="C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126110057520.png" alt="image-20191126110057520" style="zoom:50%;" />

 Master 有三个组件：**API Server、Scheduler（调度程序）、Controlle** 

API service收到部署（Deployments）请求后，scheduler会根据需要的资源，判断各节点的资源占用情况分配合适的 Node 给新的容器。判断依据包括内存、CPU、磁盘等。 Controller 负责整个集群的整体协调和健康，保证每个组件以正确的方式运行。 

**note（节点）架构**

 Master 分配容器到 Node 执行，Node 将会承受压力，通常情况下新容器不会运行在 Master 上。或者说 Master 是不可调度的，但是你也可以选择把 Master 同时也作为 Node。**即note和Master是分离的**

<img src="C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191126111544416.png" alt="image-20191126111544416" style="zoom: 67%;" />

kube-proxy(p ro c) : 在节点中管理网络，通过管理iptables等方式使pod和pod之间以及跨主机的 pod 之间网络能够互通。

kublet想api service报告信息，并把健康状态、指标和节点状态信息存入 ETCD 中。 

Supervisord 保证 Docker 和 kubelet 一直在运行中，supervisord 并不是必须组件，可以使用其他类似组件替换。

Pod 是可以在 Kubernetes 中创建和管理的最小可部署计算单元。一个 POD 中可以包含多个容器，但 Kubernetes 仅管理 pod。如果多个容器运行在一个 POD 中，就相当于这些容器运行在同一台主机中，需要注意端口占用问题。

### k8s工具

##### Kubeadm

 Kubeadm的目标是在不安装其他功能插件的基础上，建立一个通过Kubernetes一致性测试Kubernetes Conformance tests的最小可行集群。它在设计上并不会安装网络解决方案，而是需要用户自行安装第三方符合CNI的网络解决方案（如：flannel，calico，weave network等）。 

安装Kubeadm需要手动安装Kubelet和Kubectl，因为Kubeadm是不会安装和管理这两个组件的。

- Kubelet：在群集中的所有计算机上运行的组件，并执行诸如启动pod和容器之类的操作。
- Kubectl：操作群集的命令行工具。

##### 工作流程

1. 提交请求：用户创建一个yaml文件，提交yaml文件到 **Api Service**中，yaml文件含有Pod的详细信息，包含Pod运行副本数、镜像、Labels、名称，端口暴露情况等。API Server接收到请求后将yaml文件中的spec数据存入Etcd中
2. 资源状态同步：这一步涉及到**Replication**组件，Replication组件监控着数据库中的数据变化，对已有的Pod进行数量上的同步；
3. 资源分配：**Scheduler**会检查Etcd数据库中记录的没有被分配的Pod，将此类Pod分配至具有运行能力的Node节点中，并更新Etcd数据库中的Pod分配情况；
4. 新建容器：kubernetes集群节点中的Kubelet对Etcd数据库中的Pod部署状态进行同步，目标**Node**节点上的Kubelet将Pod相关yaml文件中的spec数据递给后面的容器运行时引擎（如Docker等），后者负责Pod容器的运行停止和更新；Kubelet会通过容器运行时引擎获取Pod的状态并将信息更新至API Server，最后写入Etcd中；
5. 节点通信：**Kube-proxy**负责各节点中Pod的网络通信，包括服务发现和负载均衡。

### 常用命令

1、获取集群有哪几个虚拟集群，即有几个namespace

````
kubectl get ns 
````

集群中默认会有 `default` 和 `kube-system` 这两个 namespace。

在执行 `kubectl` 命令时可以使用 `-n` 指定操作的 namespace。

用户的普通应用默认是在 `default` 下，与集群管理相关的为整个集群提供服务的应用一般部署在 `kube-system` 的 namespace 下，例如我们在安装 kubernetes 集群时部署的 `kubedns`、`heapseter`、`EFK` 等都是在这个 namespace 下面。

另外，并不是所有的资源对象都会对应 namespace，`node` 和 `persistentVolume` 就不属于任何 namespace。