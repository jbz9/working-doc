### 简介

参考：

https://es.xiaoleilu.com/index.html

https://elasticsearch.cn/book/elasticsearch_definitive_guide_2.x/

6.4版本官网参考：https://www.elastic.co/guide/en/elasticsearch/reference/6.4/index.html

#### 概述

开源分布式搜索引擎。Elasticsearch是**面向文档(document oriented)**的。

1. **特点**

* 分布式
* 可视化工具—Kibana

* 核心-Lucene：Lucene可以被认为是迄今为止最先进、性能最好的、功能最全的搜索引擎库。
  但是，Lucene只是一个库。想要使用它，你必须使用Java来作为开发语言并将其直接集成到你的应用中，更糟糕的是，Lucene非常复杂，你需要深入了解检索的相关知识来理解它是如何工作的。Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，但是它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单。

2. **和关系型数据库的对比**

* 关系型数据库：库—表—行、列

* ES：**索引（indices）—类型(types)—文档（documents）—字段（Fields）**;**一**个索引**对**应**多**个类型，一个类型对应多个文档，一个文档对应多个字段。

  <img src="typora-user-images/image-20200316172610236.png" alt="image-20200316172610236" style="zoom:50%;" />

3. **API**

* 第一类是 URI Search ，用 HTTP **GET** 的方式在 URL 中使用查询参数已达到查询的目的
* 另一类为 Request Body Search ，**POST**，可以使用 ES 提供的基于 JSON 格式的格式

#### ES中的概念

1. **索引**

索引（名词） ：如上文所述，一个索引(index)就像是传统关系数据库中的数据库，它是相关文档存储的地方，index的复数是indices 或indexes。
索引（动词） ：「索引一个文档」表示把一个文档存储到索引（名词）里，以便它可以被检索或者查询。这很像SQL中的INSERT关键字，差别是，如果文档已经存在，新的文档将覆盖旧的文档。

倒排索引 传统数据库为特定列增加一个索引，例如B-Tree索引来加速检索。Elasticsearch和Lucene使用一种叫做倒排索引(inverted index)的数据结构来达到相同目的。

**同一个索引下最好放置同一类型下的document文档**。从ES6后，同一个索引下只能有一个类型type。

| Elasticsearch | MySQL    |
| :------------ | :------- |
| Index         | Database |
| Type          | Table    |
| Document      | Row      |
| Field         | Column   |

2. **类型**

**Node (节点)**：node 是一个运行着的 Elasticsearch 实例，一个 node 就是一个单独的 server

**Cluster (集群)**：cluster 是多个 node 的集合

**Shard (分片)**：数据分片，一个 index 可能会存在于多个 shard

类型（type）是一种逻辑的分类，它的意义由使用者来赋予

```
Elasticsearch支持以下简单字段类型：
类型 表示的数据类型
String string   字符类型
number byte, short, integer, long  数值类型
Floating point float, double			 浮点类型
Boolean boolean						        布尔类型 
Date date													日期类型
```

在 5.X 版本中，一个 index 下可以创建多个 type；

在 6.X 版本中，一个 index 下只能存在一个 type；

在 7.X 版本中，直接去除了 type 的概念，就是说 index 不再会有 type。

1. **文档**

文档是搜索信息的**基本单元**，用json表达，文档必须被包含于一个type中。

```
文档 ID文档唯一标识由四个元数据字段组成：
_id：文档的字符串 ID
_type：文档的类型名
_index：文档所在的索引
_uid：_type 和 _id 连接成的 type#id
```

##### 安装

1、使用docker安装

```
#拉取
docker pull docker.elastic.co/elasticsearch/elasticsearch:6.4.3
#运行
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.4.3
```

###### 集群搭建

一个集群可以有多个节点，节点分为主节点和从节点，主节点不做数据存储，一个节点可以有多个分片，分片分为主要分片（**primary shard**）、**复制分片(replica shard)**，增删改查都在主要分片完成，复制分片只做备份，可以提供读请求。

节点（Node），就是一个 Elasticsearch 应用实例。

**在一个生产集群中我们可以对这些节点的职责进行划分，建议集群中设置3台以上的节点作为master节点，这些节点只负责成为主节点，维护整个集群的状态。再根据数据量设置一批data节点，这些节点只负责存储数据，后期提供建立索引和查询索引的服务，这样的话如果用户请求比较频繁，这些节点的压力也会比较大，所以在集群中建议再设置一批client节点(node.master: false node.data: false)，这些节点只负责处理用户请求，实现请求转发，负载均衡等功能。**

搭建一个只用来“协调”的es节点，让这个节点加入到es集群中，然后kibana连接这个“协调”节点，这个“协调”节点，不参加主节点选举，也不存储数据，只是用来处理传入的HTTP请求，并将操作重定向到集群中的其他es节点，然后收集并返回结果。这个“协调”节点本质上也起了一个负载均衡的作用。

分片是创建索引时指定的，并不是物理以上的。

| 节点名称 | 节点角色                                                     | 角色权限                                                     |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 节点1    | 主节点<br/>node.master: true <br/> node.data: false          | 创建、删除索引、决定将哪些分片分配给哪些节点，管理者，不存储数据 |
| 节点2    | 数据节点<br/>node.master: false<br/> node.data: true         | 对文档进行CURD操作                                           |
| 节点3    | 协调节点预先处理数据节点(ingest)<br/>node.master: false<br/>node.data: false<br/>node.ingest: false | kanbana                                                      |
| 节点4    | 预先处理数据节点(ingest)<br/>node.master: false<br/>node.data: false<br/>node.ingest: true | 在对文档搜索之前对文档进行预处理                             |
| 节点5    | 负载均衡节点<br/>node.master: false<br/> node.data: false    |                                                              |

建议：

主分片3 备份1   主分片3个 ，每个主分片对应的1个备分片=3*2=6

主分片3 备份2   主分片3个 ，每个主分片对应的2个备分片=3*3=9

<img src="typora-user-images/image-20200320143343652.png" alt="image-20200320143343652" style="zoom:50%;" />

1. 修改**集群名称、节点名称、数据存放位置、日志存放位置、绑定的IP、主机列表**

```javascript
#集群名称
cluster.name: xiake
#节点重名
node.name: xiake-1
#数据、日志存放位置
path.data: /home/open/middleware/es-6.4.2/elasticsearch-6.4.2/data
path.logs: /home/open/middleware/es-6.4.2/elasticsearch-6.4.2/logs
#绑定监听的网络接口，监听传入的请求，可以设置为IP地址或者主机名
network.bind_host: 192.168.0.11
#发布地址，用于通知集群中的其他节点，和其他节点通讯，不设置的话默认可以自动设置
network.publish_host: 192.168.81.110
#设置对外服务的http端口，默认为9200
http.port: 9200
#初始的主机列表，即新节点启动时执行发现的初始主机列表，不填写本机IP
discovery.zen.ping.unicast.hosts: ["192.168.52,141", "192.168.52,142"] 或者
discovery.zen.ping.unicast.hosts: ["192.168.52.141:9300","192.168.52.142:9300","192.168.81.110:9300"]
#指定该节点是否有资格被选举成为master节点，默认是true，es是默认集群中的第一台机器为master，如果这台机挂了就会重新选举master
node.master: true
#如果没有这种设置,遭受网络故障的集群就有可能将集群分成两个独立的集群 - 分裂的大脑 - 这将导致数据丢失
#超时时间太短有问题，找不到主节点 no known master node
discovery.zen.ping_timeout: 120s
client.transport.ping_timeout: 60s
#最小节点数
discovery.zen.minimum_master_nodes: 2
#设置几点交互Tcp端口
transport.tcp.port: 9300
#允许该节点存储数据(默认开启)
node.data: true
# 锁定物理内存地址，防止elasticsearch内存被交换出去,也就是避免es使用swap交换分区
bootstrap.memory_lock: true
# 设置请求内容的最大容量,默认100mb
http.max_content_length: 100mb
#不启用x-pack安全认证
xpack.security.enabled: false
#禁用swapping
bootstrap.memory_lock: true
bootstrap.system_call_filter: false
#thread_pool.index.size: 15
thread_pool.index.queue_size: 500
#一台服务器最大运行的节点数
node.max_local_storage_nodes: 1
#是否使用http协议对外提供服务，默认为true，开启。 
http.enabled: false
```

**生产模式**

1.修改最大文件打开数

Linux默认配置下最大打开文件数为1024，可通过`ulimit -n`查看，而ES在建索引过程中会打开很多小文件，这样很容易超过限制

2.禁止交换空间

Linux的交换空间机制是指，当内存资源不足时，Linux把某些页的内容转移至硬盘上的一块空间上，以释放内存空间。硬盘上的那块空间叫做交换空间(swap space)。如果不关闭swap，Elasticsearch的堆内存可能会被挤到磁盘中，垃圾回收速度会从毫秒级别变成分钟级别，导致节点的响应速度慢甚至和集群断开连接。

当内存不足时，Linux会移动数据到磁盘中，这样会导致Elasticsearch的数据被挤压到磁盘。

```shell
 #尽量不使用，非禁用。
 #Elasticsearch通过文件映射(mmap)来读取磁盘中的文件，这样可以比read系统调用少一次内存拷贝，也被称为0拷贝技术。ES映射的文件会很多，所以需要修改最大映射文件的数量，通过修改vm.max_map_count配置项可实现。
 vim /etc/sysctl.conf
 添加
 vm.swappiness = 1
 vm.max_map_count=262144
 重新加载
 sysctl -p 
 #使用ES的内存锁
 bootstrap.memory_lock: true
```



**集群和节点**

**节点(node)**是一个运行着的Elasticsearch实例。**集群(cluster)**是一组具有相同`cluster.name`的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群。

你最好找一个合适的名字来替代`cluster.name`的默认值，防止一个新启动的节点加入到相同网络中的另一个同名的集群中。

Elasticsearch集群可以包含多个**索引(indices)**（数据库），每一个索引可以包含多个**类型(types)**（表），每一个类型包含多个**文档(documents)**（行），然后每个文档包含多个**字段(Fields)**（列）。

6.0的版本不允许一个index下面有多个type。

##### 名词

###### 索引

索引是含有相同属性的文档集合，索引一旦创建不能更改。

文档中的所有字段都会被分词，被**索引**（拥有一个倒排索引），只有这样他们才是可被搜索的。

文档是可以被索引的基本数据单位。

###### 倒排索引

查询索引和倒排索引。ES中使用**倒排索引**进行**全文搜索**。

<img src="typora-user-images/image-20200318160901645.png" alt="image-20200318160901645" style="zoom: 50%;" />

倒排索引是如何进行的：

1、需要创建倒排索引

首先对每个文档的`content`字段为单独的单词进行切分，切分后的单词叫做**词(terms)**或者**表征(tokens)**；把所有的**唯一词**放入列表并排序。—**抽出所有文档的唯一词**

例如，我们有两个文档，每个文档`content`字段包含：

1. The **quick brown** fox jumped over the lazy dog
2. Quick **brown** foxes leap over lazy dogs in summer

2、开始搜索，例如搜索` quick brown`,我们只需要找到每个词在哪个文档中出现即可。

<img src="typora-user-images/image-20200318150539540.png" alt="image-20200318150539540" style="zoom:50%;" />

3、比较匹配度

两个文档都匹配，但是第一个比第二个有更多的匹配项。 如果我们加入简单的**相似度算法(similarity algorithm)**，计算匹配单词的数目，这样我们就可以说第一个文档比第二个匹配度更高——对于我们的查询具有更多相关性。

倒排索引中存在的问题：

<img src="typora-user-images/image-20200318154022021.png" alt="image-20200318154022021" style="zoom:50%;" />

大小写、单复数和同义词没法匹配.。

解决：可以对词进行**标准化转换**。对查询词语和唯一词都进行标准化转换。标记化和标准化的过程叫做**分词(analysis)**

###### 分词和分词器

```shell
#空格分词器
POST /_analyze
{
  "analyzer": "whitespace",
  "text":     "Nice to meet you"
}
#standard分词器 大写转小写了
POST /_analyze
{
  "analyzer": "standard",
  "text":     "Nice to meet you"
}

#中文分词器
GET /_analyze
{
  "text":"你好啊",
  "analyzer":"pinyin"
}
```



1、分词

* 首先将文本切割成一个一个唯一词，作为倒排的索引
* 然后对这些唯一词进行标准化，提高它的查全率

中文：单字分割

###### 映射

索引中每一个文档都有一个类型，每一个类型都有自己的映射。索引—文档—类型—映射（模式定义，定义字段）。

映射包含了：字段的类型、以及字段类型代表的数据的类型、字段被ES处理的方式。**字段的数据类型和属性****ES自动创建mapping**

**Elasticsearch支持以下简单字段类型：**

| 类型                 | 表示的数据类型                                        |
| -------------------- | ----------------------------------------------------- |
| String               | `string`,`text`,`keyword`,        `keyword`不会被分词 |
| Whole number 整数    | `byte`, `short`, `integer`, `long`                    |
| Floating point  浮点 | `float`, `double`                                     |
| Boolean   布尔       | `boolean`                                             |
| Date                 | `date`  不会被分词                                    |

**复杂数据类型**

###### 增删改查

这些操作都是针对索引的。添加数据的时候为所有字段创建索引，查询的时候创建倒排索引。

###### Sql语句

###### 1**、新增、更新**

```shell
#添加数据、更新数据
#　　PUT用于更新操作，POST用于新增操作比较合适。
1、PUT /索引名称/类型名称/指定的id
PUT /test/user/1
{
    "first_name" : "王大",
    "age" :        25,
     "birthday": "1999-01-01",
    "interests": [ "玩游戏", "听音乐","码代码" ]
}

#数据一致性  ?version=8 请求带上版本号，每个索引的文档都有一个版本号。关联的 version编号作为对索引API请求的响应的一部分返回。
PUT /test/user/1?version=8
{
    "first_name" : "王大",
    "age" :        31,
     "birthday": "1999-01-01",
    "interests": [ "玩游戏", "听音乐","码代码" ]
}

#如果不存在，则创建，如果存在则创建失败
PUT test/user/4?op_type=create
{
    "user" : "张三",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}

2、POST /索引名称/索引名称  不指定ID，自动生成ID ,添加
POST /test/user
{
    "first_name" : "王大",
    "age" :        25,
     "birthday": "1999-01-01",
    "interests": [ "玩游戏", "听音乐","码代码" ]
}


#更新

POST /test/user/1/_update
{
    "doc": {
      "first_name": "王六"
    }
}

PUT /test/user/1
{
    "first_name" : "王大大",
    "age" :        25,
     "birthday": "1999-01-01",
    "interests": [ "玩游戏", "听音乐","码代码" ]
}
#通过查询更新 查询25岁的人更新为100岁  Painless是ES默认的脚本语言
https://www.elastic.co/guide/en/elasticsearch/painless/6.4/painless-lang-spec.html

POST /test/user/_update_by_query
{
   "script": {
    "source": "ctx._source.age=100",
    "lang": "painless"
  },
  "query":{
    "match":{
      "age":25
    }
  }
 
}

#迁移索引 复制a索引 数据到 新的 b索引,想覆盖id和类型相同的文档。
POST _reindex
{
  "source": {
    "index": "a"
  },
  "dest": {
    "index": "b"
  }
}
#获取专业信息
GET /a/user/3/_termvectors
```

###### 2、**查询**

* 简单查询

  ```shell
  GET /索引名称/类型名称/id
  GET /test/user/1
  #指定字段查询
  GET /test/user/_search?q=age:25
  #仅仅返回  数据库字段
  GET test/user/1/_source
  #查询索引下所有数据
  GET /test/user/_search
  GET /test/user/_search?q=*&sort=age:asc&pretty #查询所有并且按照age降序 格式化
  #或者  size:返回条数 默认为10；from从第一条开始
  GET /test/user/_search
  {
    "query": {
      "match_all": {}
    },
    "sort": [
      {
          "age": "asc"
        }
    ],
    "from": 0, 
    "size": 2
  }
  
  #语句查询 含有王大
  GET /test/user/_search
  {
    "query": {
      "match": {
      "first_name": "王大"
        
      }
    }
  }
  
  #创建索引时指定mapping
  PUT my_index
  {
    "mappings": {
      "doc": {
        "dynamic": false,
        "properties": {
          "title": {
            "type": "text",
            "analyzer": "ik_max_word",
                          "search_analyzer": "ik_max_word"
          },
          "name": {
            "type": "keyword"
          },
          "age": {
            "type": "integer"
          }
        }
      }
    }
  }
  #查询映射
  GET /索引名称/_mapping
  ```

  <img src="typora-user-images/image-20200321152859312.png" alt="image-20200321152859312" style="zoom:50%;" />

  

  * 结构化查询-使用json格式化

  ```
  #查询年龄小于25且姓名为XXX
  GET /test/user/_search
  {
      "query" : {
          "bool": {
              "filter" : {
                  "range" : {
                      "age" : { "lt" : 30 } 
                  }
              },
              "must": {
                  "match" : {
                      "first_name" : "王大" 
                  }
              }
          }
      }
  }
  
  #查询部分字段
  GET /test/user/_search
  {
    "query": {
      "match_all": {}
    },
    "_source": ["first_name","age"], 
    "from": 0, 
    "size": 10
  }
  #查询first_name中含有“八”的数据。模糊查询
  GET /test/user/_search
  {
    "query": {
      "match": {
        "first_name": "八"
      }
    },
    "from": 0, 
    "size": 10
  }
  ```

  * 布尔查询

  ```shell
  #查询 first_name 字段含有“王”且含有“三”  且
  GET /test/user/_search
  {
    "query": {
      "bool": {
        "must": [
          { "match": { "first_name": "王" } },
          { "match": { "first_name": "三" } }
        ]
      }
    }
  }
  #或
  GET /test/user/_search
  {
    "query": {
      "bool": {
        "should": [
          { "match": { "interests": "唱歌" } },
          { "match": { "interests": "看书" } }
        ]
      }
    }
  }
  
  #不包含 
  GET /test/user/_search
  {
    "query": {
      "bool": {
        "must_not": [
          { "match": { "interests": "唱歌" } },
          { "match": { "interests": "看书" } }
        ]
      }
    }
  }
  
  #查询年龄是17 且 first_name不含有“五”
  GET /test/user/_search
  {
    "query": {
      "bool": {
        "must": [
          { "match": { "age": "17" } }
        ],
        "must_not": [
          { "match": { "first_name": "五" } }
        ]
      }
    }
  }
  ```

  * 过滤查询

  ```shell
  #查询所有 年龄在10-16岁之间的. 范围查询
  GET /test/_search
  {
    "query": {"bool": {
      "must": [
        {"match_all": {}}
      ],
      "filter": {
        "range": {
          "age": {
            "gte": 10,
            "lte": 16
          }
        }
      }
    }}
  }
  
  
  ```

* 聚合查询

  ```shell
  #分组 按照 age 字段 对数据进行分组
  #类似 SELECT state, COUNT(*) FROM bank GROUP BY state ORDER BY COUNT(*) DESC LIMIT 10;
  GET /test/_search
  {
    "size": 0,
    "aggs": {
      "group_by_age": {
        "terms": {
          "field": "age"
        }
      }
    }
  }
  
  #计算平均值，分组完 按照age 计算平均值
  GET /test/_search
  {
      "size": 0,
      "aggs": {
          "group_by_age": {
              "terms": {
                  "field": "age"
              },
              "aggs": {
                  "average_balance": {
                      "avg": {
                          "field": "age"
                      }
                  }
              }
          }
      }
  }
  # 先按照年龄分组 再按性别分组 、再按账号分组
  GET /bank/_search
  {
    "size": 0,
    "aggs": {
      "group_by_age": {
        "range": {
          "field": "age",
          "ranges": [
            {
              "from": 20,
              "to": 30
            },
            {
              "from": 30,
              "to": 40
            },
            {
              "from": 40,
              "to": 50
            }
          ]
        },
        "aggs": {
          "group_by_gender": {
            "terms": {
              "field": "gender.keyword"
            },
            "aggs": {
              "average_balance": {
                "avg": {
                  "field": "balance"
                }
              }
            }
          }
        }
      }
    }
  }
  

  ```
  
* 多表查询

  ```shell
  #查询2个索引里的文档，需要指定 索引、类型、id
  GET /_mget
  {
      "docs": [
          {
              "_index": "a",
              "_type": "user",
              "_id": "3"
          },
          {
              "_index": "b",
              "_type": "user",
              "_id": "3",
              "_source": {
                  "include": [
                      "name"
                  ],
                  "exclude": [
                      "post_date"
                  ]
              }
          }
      ]
  }
  
  # 查询 a,b 索引
  GET /_all/_search?q=age:25
  #查询所有索引
  GET /a,b/_search?q=age:25
  # query查询 match查询不分词 将“王一” 分词后 再去查询 
  GET /a,b/_search
  {
    "query": {
      "match": {
      "first_name": "李一"  
      }
    }
  }
  #term查询不分词 拿“王一” 去查询 
  GET /_search
  {
    "size": 100, 
    "_source": true,
    "query" : {
          "term" : { "first_name": "王一"}
      }
   #查询关键字高亮  min_score 最小命中率
  GET /_search
  {
    "min_score": 0.9,
    "size": 100, 
    "_source": true,
    "query" : {
          "match": { "first_name": "王一"}
      },
      "highlight": {
        "fields" : {
              "first_name" : {}
          }
      }
  }
  
  ```

  

###### **3、删除**

```shell
#按照id删除
DELETE test/user/1
#删除一个索引下全部数据
POST /test/_delete_by_query
{
  "query":{
    "match_all": {}
  }
}

#删除 "first_name" 含有 "一" 的数据
POST /test/_delete_by_query
{
  "query":{
    "match": {
     "first_name": "一"
    }
  }
}
```



###### API



**更新**

put:将原有记录做一个删除标记，拿新的数据再创建一个文档，删除标记的文档再一定时间后自动删除

<img src="typora-user-images/image-20200319145509922.png" alt="image-20200319145509922" style="zoom:50%;" />

**文档查询**

![image-20200319153721676](typora-user-images/image-20200319153721676.png)

**分页查询**

![image-20200319155936554](typora-user-images/image-20200319155936554.png)



###### 版本控制

保持数据的一致性。采用了乐观锁机制。

乐观锁机制：是一种思想，表中有一个版本字段，当我需要进行业务操作（增删改查）的时候，我会带上这个字段和数据库中版本字段进行比较，如果通过则进行增删改查，没有通过就拒绝操作。为什么叫乐观锁？因为它实际上并没有在数据库进行加锁。

悲观锁：数据库实现，

使用乐观锁机制，有一个内部版本控制，增删改查 版本号会+1。_version

外部版本控制：mysql、oracle等关系型数据库导入到ES中，将它们的版本控制带入到ES中。version_type=external 版本号范围1-2^23。它不再检查版本号是否一致，而是检查请求带过来的版本号是否大于数据库中版本号，如果是，则请求成功，并且把请求带过来的外部版本号存储到内部版本号中。

###### 分布式架构

ES集群至少2台机器，一台主分片，一台副本分片

垂直扩容：服务器数量不变，扩大服务器容量。

水平扩大：直接增加服务器数量

容错性：

| 节点数量 | 可以宕机数量 |
| -------- | ------------ |
| 3        | 1            |
|          |              |

###### mysql-ES

mysql中的数据库导入到ES中。

**文档ID**

1、ES自动生成

post  /索引名称/类型名称 。

ES生成的id长度为20个字符,使用的是base64编码,URL安全,使用的是GUID算法(全局唯一),分布式下并发生成id值时不会冲突。

2、手动创建

关系型数据库导入数据到ES中，使用关系型本身ID作为文档ID

###### 数据路由

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1653290782522image-20200319151700677.png)

###### 查看集群API

```
http://192.168.52.141:9200/_cat9
```

## 常见问题

### 倒排索引

1个字节=8个比特，例如00000001

1个比特只能存储0或者1，能够存储的最大的数量，即2的比特次方

int是4个字节，32个比特，因为int是有符号的整型，所以是2^31,大约21亿

#### 节点

一个索引的所有分片会被均衡的分配每个节点上；每个分片都可以看做一个独立的索引，

如果有新的节点被添加时，那么，原来集群节点上的分片，会部分迁移到新的节点上；

#### 查询原理

ES的查询分为2个阶段，查询分发阶段和结果汇总阶段

查询分发阶段：主要是从各个分片中查询中数据，因为ES的index是被分割成分片来存储到各个节点上的。

结果汇总阶段：就是把从分片中查询到数据，进行汇总排序，然后，然后给用户。

cluster state：集群状态信息

translog ：

路由Routing

每个节点，每个都存留一份路由表，所以当请求到任何一个节点时，ElasticSearch都有能力将请求转发到期望节点的shard进一步处理。

