# Argo

## 简述

ArgoCD是用于Kubernetes的声明性GitOps持续交付（CD）工具，也就是把Git仓库中的k8s配置同步应用到集群中。

## GitOps

GitOps是把Git作为交付流水线的核心，将配置文件放到在 Git 库中进行版本控制。通过使用 Git 库，应用更容易部署到 Kubernetes 中，以及进行版本回滚。更重要的是，当灾难发生时，集群的基础架构可以从 Git 库中可靠且快速地恢复。

核心：通过Git以及PR来管理K8s中的应用。

* 将k8s的配置资源文件放到Git上
* 通过PR来更改仓库上k8s的资源
* 修改git后，重新部署k8s

优势：所有的操作都会被记录下来，通过的Git的版本控制，可以快速的回滚。

## 问题

### Argo CD和Jenkins的区别

Argo是负责CD（Continuous Delivery）持续交付的,是把Git仓库的配置应用到kubernets集群环境中；

Jenkins主要负责CI持续集成。

