# Redis

## 说一下Redis

Redis是一个k、value形式的缓存数据库，它是基于内存操作的，所以速度比较快。

## Redis为什么这么快

首先是因为它是基于内存操作的；然后是它是它采用了IO多路复用，底层的epoll模式；最后它是工作线程是单线程操作，减少了CPU上下文切换的带来的资源消耗。

#### redis是单线程还是多线程

redis的核心线程，对网络IO、数据读写这块是单线程，但是对于整个redis，它里面对于一些后台任务，持久化、异步删除、集群数据同步这些是其它线程执行的。

#### 为什么redis选用单线程

Redis是存内存操作，它的性能瓶颈是网络延迟而不是执行速度，所以多线程并不能带来大的提升。

## 内存淘汰机制

Lfu：least recently used 最近最少使用，使用当前时间-最后一次访问时间，这个数值越大，淘汰的优先级就越高

Lru：least Frequently used 最不常使用，会统计每次Key的访问频率，越低，淘汰的优先级越高

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1652351786936redis-%E8%BF%87%E6%9C%9F-%E6%B7%98%E6%B1%B0%E7%AD%96%E7%95%A5.drawio.png)





##### 有哪几种数据结构

答：5种。字符串、列表（list）、Hash（散列）、无序集合（set）、有序集合(zset)

补充：

```shell
1、字符串
本机:0>set 1 "小明"
"OK"
本机:0>get 1
//同时设置多组数据，原子操作，要么都成功，要么都设置失败
本机:0>mset 1 "小米" 2 "小红"
"OK"
本机:0>get 1
"小米"
本机:0>get 2
"小红"
本机:0>del 1
"1"
//将指定的值追加到key末尾，若key不存在，则创建并赋值，返回追加后的字符串长度
本机:0>append 1 1
"7"
本机:0>get 1
"小明1"
本机:0>
//查看剩余的生存时间，-1 表示永久存在， -2表示不存在，时间秒
本机:0>ttl 1
"-1"
本机:0>set 1 "小米" ex 10
"OK"
//移除指定key的生存时间，成功返回1，若key不存在或不存在生存时间时返回0
本机:0>persist 1
"1"
  
**全局key操作，对redis的五个数据类型都适用的命令**
rename key newkey    改名
当key和newkey相同或者key不存在时返回一个错误，当newkey已存在时则会覆盖
本机:0>rename 1 2
"OK"
//查看所有的key
本机:0>keys id_*
 1)  "id_1"
 2)  "id_2"
本机:0>keys *
 1)  "id_1"
 2)  "2"
 3)  "id_2"
本机:0>exists 2 查看KEY 是否存在
"1"
本机:0>type 2  查看key的类型
"string"
本机:0>expire 2 1000 设置key的过期时间
"1"
本机:0>persist 2 移除过期时间
"1"
 
2、list列表，是一个字符串列表，可以在列表头部或尾部添加/删除数据
添加数据：
rpush key value [value…]   在尾部添加数据，右边添加
lpush key value [value…]   在头部添加数据
本机:0>rpush name "小米" "小明" "小红"
"3"
本机:0>rpush name "小李"
"4"
本机:0>lindex name 0 查看某一条数据
"小米"
本机:0>lrange name 0 -1 查看列表的所有数据
 1)  "小米"
 2)  "小明"
 3)  "小红"
 4)  "小李"
本机:0>lrange name 0 1 查询一段数据
 1)  "小米"
 2)  "小明"
本机:0>llen name 列表长度
"4"
修改数据：lset key index value
本机:0>lset name 0 "你好" 指定索引，修改列表数据
"OK"
本机:0>lindex name 0
"你好
删除数据：
lpop key    删除左边第一个 头部
rpop key    删除右边第一个

3、Set集合 元素为String类型，无序、不重复
//sadd key member [member...]     增加元素
本机:0>sadd id "1" "2" "3"
"3"
本机:0>scard id 查询元素个数
"3"
本机:0>smembers id 查询所有数据
 1)  "1"
 2)  "2"
 3)  "3"
sismember key member 
本机:0>sismember id "啊啊" 断集合存在某个值
"0"
本机:0>srem id 1 2 删除集合中的元素
"1"
删除 del key

4、Hash类型
是一个键值(key=>value)对集合。是string 类型的 field 和 value 的映射表
     user       　　　　      { name:juhao， 　　          age:18 }
user -> key(键)    　    　name,age ->field(域)    　　 juhao,18 ->value(值)
本机:0>hset user_2 name "小红" id 1 添加数据
"2"
本机:0>hgetall user_2 查询数据
 1)  "name"
 2)  "小红"
 3)  "id"
 4)  "1"
本机:0>hget user_2 name
"小红"
本机:0>hvals user_2 查询所有value
 1)  "小红"
 2)  "1"
本机:0>hkeys user_2  查询所有的key
 1)  "name"
 2)  "id"
 本机:0>hmget user_2 id name 查询多个字段的值
 1)  "1"
 2)  "小红"
本机:0>hlen user_2 查询有几个键值对
"2"

5、zset类型 类似于Set,不同的是Sorted中的每个成员都分配了一个分数（Score）用于对其中的成员进行排序（升序）。zset的成员是唯一的,但分数(score)却可以重复。
zadd key score member[ [score member] ..] 添加、修改
本机:0>zadd top 1 "赵" 2 "钱" 3 "孙"
"3"
本机:0>zrange top 0 -1 查询集合
 1)  "赵"
 2)  "钱"
 3)  "孙"
本机:0>zrangebyscore top 1 2  返回集合中 score 在给定区间的元素
 1)  "赵"
 2)  "钱"
 本机:0>zrem top "赵" 删除元素
"1"
本机:0>zrange top 1 2 删除集合中索引在给定区间的元素
 1)  "孙"
```

##### 2、redis数据过期了怎么办？

答：redis过期删除策略，主要有2种，非实时删除。

①惰性删除，它会在每次查询的时候去判断有没有过期了，过期了就删除，

②定期扫描删除：redis会定时扫描部分key，看是否过期了，过期了则删除，默认100ms

这2种方式基本可以保证大部分过期数据都会被删除，对于还有有些没有删除了数据，redis还有内存淘汰策略。



##### **4、redis持久化策略**有几种

答：有2种，是RDB和AOF。持久化是为了在redis服务器发生故障重启的时候，能够还原之前的数据，因为redis是内存数据库，数据是放在内存里面的。

RDB是快照模式，周期性的持久化，也就是每隔一段时间就将内存里面的数据存放磁盘上面去。非实时的，适合全量备份

AOF它是每次执行完更改缓存数据的命令之后，就会记录到磁盘上面去，默认是不开启的。实时的。

##### 6、缓存穿透怎么办

答：缓存穿透是从redis和数据库里面没有查到数据，导致请求到了数据库,可能是非法Key

**解决：（1）对Key进行校验（2）对于数据库和redis都没有的数据，给它在缓存里面增加一个null的值，并设置一个短期的过期时间，这样请求就不会请求到数据库了**

##### 7、缓存击穿怎么办？

答：缓存击穿是同一时间有大量的请求过来，而这个热点数据恰好都过期了。导致数据库崩溃。是针对热点数据

**解决：（1）设置随机的过期时间**（2）对应热点的数据，不设置过期时间（3）针对从数据库拿数据的逻辑进行加锁

缓存击穿实际上是缓存雪崩的一个特例，缓存击穿是指缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取数据，引起数据库压力瞬间增大，造成过大压力。击穿与雪崩的区别即在于击穿是对于某一特定的热点数据来说，而雪崩是全部数据。

```java
    public String get(key) {
        String value = redis.get(key);
        if (value == null) {
            // 分布式锁，去数据库拿数据，设置3min的超时，防止del操作失败的时候，下次缓存过期一直不能load db
            if (redis.setnx(Thread.currentThread().getId(), 1, 3 * 60) == 1) {
                //代表设置成功
                value = db.get(key);
                redis.set(key, value, expire_secs);
                redis.del(key_mutex);
            } else {
                //这个时候代表同时候的其他线程已经load db并回设到缓存了，这时候等会重试即可
                sleep(50);
                //再去拿缓存数据
                get(key);
            }
        }
        return value;
    }

//设置永不过期
public String get(Sting key){
	V v = redis.get(key);
	String value = v.getValue(); 
	long timeout = v.getTimeout(); 
	if (v.timeout <= System.currentTimeMillis()){
		//Asynchronous update background exception execution
		threadPool.execute(new Runnable(){
			public void run(){
				String keyMutex = "mutex:" + key; 
				if(redis.setnx(keyMutex, "1")){
					//3 min timeout to avoid mutex holder crash
					redis.expire(keyMutex, 3 * 60);
					String dbValue = db.get(key);
					redis.set(key, dbValue);  
           redis.delete(keyMutex);
         }
       }
    });
  }
  return value;
}
```

##### 8、缓存雪崩怎么办

答：缓存雪崩的话可以能是redis挂掉了，导致数据都请求到了数据库，那么可能对redis做一个高可用的哨兵模式；也有可能是设置了相同过期时间，同一时间大量的缓存失效了，那么就需要对数据设置不同的过期时间，也可能对热点数据设置不同的过期时间；如果是请求量太多导致的话，那些就需要加锁和对Key进行校验，和使用redis的**布隆过滤器**判断数据在不在数据库，不在的话直接返回。

解决：（1）加锁（2）随机的过期时间

##### 9、redis分布式锁了解吗，说一下怎么实现的

答：**实现的话就用redis的setex ，设置key、value和过期时间**，防止出现异常，锁得不到释放。这个命令的话它是原子操作。加锁的话就去set一条数据，拿到锁，业务逻辑处理完成后，就去删除数据释放锁(lua保证每个客户端都只是删除自己的锁)。

存在的问题：可以将过期时间放到value里，这样每次加锁的时候去判断一下，key是不是过期了没有删除

补充：

分布式锁：为了解决在分布式环境下，对共享资源的操作问题，因为分布式环境下JAVA锁已经不满足了。

Redission 

```shell
#如果 key 不存在，则插入这条数据，并且返回1，如果存在，返回0 ，不做任何操作
#是SET if Not eXists(如果不存在，则 SET)的简写。
setex key value
例如：
SETNX 1 1

# 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期，超时了，就查不到数据了，单位 秒。注意，setex 会覆盖旧值。它是原子操作，设值和设过期时间是同时的
setex key seconds value
例如：
setex 1 50 1
```

##### 10、redis有几种部署架构

答：有单机的、主从复制的、哨兵模式。

主从模型：主节点复制**写**入数据，然后通过异步将数据复制到从节点，从节点负责读取数据。所有的读取请求都是走从节点的，这样的很容易水平扩容，能够支持高并发读取。

哨兵模式：在主从模式的基础上，增加了哨兵节点，用来监控节点，一旦主节点宕机了，就会从从节点里面选择一个从节点成为主节点，做到主从切换。高可用的话就是使用哨兵模式

高可用的话还是用哨兵模式，哨兵模式的话，它的哨兵节点可以监控主节点和从节点，如果主节点挂了的话，它会重新选举主节点

##### 11、在项目中缓存是如何使用的？为什么要用缓存？

答：主要是对一些经常查询的数据，把它放到数据库里面，提高查询速度

##### 12、redis和数据库数据不一致怎么解决，怎么样才能保证缓存一致性

答：

更新数据：先更新数据库，再更新缓存。更新数据库和更新缓存是2个动作，不是原子操作，都存在失败的风险，如果先更新缓存的话，如果在更新数据库的时候失败了，那么下次取数据的时候就会从缓存里面取，这时候取到的数据就是不对的。

读取数据：先读缓存，读取不到就读数据库，然后再把到数据写到缓存中。

##### 15、什么样的数据适合放入缓存

答：缓存是为了缓解读的压力

* 经常使用的数据
* 数据不能经常变化（如果总是变的话，那么缓存里的数据总是更新）

* 非敏感的数据（比如金额，这样的数据就不适合放入缓存，缓存里可能有脏数据）

##### 15、redis的发布订阅（pub/sub）模式介绍一下？

答：发布用的是publish命令，订阅用的是subscribe命令，订阅的话可以订阅多个频道。pubscribe

补充：

```
#发布消息
本机:0>publish todaynews  "新中国成立了"
"0" 代表没人订阅

#打开另外一个客户端，去订阅
格式：SUBSCRIBE channel [channel ...]
本机: 0>subscribe todaynews
Switch to Pub/ Sub mode. Close console tab to stop 1 isten for messages.
1) " subscribe " 表示类型
2)”todaynews'    表示订阅的频道
3)"1”            订阅的数量

#退订
格式： UNSUBSCRIBE channel [channel ...]
本机:0>unsubscribe todaynews
 1)  "unsubscribe"
 2)  "todaynews"
 3)  "0"

```