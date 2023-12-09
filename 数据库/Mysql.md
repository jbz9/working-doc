## MySQL

### 介绍

它是一个关系型数据库，包含了“一对一、一对多、多对多”这些关系模型，本身是开源免费的。MySQL 5.5版本后默认的存储引擎为InnoDB。

####  数据库三大范式

* 第一范式：**每一列都不可以再拆分**
* 第二范式：非主键列**完全依赖主键**，不能部分依赖
* 第三范式：非主键列**直接依赖主键**，不能间接依赖

①第一范式

| id   | name | 联系方式           |
| ---- | ---- | ------------------ |
| 001  | 小明 | 188998             |
| 002  | 小王 | 6347623234@163.com |

假设现在有一张user表，字段有id、name、联系方式，这几个字段，因为联系方式可以分为：手机号码和邮箱，还能够再分，所以它是不符合第一范式

②第二范式

| 学生编号（PK） | 教师编号(PK) | 学生姓名 | 教师姓名 |
| -------------- | ------------ | -------- | -------- |
| 001            | 01           | 小明     | 王**     |

假设现在有张学生表，存放学生和老师的信息，用学生ID和老师ID做复合主键，存放学生姓名、老师姓名。因为学生姓名只依赖学生ID，而不依赖老师ID，所以是部分依赖，表需要拆分。

③第三范式

| 学生ID | 学生姓名 | 所在班级编号 | 所在班级名称 |
| ------ | -------- | ------------ | ------------ |
| 001    | 小明     | 1-1          | 一班         |

假设现在有张学生表，存放了学生ID、学生姓名、班级编号、班级名称，那么其中，班级名称是依赖于班级编号，所在班级编号依赖于学生ID，这就是间接依赖主键，违背了第三范式。

### 储存引擎

#### InnoDB

- **特点：** InnoDB是MySQL默认的事务型存储引擎，**支持事务、行级锁、外键**等特性。它适用于大多数应用场景，尤其是需要事务支持的应用。

- **原理实现：** InnoDB采用了聚集索引的方式，数据文件本身就是按照主键顺序组织的，这使得范围查询和按主键顺序查询非常高效。同时，InnoDB还支持非聚集索引，通过B+树结构实现。

- **示例：**

  ```
  sqlCopy codeCREATE TABLE example_table (
      id INT PRIMARY KEY,
      name VARCHAR(50)
  ) ENGINE=InnoDB;
  ```

#### MyIsam

**InnoDB和MyIsam对比**

| MyISam                                                    | InnoDB                                        |      |
| --------------------------------------------------------- | --------------------------------------------- | ---- |
| 只支持表锁                                                | **支持表锁和行锁**                            |      |
| 不支持事务                                                | 支持事务                                      |      |
| 三个文件存储 一个是存储数据、一个存储索引、一个存储表结构 | 2个文件存储，一个存表结构，一个存储索引和数据 |      |
| 插入和查询性能较高，但不适合频繁的更新和删除操作。        |                                               |      |

### 内存结构

[MySQL memory architecture and index description | Develop Paper]()

### 事务

#### 基础介绍

**事务**是一系列数据库操作，被视为一个独立的工作单元。在许多业务场景中，一系列的数据库操作必须原子执行，以避免数据不一致或丢失的情况。在MySQL中，事务具有四个特性，通常被称为ACID属性：

1. **原子性（Atomicity）：** 事务中的所有操作**要么全部执行成功，要么全部失败回滚。没有中间状态。**
2. **一致性（Consistency）：** **事务开始前和结束后，数据库从一个一致的状态转移到另一个一致的状态**，保持数据的完整性。
3. **隔离性（Isolation）：** **多个事务并发执行时**，每个事务都看到一个独立的、不受其他事务影响的数据快照。
4. **持久性（Durability）：** **一旦事务提交，数据就会持久化到磁盘**，即使系统发生故障。

#### 如何保证 ACID

1. **事务日志（Transaction Log）：**
   - 数据库系统通过**事务日志**记录事务的操作，包括事务开始、提交、回滚等。在发生故障时，可以通过**重放事务日志**来保证持久性。
2. **锁机制：**
   - 锁机制用于确保事务的隔离性，防止并发事务之间的冲突。通过使用**共享锁和排他锁**等方式，控制事务对数据的访问。
3. **回滚机制：**
   - 当事务发生错误或违反完整性约束时，数据库系统会执行回滚操作，将数据库恢复到事务开始前的状态，确保原子性和一致性。
4. **持久性机制：**
   - 数据库系统使用持久性机制将已提交的事务的修改写入数据库的永久存储介质，例如磁盘。这通常涉及到将事务的修改缓冲到内存中，然后定期刷新到磁盘。
5. **并发控制机制：**
   - 通过并发控制机制，如锁机制或多版本并发控制（MVCC），确保事务的隔离性，防止并发执行事务之间的相互干扰。

#### 事务并发

在**并发环境下**，事务的隔离性很难保证，**会出现一系列问题，比如脏读、幻读、不可重复读。**这些问题源于多个事务同时执行，争夺数据库资源，而没有合适的隔离机制来保证并发执行的事务之间不会相互干扰。

##### 并发带来的问题

以下是一些常见的事务**并发问题：**

1. **脏读（Dirty Read）：** 一个事务**读取了另一个事务未提交的数据。**如果事务 A 修改了数据，但尚未提交，而此时事务 B 读取了这个未提交的数据，就会产生脏读。
2. **不可重复读（Non-repeatable Read）：** 在一个事务中，**两次读取相同的数据得到的结果不一致。**这可能是由于其他事务在两次读取之间修改了数据，导致第二次读取的结果不同。
3. **幻读（Phantom Read）：** 在一个事务中，**两次查询同一个范围的数据得到的结果不一致。**这可能是由于其他事务在两次查询之间插入或删除了数据，导致结果不一致。
4. **丢失更新（Lost Update）：** 多个事务同时对同一数据进行修改，但由于缺乏合适的同步机制，其中一个事务的修改可能会被另一个事务覆盖，导致数据丢失。
5. **并发事务控制不当：** 缺乏有效的并发事务控制机制，可能导致事务之间的竞争条件和数据不一致性，影响系统的正确性和可靠性

##### 解决方案

1. **隔离级别：** MySql提供不同的隔离级别，如读未提交、读已提交、可重复读、串行化，用于控制事务的隔离性，从而解决脏读、不可重复读和幻读等问题。（MySql提供，我们使用）
2. **锁机制：** 使用锁来确保在同一时间只有一个事务可以访问某个数据项，防止并发修改导致的问题。包括共享锁和排它锁等。（MySql提供，我们使用）
3. **MVCC（多版本并发控制）：** 通过保存数据的历史版本，不同事务看到不同版本的数据，从而避免了一些并发问题。（MySql内部实现，不需要我们关心）
4. **乐观并发控制：** 事务在提交时检查是否有冲突，如果没有冲突则提交，否则回滚。通过版本号或时间戳等方式实现。（CAS，我们自己实现）
5. **悲观并发控制：** 在事务执行期间对数据进行加锁，防止其他事务访问。适用于对数据更新频繁的场景。（MySql提供锁，我们使用）

###### **隔离级别**

隔离级别从低到高分别为：

* ⬇️**读未提交**（Read Uncommitted）-三个一个都解决不了
* ⬇️**读已提交**（Read Committed）-解决脏读
* ⬇️**可重复读**（Read Repeatable）-解决脏读、不可重复读

- ⬇️**可串行化**（Serializable）-脏读、不可重复度、幻读的问题

|      隔离级别      | 脏读 | 不可重复读 |      幻读       | 加锁 |
| :----------------: | :--: | :--------: | :-------------: | ---- |
| 读未提交读（最低） |  √   |     √      |        √        | 否   |
|      读已提交      |  ×   |     √      |        √        | 否   |
|      可重复读      |  ×   |     ×      | √（innodb除外） | 否   |
|   串行化（最高）   |  ×   |     ×      |        ×        | 是   |

**Mysql默认级别是可重复读，InnoDB使用MVCC解决脏读、不可重复读的问题，使用加锁解决幻读**

**脏读**

由于事务的可回滚特性，因此commit前的任何读写，都有被撤销的可能，假如某个事物读取了还未commit事务的写数据，后来对方回滚了，那么读到的就是脏数据，因为它已经不存在了。

**幻读**

事务A查询一个范围的值，另一个并发事务B往这个范围中插入了数据并提交，然后事务A再查询相同范围，发现多了一条记录，或者某条记录被别的事务删除，事务A发现少了一条记录。

**不可重复读**

事务A读取一个值，但是没有对它进行任何修改，另一个并发事务B修改了这个值并且提交了，事务A再去读，发现已经不是自己第一次读到的值了，是B修改后的值，就是不可重复读。

实现可串行化通常有下面三种方法

- **串行执行事务**
- **二阶段加锁**
- **乐观并发控制技术**

```sql
1、开启事务
2、编写事务的sql（多条sql语句）
3、提交事务或回滚事务
savepoint 是事务中的一个状态点，使得我们可以将事务回滚至特定的点，而不是将整个事务都撤销。

begin; 
 INSERT test(id,username) VALUES(1,'admin');
 INSERT test(id,username) VALUES(2,'001');
 INSERT test(id,username) VALUES(3,'002');
 ROLLBACK;
commit;
SELECT * FROM test;

--回滚部分数据
BEGIN;
	INSERT test(id,username) VALUES(1,'admin');
	savepoint point_1;
	INSERT test(id,username) VALUES(2,'admin');
	savepoint point_2;
	INSERT test(id,username) VALUES(3,'admin');
	savepoint point_3;
	-- 3条记录
	SELECT * FROM test;
	-- rollback 到第2条
	rollback to point_2;
	-- 删除 
	release savepoint point_1;
commit; 
SELECT * FROM test;
```

###### MVCC(多并发版本控制)

Multi-Version Concurrency Control,多版本并发控制。MVCC实现的是读已提交（RC）、可重复读（RR），这2个隔离级别的并发控制。

原理：**通过redo log，记录事务ID，每次select（RC）或者开始事务（RP）的时候都会生成一个readview，会记录当前事务ID、最大事务ID、最小事务ID以及所有的事务ID列表。**

ReadView：快照读，就是最普通的select查询语句

当前读：insert、update、delete、select .. for update

原子性—undo log

持久性—redo log

隔离性—MVCC（RC、RP）和加锁（串行化）

**ReadView**

RC：每次select会生成一个readview

RP：每个事务生成一个readview

m_ids：表示在生成readview时，当前系统中，活跃的事务ID列表，即没有进行commit的事务。

min_trx_id：表示在生成readview时，当前系统中活跃的事务中，最小的事务ID，就是m_ids中的最小值

max_trx_id：表示在生成readview时，系统应该分配给下一个事务的ID

creator_trx_id：表示在生成readview时，创建给自己的事务id

判断redo log版本链中哪个版本可用

trx_id=creator_trx_id  可以访问这个版本，即访问的是自己的事务

trx_id<min_trx_id 可以访问这个版本,即访问的是已经commit的事务

trx_id>max_trx_id 不可以访问这个版本,即访问下一个事务ID，版本链中还没有

min_trx_id <=trx_id<=max_trx_id  如果事务ID在事务ID列表中有，代表是没有commit的事务，不能访问，反之则可以

##### MVCC如何实现事务的隔离级别

MVCC实现的是读已提交（RC）、可重复读（RR），这2个隔离级别的并发控制

基于redo log版本链，每个版本链会记录数据、事务ID（tx_id）、回滚指针(roll_pointer)指向上一个版本

MVCC为每个事务都创建一个可见的快照，让事务能够在读取数据时看到一致的快照，不会受到其他并发事务的影响。核心思想是：

**多版本并发控制（MVCC）** 在一定程度上实现了**读写并发**，它只在**读已提交（READ COMMITTED）**和**可重复读（REPEATABLE READ）** 和两个隔离级别下工作。其他两个隔离级别都和 MVCC 不兼容，因为 **未提交读（READ UNCOMMITTED）**，总是读取最新的数据行，而不是符合当前事务版本的数据行。而 **可串行化（SERIALIZABLE）** 则会对所有读取的行都加锁。

**事务版本号**

每开启一个事务，我们都会从数据库中获得一个事务 ID（也就是事务版本号），这个事务 ID 是自增长的，通过 ID 大小，我们就可以判断事务的时间顺序。

#### 应用场景

1. **资金交易：** 在金融系统中，资金的转账和交易需要使用事务，以确保原子性和一致性。
2. **订单处理：** 在电商平台中，处理订单的创建、付款、发货等操作需要使用事务来保证数据的完整性。
3. **库存管理：** 在仓储系统中，对商品库存的增减和库存记录的更新通常需要事务支持。
4. **预订系统：** 在酒店、航空等预订系统中，保证座位和房间的一致性、原子性是关键。

#### 示例

```sql
-- 开启事务
START TRANSACTION;

-- 执行更新操作
UPDATE accounts SET balance = balance - 100 WHERE account_id = 'A';
UPDATE accounts SET balance = balance + 100 WHERE account_id = 'B';

-- 提交事务
COMMIT;

```

#### 事务执行流程

```ASN.1
  +---------------------------+
  | 1. 事务开始 (BEGIN)     |
  +---------------------------+
               |
               v
  +---------------------------+
  | 2. 生成事务ID和TCB维护事务状态|
  +---------------------------+
               |
               v
  +---------------------------+
  | 3. 记录事务ID和开始时间到redo log事务日志|
  |    |
  +---------------------------+
               |
               v
  +---------------------------+
  | 4. 执行SQL语句              |
  |    a. 查询库存 (FOR UPDATE)|
  |    b. 更新订单状态          |
  |    c. 扣除库存              |
  |    d. 更新订单状态为完成    |
  +---------------------------+
               |
               v
  +---------------------------+
  | 5. 生成Undo日志，记录旧数据的副本, |
  |    为了回滚时，能够恢复旧数据     |
  +---------------------------+
               |
               v
  +---------------------------+
  | 6. 提交事务 (COMMIT)       |
  +---------------------------+
               |
               v
  +---------------------------+
  | 7. 持久化事务日志到磁盘   |
  +---------------------------+
               |
               v
  +---------------------------+
  | 8. 释放锁              |
  +---------------------------+
               |
               v
  +---------------------------+
  | 9. 通知用户事务成功结束    |
  +---------------------------+
               |
               v
  +---------------------------+
  | 10. 用户可回滚事务         |
  +---------------------------+
               |
               v
  +---------------------------+
  | 11. 应用Undo日志           |
  +---------------------------+
               |
               v
  +---------------------------+
  | 12. 结束事务               |
  +---------------------------+
```

### 索引

**索引是排好序的数据结构**，创建一个索引=创建一个B+树。

聚集索引并不是一种单独的索引类型，而是一种数据存储方式。对于InnoDB，一张表最多只有一个聚集索引，可以有多个非聚集索引（辅助索引）

建表的时候都会创建一个聚集索引，每张表都有唯一的聚集索引：

- 如果主键被定义了，那么这个主键就是作为聚集索引
- 如果没有主键被定义，那么该表的第一个唯一非空索引作为聚集索引
- 如果没有主键也没有唯一索引，InnoDB 内部会生成一个隐藏的主键作为聚集索引，这个隐藏的主键是一个 6 个字节的列，该类的值会随着数据的插入自增。

在创建表添加的索引都是非聚集索引，非聚集索引就是一个为了找到聚集索引的二级索引，通过二级索引索引找到主键，再查找数据。

**page**

[MySQL :: MySQL Internals Manual :: 22.2 InnoDB Page Structure](https://dev.mysql.com/doc/internals/en/innodb-page-structure.html)

在操作系统中,一页等于4kb，等于4*1024=4096个字节，在Innodb索引中，一页等于16kb。

```sql
show global status like 'Innodb_page_size'
16384= 4 page
```

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1650682755413Mysql%E7%B4%A2%E5%BC%95.drawio.png)

<img src="D:\软件\Markdown\typora-user-images\image-20220423110309314.png" alt="image-20220423110309314" style="zoom:67%;" />

**为什么InnoDB需要使用一个自增ID**

① 这个是因为InnoDB的数据页page存放数据的时候是有序的，使用自增ID,那么每次插入的时候直接插入到最后就可以，如果不是自增的，那么Innodb在每次插入的时候都需要去遍历，比较，才能找到数据插入的位置，影响效率。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16506799364111650679935817.png" style="zoom:50%;" />

②第二点的话，如果不是主键自动的InnoDB会有页分裂的情况，如果使用自增索引的话，我们每次插入数据都是最后，那么当page满的时候，只需要在申请一个page页，把新数据放到新的page页就可以了；如果使用的是非自动，那么每次插入数据的很可能会在中间的位置，这时候就需要移动后面的数据，并且把部分数据迁移到新的页中，增加了很多开销。

**缺点**

并发插入，可能会导致竞争。

解决：

①使用InnoDB的参数，来设置

InnoDB存储引擎提供了一个参数innodb_autoinc_lock_mode来控制自增长的模式，该参数的默认值为1。

索引是一种查询机制，能够提高查询的效率，但是使用索引本身会占用磁盘，也会增加维护成本，同时在更新或者删除的时候会降低效率，因为它还需要去更新索引文件。所以，如果表的数据量大或者查询比较频繁的话，比较适合使用索引。

```sql
-- 查看索引 mysql
show index from table_name
--create index mysql
DROP TABLE log;
CREATE TABLE log(
	ID INT PRIMARY KEY NOT NULL,
	username VARCHAR ( 64 ),
	`operation` VARCHAR ( 64 ),
	ip VARCHAR ( 256 ),
	request_url VARCHAR ( 64 ),
	create_time TIMESTAMP ,
  unique index username_index ( username DESC) 
);
-- 删除索引
ALTER TABLE log DROP INDEX username_index
-- pg 
DROP TABLE dev.log;
CREATE TABLE dev.log(
	ID INT PRIMARY KEY NOT NULL,
	username VARCHAR ( 64 ),
	"operation" VARCHAR ( 64 ),
	ip VARCHAR ( 256 ),
	request_url VARCHAR ( 64 ),
	create_time TIMESTAMP 
);
CREATE UNIQUE INDEX username_index ON dev.log( username DESC );
CREATE INDEX ip_index ON dev.log( ip )
-- 删除索引,不同表的索引名也不能重复，索引名是全局唯一的
DROP INDEX username_index

```

返回数据，各主要参数说明如下：

| 参数         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| Table        | 表示创建索引的数据表名，这里是 tb_stu_info2 数据表。         |
| Non_unique   | 表示该索引是否是唯一索引。若不是唯一索引，则该列的值为 1；若是唯一索引，则该列的值为 0。 |
| Key_name     | 表示索引的名称。                                             |
| Seq_in_index | 表示该列在索引中的位置，如果索引是单列的，则该列的值为 1；如果索引是组合索引，则该列的值为每列在索引定义中的顺序。 |
| Column_name  | 表示定义索引的列字段。                                       |
| Collation    | 表示列以何种顺序存储在索引中。在 MySQL 中，升序显示值“A”（升序），若显示为 NULL，则表示无分类。 |
| Cardinality  | 索引中唯一值数目的估计值。基数根据被存储为整数的统计数据计数，所以即使对于小型表，该值也没有必要是精确的。基数越大，当进行联合时，MySQL 使用该索引的机会就越大。 |
| Sub_part     | 表示列中被编入索引的字符的数量。若列只是部分被编入索引，则该列的值为被编入索引的字符的数目；若整列被编入索引，则该列的值为 NULL。 |
| Packed       | 指示关键字如何被压缩。若没有被压缩，值为 NULL。              |
| Null         | 用于显示索引列中是否包含 NULL。若列含有 NULL，该列的值为 YES。若没有，则该列的值为 NO。 |
| Index_type   | 显示索引使用的类型和方法（BTREE、FULLTEXT、HASH、RTREE）。   |
| Comment      | 显示评注。                                                   |

可分为：

* 单值索引
* 唯一索引
* 复合索引

#### 索引分类

索引的数据结构主要是哈希索引和B + Tree索引，对应哈希索引来说，底层的数据结构就是哈希表，在查询单条记录的时候，可以选择哈希索引，查询较快。

* B+ Tree：包含2种节点：索引节点和叶子节点，索引节点存储索引，叶子节点用来存储数据
  * 优点：不需要进行全表扫描，只需要对树进行搜索，所以查找速度较快

####  B Tree 和 B + Tree

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507646272591650764626738.png" style="zoom:50%;" />

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507646742591650764673498.png" style="zoom:50%;" />

**树的高度=3，单表大约能存2千万数据量**

1、2层：假设索引是bigint，那么占8个字节，data：索引、指针索引（ 指向下一层节点的指针地址） 6b

(16*1024)/(8+6)=1170

2层：假设1条数据占1kb 1024个字节 那么可以存16条数据

1170*1170* 16=2千万

**聚集索引和非聚集索引**

MyISam的数据和索引是分开存储的，它是非聚聚的;InnoDB的数据和索引存在一起，它是聚集的。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507871251831650787124408.png" style="zoom:50%;" />

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1650808582526image-20220424214904146.png" style="zoom:50%;" />

##### **B+树**

B+树是由二叉树和B树演化过来的，是一个多路平衡树，二叉树：只有一个根节点，父节点下只能有2个节点，**左小右大（从小到大的顺序）**

### 锁

按粒度分类：行锁、表锁、全局锁。

表锁和行锁的区别：
表锁：开销小，加锁快；不会出现死锁；锁定粒度⼤，发⽣锁冲突的概率最⾼，并发度最低；
行锁：开销大，加锁慢；会出现死锁；锁定粒度最⼩，发⽣锁冲突的概率最低，并发度也最⾼；

意向锁

因为锁的粒度不同，表锁的范围覆盖了行锁的范围，所以表锁和行锁会产生冲突，例如事务A对表中某一行数据加了行锁，然后事务B想加表锁，正常来说是应该要冲突的。如果只有行锁的话，要判断是否冲突就得遍历每一行数据了，这样的效率实在不高，因此我们就有了意向表锁。

意向锁的主要目的是为了使得 **行锁** 和 **表锁** 共存，事务在申请行锁前，必须先申请表的意向锁，成功后再申请行锁。注意：申请意向锁的动作是数据库完成的，不需要开发者来申请。

意向锁是表锁，但是却表示事务正在读或写某一行记录，而不是整个表， 所以意向锁之间不会产生冲突，真正的冲突在加行锁时检查。

意向锁分为意向读锁(IS)和意向写锁(IX)。

按功能分类：共享锁、排它锁

**（1）读写锁**

* 排它锁（Exclusive），简写为 X 锁，又称写锁

* 共享锁（Shared），简写为 S 锁，又称读锁。

有以下两个规定：

* 一个事务对数据对象 A 加了 X 锁，就可以对 A 进行读取和更新。加锁期间其它事务不能对 A 加任何锁。 

* 一个事务对数据对象 A 加了 S 锁，可以对 A 进行读取操作，但是不能进行更新操作。加锁期间其它事务能对 A 加 S 锁，但是不能加 X 锁。

按实现方式分类：悲观锁和乐观锁

## 常见问题

### **为什么不使用红黑树**

红黑树是二叉树，如果数据大了，树的高度很高，导致查询会很深，这样IO会很高

### **B树和B+树的区别**

① B+树只有叶子节点才存储数据，其它节点只存索引key（例如主键索引ID的值）,B树的数据和索引是不做区分的。（即每个节点将索引和data都做了存储）。

② B+树的叶子节点是有指针的，指向下一个叶子节点，B树没有

### **为什么InnoDB必须建主键，而且是自增的**

因为Mysql默认使用的InnoDB存储引擎，对于InnoDB，每张表，有且只有一个聚集索引，聚集索引的创建是有规则的。

**如果我们的表有主键，那么就会使用主键作为聚集索引，如果我们没有创建主键，那么，首先InnoDB会寻找一个唯一非空索列作为聚集索引，如果没有找到，那么，它会新建一个隐藏的自增列作为作为索引，这样是比较消耗资源的。**

至于**使用自增，是为了减少页裂，因为InnoDB是B+Tree的数据结构，B+Tree是有序的，**而对于聚集索引，它的数据在磁盘存放顺序和索引顺序是一致的，同时它又是以页的方式，进行存储，每页16Kb。这样的话：

如果主键是递增的，那么每次数据插入只需要插入到最后就可以了，如果当前页满了，那么只需要再申请一页就可以；

如果它不是递增的，那么每次数据插入大概率会插入在中间，这样就需要移动数据，如果数据满了，那么还需要移动数据到新的一页，这样做会严重影响插入效率。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507888101281650788809236.png" style="zoom:67%;" />

### 什么是存储过程

存储过程是一系列Sql的合集，能够预编译到数据库中，优势是它会加快sql的执行速度，但是同时比较难以调试，可移植性也差，造成sql和业务耦合。

### 什么是覆盖索引

创建一个索引，该索引包含查询中用到的所有字段，称为“覆盖索引”。或者称为索引覆盖；使用覆盖索引可以避免innoDB引擎回表查询

### Mysql优化

优化的话有几个方面：SQL、表设计、硬件

（1）SQL和索引优化

（2）表设计优化

（3）硬件优化

### 页面查询慢怎么办

①开启慢日志，去看哪些SQL查询慢

```sql
set global slow_query_log='ON'; 
```

②使用explain查看慢SQL

看是否建立了索引,如果没有创建索引，那么就创建索引

如果有索引，就看有没有走索引，防止索引失效

③使用**show profile**查看SQL的性能

### 单表数据量太大，查询变慢，如何解决

解决方案：分库分表。Apache的ShardingSphere。

ShardingSphere

有JDBC、Proxy、Sidecar 3个产品。

ShardingSphere-JDBC

ShardingSphere-Proxy

**分表**

原因：单表数据量到达瓶颈

水平拆分

表结构不发生改变，根据数据拆分

垂直拆分

根据字段拆分

**分库**

原因：单库数据量到达瓶颈

带来的问题

分布式事务

联合查询困难

https://developpaper.com/mysql-memory-architecture-and-index-description/)

#### 7. 数据库设计

数据库设计通常分为6个阶段：需求分析—>概念模型（E-R图）—>逻辑模型（表）—>物理模型

① 需求分析：分析用户的需求，包括数据、功能和性能需求；

② 概念结构设计：主要采用E-R模型进行设计，包括画E-R图；

③ 逻辑结构设计：通过将E-R图转换成表，实现从E-R模型到关系模型的转换；

④ 数据库物理设计：主要是为所设计的数据库选择合适的存储结构和存取路径；

⑤ 数据库的实施：包括编程、测试和试运行；6数据库运行与维护：系统的运行与数据库的日常维护。

#### 8. SQL优化

##### 1. SQL优化的原理是什么?

减少访问数据库的次数，减少访问数据量和返回数据量

##### 2. Sql执行慢怎么排查

```sql

SET profiling = 1;
SELECT @@profiling;
SELECT * FROM paas_flow_log

-- 查询耗时
SHOW PROFILES;

-- 查询资源消耗 Query_id 142
show profile cpu,block io for query 163
-- 关闭
SET profiling = 0;
```

##### 3. 优化原则

###### 1. 最左匹配原则

是针对联合索引的。

在a,b,c三个字段上建立一个联合索引，我们可以选择自己想要的优先级，(a、b、c)

比如索引abc_index:(a,b,c)是a,b,c三个字段的联合索引，下列sql执行时都无法命中索引abc_index：

```sql
select * from table where c = '1';

select * from table where b ='1' and c ='2';
```

以下三种情况却会走索引:

```sql
select * from table where a = '1';

select * from table where a = '1' and b = '2';

select * from table where a = '1' and b = '2'  and c='3';
```

原理：

B+Tree是有序的

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16508088715251650808871399.png)   

###### 2.索引下推

索引下推（index condition pushdown ）简称ICP。


## 二、SQL

### **DB/table SQL**

```sql 
#创建库
CREATE DATABASE test;
#切换库
USE test;
#创建表
USE    test;
CREATE TABLE user(
   id int not null AUTO_INCREMENT,
  name varchar(64) null,
   primary key (`id`)
)
#修改表
use test;
#增加字段
alter table user   add age int(10)

use test;
#删除字段
alter table user   drop column  age 

use test;
#删除表
drop table test;
```

#### Select

##### 条件查询

```sql
语法：
SELECT 要查询的东西
【FROM 表名】;
要查询的东西 可以是常量值、可以是表达式、可以是字段、可以是函数
语法：
select 
    要查询的字段|表达式|常量值|函数
from 
    表
where 
    条件 ;
```

##### 排序查询

```sql
语法：
select
    要查询的东西
from
    表
where 
    条件
    order by 排序的字段|表达式|函数|别名 【asc|desc】
```



#### 函数

```sql
1、字符函数
  concat拼接
  substr截取子串
  upper转换成大写
  lower转换成小写
  trim去前后指定的空格和字符
  ltrim去左边空格
  rtrim去右边空格
  replace替换
  lpad左填充
  rpad右填充
  instr返回子串第一次出现的索引
  length 获取字节个数

2、数学函数
  round 四舍五入
  rand 随机数
  floor向下取整
  ceil向上取整
  mod取余
  truncate截断
3、日期函数
  now当前系统日期+时间
  curdate当前系统日期
  curtime当前系统时间
  str_to_date 将字符转换成日期
  date_format将日期转换成字符
4、流程控制函数
  if 处理双分支
  case语句 处理多分支
    情况1：处理等值判断
    情况2：处理条件判断

5、其他函数
  version版本
  database当前库
  user当前连接用户 
```

##### 分组函数

sum 求和
max 最大值
min 最小值
avg 平均值

count 计数

特点：
1、以上五个分组函数都忽略null值，除了count(*)
2、sum和avg一般用于处理数值型
    max、min、count可以处理任何数据类型
3、都可以搭配distinct使用，用于统计去重后的结果
4、count的参数可以支持：
    字段、*、常量值，一般放1

   建议使用 count(*)



#### 分组查询

```sql

GROUP BY 
例：按照area地区进行分类，查看每一类数据的和，平均数、最大值等
SELECT area,AVG(env_id),SUM(env_id),MAX(env_id) FROM eip_app WHERE del_flag=0 
GROUP BY area

having 对分组的结果，进行筛选

SELECT area,AVG(env_id),SUM(env_id),MAX(env_id),COUNT(env_id) FROM eip_app WHERE del_flag=0 GROUP BY area HAVING SUM(env_id) >3 

筛选出 SUM(env_id) >3 
```



#### 多表查询

```sql
#连接查询

select 字段，...
from 表1
【inner|left outer|right outer|cross】join 表2 on 连接条件
【inner|left outer|right outer|cross】join 表3 on 连接条件
【where 筛选条件】
【group by 分组字段】
【having 分组后的筛选条件】
【order by 排序的字段或表达式】

例：
SELECT eip_app.id,eip_app.`name`,eip_app_limit_policy.type FROM eip_app inner JOIN eip_app_limit_policy ON  eip_app.id=eip_app_limit_policy.app_id WHERE eip_app.del_flag=0

内连接：只返回两个表中连接字段相等的行
左连接：返回包括左表中的所有记录 + 内连接匹配到数据
右连接：返回包括右表中的所有记录 + 内连接匹配到数据
全外连接： 左、右表+内连接匹配数据
```

##### 子查询

在一个查询sql中有嵌套了一个查询语句，一个sql的条件是另一个sql查询出来的结果

**子查询可以返回的数据类型一共分为四种：**

   1. 单行单列：返回的是一个具体列的内容，可以理解为一个单值数据；
   2. 单行多列：返回一行数据中多个列的内容；
   3. 多行单列：返回多行记录之中同一列的内容，相当于给出了一个操作范围；
   4. 多行多列：查询返回的结果是一张临时表；

```sql
SELECT column_name [, column_name ]
FROM   table1 [, table2 ]
WHERE  column_name OPERATOR
      (SELECT column_name [, column_name ]
      FROM table1 [, table2 ]
      [WHERE])

SELECT name FROM eip_app WHERE id IN
( SELECT app_id FROM eip_app_limit_policy WHERE `status`=1)

SELECT *
FROM emp e
WHERE e.job=(
  SELECT job
  FROM emp 
  WHERE ename='ALLEN') 
  AND e.sal>(
  SELECT sal
  FROM emp 
  WHERE empno=7521);

自连接
SELECT e1.name
FROM employee AS e1 INNER JOIN employee AS e2
ON e1.department = e2.department
      AND e2.name = "Jim";
```

##### 分页查询

```sql
select 字段|表达式,...
from 表
【where 条件】
【group by 分组字段】
【having 条件】
【order by 排序的字段】
limit 【起始行（从0开始），】返回的总行数;
```

##### Mysql索引介绍一下

答：主要B+树索引和Hash索引

B+树

* 多路平衡树
* 有序的
* 叶子节点(终端节点)用来存放数据，非叶子节点用来存放索引和子节点的指针地址
* 能够范围查找
* innodb的默认索引

Hash索引

* hash表
* 无序的
* 精确查找，不支持排序、范围查找和模糊查询。

##### 2、索引优缺点

答：优点：提高查询速度，**降低IO成本**；对于分组和排序的SQL,能够降低降低它们的成本，提高速度；对于外键索引，能够加快2个表之间的连接

缺点：索引虽然提高了查询成本，但是降低了修改、删除的效率，因为增加了索引，就需要维护数据和索引直接的关系。

##### 3、什么情况下适合使用索引

答：表的数据比较多，或者这张表查询比较频繁；

频繁作为查询条件的字段（where后面的字段），多表关连的字段（join on的字段），排序、分组的字段

##### 4、什么情况下不适合使用索引

答：数据太少；频繁更新的字段；

##### 5、什么情况下索引失效

模型数空运最快

模：模糊查询like %放在最前面，例`SELECT id FROM `user` WHERE user_name LIKE '%王'`

型：类型不匹配，比如索引列是varchar，条件传了int

数：对索引列用了内部函数

空：索引列是NULL

最：联合索引没有按照最做匹配原则

快：如果全表扫描查询预计比索引要快

答：（1）模糊查询like用第一个字符用%匹配的时候（2）在索引列做函数计算的时候（3）使用 不等于、大于、小于也会导致失效，（4）查询条件有or的时候（or前后2个查询条件的字段有一个没有索引，那么索引就失效）（5）类型不一致（索引列的类型和传入的查询词不一样，比如索引类是varchar，条件传了一个int）

##### 6、为什么InnoDB建议手动创建主键索引

答：虽然我们创建表可以不主动创建索引，数据库也会帮我们创建聚簇（**cù**）索引，但是我们自己不去创建，而是让数据库再去帮我们维护的，是比较消耗数据库资源的。

##### 7、索引的种类

答：主键索引、普通索引、唯一索引、复合索引、全文索引、空间索引

补充：

普通索引：一个索引对应一个列

唯一索引：索引列的值是唯一的，可以是null，但是只能有一个

复合索引：多个列构成一个索引

全文索引：它的是类型fulltext,索引列的值支持全文查找，可以在CHAR、VARCHAR、TEXT类型(文本类型)的列上创建。

##### 8、主键索引和非主键索引的区别

答：主键索引会直接根据主键去查，非主键会先查询查询条件的索引字段，得到主对应的主键索引的值，再去查，也就是回表

补充：

如果查询语句是 select * from table where ID = 100,即主键查询的方式，则只需要搜索 ID 这棵 B+树。

如果查询语句是 select * from table where name = '小明'，即非主键的查询方式，则先搜索name索引树，得到ID=100,再到ID索引树搜索一次，这个过程也被称为回表。

##### 9、主键和唯一索引的区别

答：主键索引只能有1个，唯一索引可以有多个；主键索引不能为空，唯一索引可以；主键索引可以被其它的表设为外键，唯一索引不可以；

补充：

可以有多个列组成一个主键索引。例如： PRIMARY KEY (`id`, `name`) USING BTREE

##### 10、索引是越多越好吗

答：不是，因为创建和维护索引需要消耗数据库资源，增加管理成本，而且，索引是针对查询的，索引越多，更新速度也会越慢

##### 11、为什么建议使用自增索引

答:自增索引的话，可以避免页分裂，Mysql的底层数据结构是B+Tree，它是有序的，如果不是自增的话，它就会往中间节点插入数据，如果恰好这个节点数据满了的话，它还会分裂节点，如果是自增的话，那么只需要往最后面插入数据就可以了。

补充：索引其实就是一颗 B+ 树，一个表有多少个索引就会有多少颗 B+ 树，mysql 中的数据都是按顺序保存在 B+ 树上的（所以说索引本身是有序的）。

**12、唯一索引和普通索引哪个性能更好？**

答：查询的是差不多的，更新数据的话，因为唯一索引会做一个唯一性校验，要慢一点

##### 13、从innodb的索引结构分析，为什么索引的key长度不能太长?

答：因为索引也会被存储起来，索引太长了，就会导致一页里面存放的索引变少，导致数据也变多，最后导致查询效率变慢

##### 14、Mysql的存储过程和存储函数

答：存储过程和存储函数的话就是在数据库里面定义一些SQL语句，这些SQL的话被事先预编译好存到数据库，之后我们就可以通过名称和参数去调用它，使用它话可以提高执行速度，不需要每次都次都去编译SQL，还有减少网络开销

##### 15、SQL优化有了解吗

答：SQL优化的话可以通过查询语句进行优化，比如：

* 查询的时候只查询需要的字段，而不用select *查询所有的

* 能够一次查询到信息，尽量一次查结束，避免查多次，增加数据库负担

* 然后对于表字段过多的，可以考虑拆表，简化它的表结构

* **避免在where子句中使用函数**，因为使用函数可能导致索引失效，从而全表扫描
* **避免使用SELECT DISTINCT：** DISTINCT操作会对结果集进行排序和去重，**可能导致性能问题**。在确实需要去重时，可以考虑其他方式，如使用GROUP BY。

* **使用合适的数据类型：** 不要使用过大或者不必要的数据类型
* **分页优化：** 对于需要分页的查询，使用`LIMIT`和`OFFSET`而不是获取全部数据后再进行分页操作。

* 多表连接的话，最好不超过3张表，太多的话编译过慢，查询速度降低

* 如果数据量很大的话，可以考虑做读写分离，以及分库分表

##### 16、Mysql锁有了解吗

答：Mysql有表锁、行锁；读锁、写锁

补充：

表锁：操作的时候，会锁住整张表

行锁：操作的时候，锁住整行

读锁:是共享锁，多个读的操作相互不影响

写锁：是排它锁，在写入数据的时候，其它写入和读取的的操作都不能进行

InnoDB 与 MyISAM 的最大不同有两点：一是支持事务；二是 采用了行级锁。

##### 17、事务的特性

答：事务就是一组SQL，它们有4个特性，**原子性**，操作要么都执行了，那么都没执行；**一致性**，事务执行前后，数据状态都是一致性的状态；**隔离性**，事务与事务之间相互隔离；**持久性**，事务提交之后，数据就会永久性的保存。

补充：

一致性：

所谓一致性是数据库处理前后结果应与其所抽象的客观世界中真实状况保持一致。这种一致性是一种需要管理员去定义的规则。管理员如何指定规则，数据库就严格按照这种规则去处理数据。

事务的隔离级别：用来解决事务并发出现的问题

- （read uncommitted） 读未提交，在一个事务中能读取到另一个未提交事务的操作结果
- （read committed） 读已提交，在一个事务中能读取到另一个已提交事务的操作结果
- （read repeatable） 可重复读，在一个事务内，多次读取的数据应该是一个一致的状态。
- （serializable） 串行，你可以理解事务时排队执行的，没有并发

| 隔离级别                            | 丢失更新 | 脏读 | 不可重复读 | 幻读 |
| ----------------------------------- | -------- | ---- | ---------- | ---- |
| Read uncommitted（读未提交）        | ×        | √    | √          | √    |
| Read committed（读已提交）          | ×        | ×    | √          | √    |
| Repeatable read（默认）（可重复读） | ×        | ×    | ×          | √    |
| Serializable（串行化）              | ×        | ×    | ×          | ×    |

备注 ： √ 代表可能出现 ， × 代表不会出现 

脏读：读取了未提交的数据-解决它使用读已提交

不可重读：前后多次读取，数据内容不一致-解决它使用可重复读，针对的是update

幻读：前后多次读取，count数据总量不一样-解决它使用串行化,针对的是insert和delete

```java
 @Transactional(rollbackFor = Exception.class,isolation = Isolation.DEFAULT)
​```

##### 18、数据库锁

数据库是一个多用户使用的共享资源。当多个用户并发地存取数据时，在数据库中就会产生多个事务同时存取同一数据的情况。若对并发操作不加控制就可能会读取和存储不正确的数据，破坏数据库的一致性。

加锁是实现数据库并发控制的一个非常重要的技术。当事务在对某个数据对象进行操作前，先向系统发出请求，对其加锁。加锁后事务就对该数据对象有了一定的控制，在该事务释放锁之前，其他的事务不能对此数据对象进行更新操作。

基本锁类型：**行级锁、表级锁**

##### 19、Mybait如何实现乐观锁

答：

- 取出记录时，获取当前 **version**
- 在更新时，带上这个 **version**，即 **set version = newVersion where version = oldVersion**
- 如果 **version** 不对，就更新失败

```
```sql
 <update id="updateDeposit" keyProperty="id"  parameterType="com.cloud.demo.model.Account">
        update account
        set deposit=#{deposit},
            version = version + 1
        where id = #{id}
          and version = #{version}
    </update> <update id="updateDeposit" keyProperty="id" parameterType="com.cloud.demo.model.Account">
        update account
        set deposit=#{deposit},
            version = version + 1
        where id = #{id}
          and version = #{version}
    </update>
```

```

##### 20、如果该表存在多个字段查询频繁，是该建立多个单列索引还是创建一个多列联合索引呢？

答：

##### 21、多条数据怎么查重

答：用GROUP BY 分组去查。

​```sqlite
SELECT "name" , COUNT(1) FROM "t_neop_record_day" GROUP BY name 
HAVING       (COUNT(1) > 1)
```

## 三、MyBatis

##### 1、介绍一下Mybatis

答：它是一个半自动ORM对象关系映射框架，作用在持久层。我们自己在xml文件可以写Sql，灵活度高

优点是：解除了sql和代码的耦合

补充：

物理分页：通过SQL语句关键字limit

逻辑分页：数据库返回所有数据，代码中自己分页

物理分页多次访问数据库，数据库压力大；逻辑分页一次返回全部数据，内存消耗大。

逻辑分页一次性将数据读取到内存，数据发生改变，数据库的最新状态不能实时反映到操作中，实时性差。物理分页每次需要数据时都访问数据库，能够获取数据库的最新状态，实时性强。

三层架构

持久层：用DAO模式，建立实体类和数据库表映射（ORM映射）。也就是哪个类对应哪个表，哪个属性对应哪个列。持久层  的目的就是，完成对象数据和关系数据的转换。

业务层：采用事务脚本模式。将一个业务中所有的操作封装成一个方法，同时保证方法中所有的数据库更新操作，即保证同时成 功或同时失败。避免部分成功部分失败引起的数据混乱操作。

表现层：采用MVC模式。

M称为模型，也就是实体类。用于数据的封装和数据的传输。

V为视图，也就是GUI组件，用于数据的展示。

C为控制，也就是事件，用于流程的控制

设计原则：

* 业务层接口的设计原则：一个实体类一个接口，一次提交一个业务方法。业务方法的参数来自表现层。

* 持久层接口的设计原则：一个实体类一个接口，一次数据库操作一个持久方法。

##### 2、同一个命令空间下可以有2个ID相同的sql吗

答：不可以，会报错，不同的namespace代表着不同的DAO,那么ID是可以重复的

##### 3、模糊查询%怎么处理

答：用concat函数，或者bind标签

补充：

```xml
<select id="pageList" parameterType="java.util.HashMap" resultMap="XxlJobInfo">
      SELECT <include refid="Base_Column_List" />
      FROM xxl_job_info AS t
      <trim prefix="WHERE" prefixOverrides="AND | OR" >
         <if test="jobGroup gt 0">
            AND t.job_group = #{jobGroup}
         </if>
            <if test="triggerStatus gte 0">
                AND t.trigger_status = #{triggerStatus}
            </if>
         <if test="jobDesc != null and jobDesc != ''">
            AND t.job_desc like CONCAT(CONCAT('%', #{jobDesc}), '%')
         </if>
         <if test="executorHandler != null and executorHandler != ''">
            AND t.executor_handler like CONCAT(CONCAT('%', #{executorHandler}), '%')
         </if>
         <if test="author != null and author != ''">
            AND t.author like CONCAT(CONCAT('%', #{author}), '%')
         </if>
      </trim>
      ORDER BY id DESC
      LIMIT #{offset}, #{pagesize}
   </select>
```

```xml
<select id="listUserLikeUsername" resultType="com.jourwon.pojo.User">
　　<bind name="pattern" value="'%' + username + '%'" />
　　select id,sex,age,username,password from person where username LIKE #{pattern}
</select>
```

##### 4、怎么使用多个参数

答:用Parma注解，或者Map传参,或者直接传实体类

补充：

```java
public User selectUser(@Param("userName") String name, int @Param("deptId") deptId);
```

```xml
<select id="selectUser" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select>
```

```java
public User selectUser(Map<String, Object> params);
```

```xml
<select id="selectUser" parameterType="java.util.Map" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select>

```

<select id="selectUser" parameterType="com.jourwon.pojo.User" resultMap="UserResultMap">
    select * from user
    where user_name = #{userName} and dept_id = #{deptId}
</select>


##### 5、include标签

答:是用来复用其它标签的，它和sql标签是一起用的，select标签里，再用select关键字去导入include标签，然后include标签引用sql

```xml
  <sql id="Base_Column_List">
    id, registry_value, manage_addr, service_id, create_time, instance_work_path,
    ins_status, collector_id, collector_ins_id, area_code, domain, vendor, DATA_TYPE, 
    ins_protocols,heartbeat_time
  </sql>
```



```xml
<select id="selectByPrimaryKey" parameterType="java.lang.Integer" resultMap="BaseResultMap">
select 
<include refid="Base_Column_List" />
from t_coll_service_instance
where id = #{id,jdbcType=INTEGER}
  </select>
```



##### 6、foreach标签

答：它是一般用在in查询里面的。用来遍历一个集合

补充：



```java
List<TcollCriterionDefine> selectByPrimaryKeys(@Param("ids") List<Integer> ids);
```

```xml
  <select id="selectByPrimaryKeys" resultMap="BaseResultMap">
    select * from t_coll_criterion_define cd
    <where>
      //注意：此处不能写list!=''要写成list.size()>0,不然会报错
      <if test="ids != null and ids.size()>0">
        cd.id IN
        <foreach item="id" index="index" collection="ids" open="("  close=")" separator=",">
          #{id}
        </foreach>
      </if>
    </where>
  </select>
```




item　　表示集合中每一个元素进行迭代时的别名，随便起的变量名；
index　　指定一个名字，用于表示在迭代过程中，每次迭代到的位置，不常用；
open　　表示该语句以什么开始，常用“(”；
separator表示在每次进行迭代之间以什么符号作为分隔符，常用“,”；
close　　表示以什么结束，常用“)”。
​```

##### 7、当实体类中的属性名和表中的字段名不一样 ，怎么办

答：用resultMap来映射，或者在Sql语句里面，自定义字段别名

补充：

```xml
<select id="getOrder" parameterType="int" resultMap="orderResultMap">
   select * from orders where order_id=#{id}
</select>
```

```xml
<resultMap type="com.jourwon.pojo.Order" id="orderResultMap">
    <!–用id属性来映射主键字段–>
    <id property="id" column="order_id">
<!–用result属性来映射非主键字段，property为实体类属性名，column为数据库表中的属性–>
<result property ="orderno" column ="order_no"/>
<result property="price" column="order_price" />
</reslutMap>
```
##### 8、#和$符号的区别

答：#是预编译处理，会将#里面的值换成？，能够预防Sql注入，**$只是字符替换。**不能防止sql注入

##### 9、一对一关联查询、一对多关联查询怎么实现

答：支持，可以使用联合查询或者嵌套查询，通过在resultMap标签里面配置association和collection节点。

补充：association指的就是一对一，collection指的就是一对多查询。

##### 10、有哪些动态SQL

答：一共是9种。if、where、choose、foreach、when、trim、bind、set

##### 11、一级和二级缓存支持吗

答：支持。

一级缓存，默认开启，它的作用域是同一个SQLSession，Mybatis会根据方法名和参数作为KEY,将结果存到本地缓存里面（不同SqlSession之间缓存是相互隔离的），即先去查缓存，查不到再去查sql。SQLSession执行insert、update、delete操作会清空sqlSession缓存

二级缓存，需要手动开启，它的作用域是SqlSessionFactory，也就是同一个namespace命名空间下的sql，

```yaml
#开启二级缓存
mybatis.configuration.cache-enabled=true
```

一级缓存：同一个 SqlSession 对象， 在参数和 SQL 完全一样的情况先， 只执行一次 SQL 语句（如果缓存没有过期），第二次直接从内存里面取结果

##### 12、延迟加载

答：支持,collection，association支持延迟加载。也需要在配置文件去开启延迟加载。` lazyLoadingEnable=ture|false`

补充：

延迟加载：按需去加载查询。

SELECT book.*,cname FROM book,category WHERE book.cid = category.cid

我们只是需要显示图书类型，点击的时候才显示该类型的图书，如果能做到开始只查询类型，点击类型的时候再查询该类型的图书，就不需要进行两表联查了，可以提高查询的效率，也比较节省内存，这就是延迟加载。

##### 13、Dao 接⼝的⼯作原理，里面的方法能重载吗？

答：不能重载，它是根据全类名和方法名xml里找对应的SQL的。Dao 接⼝的⼯作原理是 JDK 动态代理，Mybatis 运⾏时会使⽤ JDK 动态代理为 Dao 接⼝⽣成代理 proxy 对象，代理对象 proxy 会拦截接⼝⽅法，转⽽执⾏ MappedStatement 所代表的 sql，然后将 sql 执⾏结果返回。 5.2.4 Mybatis 是如何

##### 14、Mybatis的工作原理介绍一下

答：读取mybatis的配置文件，加载数据库配置信息——加载映射的xml文件——创建SqlSessionFactory会话工厂——创建sqlsession会话对象——使用Executor执行器执行SQL——得到返回结果再解析成实体对象

##### 15、数据库三大范式

答：
① 每一列都不可以再拆分
② 非主键的列要完全依赖主键
③ 非主键的列要直接依赖主键，而不能间接依赖

##### 16、事务的特征

答：

* 原子性
* 一致性
* 隔离性
* 持久性

##### 17、脏读、幻读、不可重复读了解吗

答：脏读（dirty read）：就是一个A事务即便没有提交，它对数据的修改也可以被其他事务B事务看到，**B事务读到了A事务还未提交的数据，这个数据有可能是错的**，有可能A不想提交这个数据，这只是A事务修改数据过程中的一个中间数据，但是被B事务读到了，这种行为被称作脏读，这个数据被称为脏数据

幻读（phantom read）：**A事务多次查询数据库，结果发现查询的数据条数不一样，A事务多次查询的间隔中，B事务又写入了一些符合查询条件的多条数据**（这里的写入可以是update，insert，delete），A事务再查的话，就像发生了幻觉一样，怎么突然改变了这么多，这种现象这就叫做幻读

不可重复读（non-repeatable read）：**在A事务内，多次读取同一个数据，但是读取的过程中，B事务对这个数据进行了修改，导致此数据变化了，**那么A事务再次读取的时候，数据就和第一次读取的时候不一样了，这就叫做不可重复读

解决：使用事务的隔离级别

