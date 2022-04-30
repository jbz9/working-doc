# **Kubernets知识**
参考：

[k8s知识体系 · Kubernetes 学习笔记 (huweihuang.com)](https://www.huweihuang.com/kubernetes-notes/paas/k8s.html)

[学习资源 · Kubernetes指南 (gitbooks.io)](https://feisky.gitbooks.io/kubernetes/content/appendix/tutorial.html)
## **一. 基础概念**
容器操作平台，这些操作包括部署、调度、节点集群间扩展。
### **1. 节点Node**
运行K8s的主机或者虚拟机，实际工作的点

主节点：管理节点

- 管理节点同步
- 管理单节点生命周期
### **2. 容器组Pod**
一个 Pod 对应于由若干容器组成的一个容器组，同个组内的容器共享一个存储卷(volume)

Pod状态：

- pending：至少有一个容器启动了，则进入此状态
- running：Pod 中所有的容器都已被创建，至少有一个容器仍在运行，或者正处于启动或重启状态。
- succeeded：所有的容器都正常退出
- failed：容器组中所有容器都意外中断了
### **3. 服务Service**
对外暴露Pod，提供访问策略。Docker使用私有主机网络连接，只能与同一台机器上的容器相互通讯，如果要跨节点通信，机器需要给容器分配端口，进行端口转发。

把一组pod抽象出来做为一个微服务
### **4. 数据卷Volumes**
一个卷就是一个目录，容器对其有访问权限，防止容器重启后，数据丢失
### **5. Replication Controllers**
主要负责指定数量的 pod 在同一时间一起运行
### **6. Etcd**
是一个分布式的，依赖key-value存储的，最重要的分布式数据存储系统，可以作为一个pod部署在主节点上
### **7. 部署deployment**
借助deployment，K8s能够根据负载变化动态调整节点数量，实现负载均衡和故障自愈。

为什么不建议一个pod运行多个程序？

- 解耦：每个容器都肯能会重新发布更新，所以需要解除软件之间的耦合
- 高效：容器必须轻量化
- 透明化：便于监控系统的各个服务
## **二. 常用命令**

### **kubectl get - 列出资源**

查看所有的集群环境 多集群

kubectl config get-contexts 



切换集群

kubectl config use-context docker-desktop 



获取节点列表

kubectl get nodes -o wide  

kubectl get deployments  获取部署列表

kubectl get pods -n mw-b 获取pod列表

kubectl get services   查看service

kubectl get svc

kubectl get namespaces mw-b查看命令空间

kubectl get ns --show-labels

查看目前所有的replica set，显示了所有的pod的副本数，以及他们的可用数量以及状态

kubectl get rs -n mw-b 查看副本部署情况

kubectl get deploy -o wide 查看Deployment

kubectl get deployments -n mw-b
### **kubectl describe –列出详细信息**
kubectl describe pods

`	`查看节点详情

kubectl describe node <your-node-name>
### **kubectl logs 容器日志**
kubectl logs --tail=20 nginx  	仅输出pod nginx中最近的20条日志

4． 创建 create

5． 更新 update

6． 删除 delete

7．滚动升级 rolling-update 对指定的 replication controller 执行滚动升级

 exec 在容器内部执行命令

## **三. 常用组件**

#### **1、网络连接方式**
##### **LoadBalancer**
##### **Ingress负载均衡**
外部请求首先到达Ingress Controller，Ingress Controller根据Ingress对象的路由规则，查找到对应的Service，进而通过Endpoint查询到Pod的IP地址，然后将请求转发给Pod



ingress-controller：Ingress控制器 有：google云的GCE与ingress-nginx 

ingress对象：k8s的一个api对象，使用yaml创建

外部请求先到控制器，ingress对象告诉控制器转发规则
#### **2、网络通信**
Bridge：网桥，是一种虚拟网络设备。

Veth: 是Linux中一种虚拟出来的网络设备，veth设备总是成对出现，所以一般也叫veth-pair。充当着一个桥梁，连接着各种虚拟网络设备



基础原则

每个Pod都拥有一个独立的IP地址，而且假定所有Pod都在一个可以直接连通的、扁平的网络空间中，不管是否运行在同一Node上都可以通过Pod的IP来访问。

k8s中Pod的IP是最小粒度IP。同一个Pod内所有的容器共享一个网络堆栈，该模型称为IP-per-Pod模型。

Pod由docker0实际分配的IP，Pod内部看到的IP地址和端口与外部保持一致。同一个Pod内的不同容器共享网络，可以通过localhost来访问对方的端口，类似同一个VM内的不同进程。

IP-per-Pod模型从端口分配、域名解析、服务发现、负载均衡、应用配置等角度看，Pod可以看作是一台独立的VM或物理机。



flannel

flannel组建一个大二层扁平网络，pod的ip分配由flannel统一分配，通讯过程也是走flannel的网桥。

每个node上面都会创建一个flannel0虚拟网卡，用于跨node之间通讯。所以容器直接可以直接使用pod id进行通讯。

跨节点通讯时，发送端数据会从docker0路由到flannel0虚拟网卡，接收端数据会从flannel0路由到docker0。
##### **1、同一个pod中的各个container通讯**
在k8s中每个Pod中管理着一组Docker容器，这些Docker容器共享同一个网络命名空间，Pod中的每个Docker容器拥有与Pod相同的IP和port地址空间，并且由于他们在同一个网络命名空间，他们之间可以通过localhost相互访问。

什么机制让同一个Pod内的多个docker容器相互通信?就是使用Docker的一种网络模型：–net=container

container模式指定新创建的Docker容器和已经存在的一个容器共享一个网络命名空间，而不是和宿主机共享。新创建的Docker容器不会创建自己的网卡，配置自己的 IP，而是和一个指定的容器共享 IP、端口范围等。**这就是一个pod一个IP**

这里就是为什么k8s在调度pod时，尽量把关系紧密的服务放到一个pod中，这样网络的请求耗时就可以忽略，因为容器之间通信共享了网络空间，就像local本地通信一样。
##### **2、同一个node中的各个container通讯**
同一个Node内，不同的Pod都有一个全局IP，就是宿主机IP，可以直接通过Pod的IP进行通信。Pod地址和docker0在同一个网段
##### **3、不同node之间的pod通讯**
不同Node之间的通信需要达到两个条件：

1. 对整个集群中的Pod-IP分配进行规划，不能有冲突（可以通过第三方开源工具来管理，例如flannel）。
1. 将Node-IP与该Node上的Pod-IP关联起来，通过Node-IP再转发到Pod-IP。
##### **2、pod和service之间通讯**
Service的就是在Pod之间起到服务代理的作用，对外表现为一个单一访问接口，将请求转发给Pod，
##### **3、外部和service之间通讯**
### **四、yaml语法**
Pod示例

|    |apiVersion: v1        k8s Api的版本号 |
| :- | :- |
||kind: Pod            该元件的属性，有Pod、Node、Service、Deployment、Endpoints|
||metadata:|
||`  `name: kubernetes-demo-pod    指定该pod的名称|
||`  `labels:                        指定该pod的标签|
||`    `app: demoApp|
||spec:|
||`  `containers:|
||`    `- name: kubernetes-demo-container       指定该pod运行的容器名称|
||`      `image: hcwxd/kubernetes-demo           容器使用的image|
||`      `ports:|
||`        `- containerPort: 3000|

Service示例

ClusterIP：默认类型

自动分配一个仅 cluster 内部可以访问的虚拟 IP，使用

apiVersion: v1
kind: Service
metadata: 
`  `name: redis-leader
`  `labels:
`    `app: redis
`    `role: leader
`    `tier: backend
spec:
`  `ports: 
`  `- port: 6379
`    `targetPort: 6379
`  `type: ClusterIP



NodePort方式

在ClusterIp的基础上，增加了一个NodePort 

apiVersion: v1

kind: Service

metadata:

name: my-service  指定的service名称

spec:

selector: 选择器，去匹配标签名为app: demoApp的pods

` `app: demoApp

type: NodePort  外部访问方式LoadBalancer、NodePort、lngress

ports:

` `- protocol: TCP

`   `port: 3000         k8s集群内部service之间相互访问service端口，即service暴露出来的port

`   `targetPort: 3000       pod真正暴露的端口

`   `nodePort: 30390	     用户访问service的端口，即pod的代理端口

Service 能够将一个接收 port 映射到任意的 targetPort。 默认情况下，targetPort 将被设置为与 port 字段相同的值。

**缺点：**

1. 每个端口只能是一种服务
1. 端口范围只能是 30000-32767
1. 如果节点/VM 的 IP 地址发生变化，你需要能处理这种情况。

访问：节点ip: nodePort  不建议使用

**LoadBalancer方式**

K8集群提供一个单独的 IP 地址。已不再推荐使用，推荐使用 Ingress Controller

**Ingress**

能够使用同一个 IP 暴露多个服务，

**Deployment示例**

将pod做横向扩展，形成pod集群

apiVersion: apps/v1

kind: Deployment

metadata:

`  `name: my-deployment

spec:

`  `replicas: 3  建立多少个pod

`  `template:    所有pod的统一设定

`    `metadata:

`      `labels:

`        `app: demoApp  指定的标签名称

`    `spec:

`      `containers:

`        `- name: kubernetes-demo-container

`          `image: hcwxd/kubernetes-demo

`          `ports:

`            `- containerPort: 3000

`  `selector:

`    `matchLabels:  匹配到标签名称为 app: demoApp的pod

`      `app: demoApp

