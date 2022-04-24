### MySQL简介  
#### 1. 什么是MySQL

它是一个关系型数据库，包含了“一对一、一对多、多对多”这些关系模型，本身是开源免费的。MySQL 5.5版本后默认的存储引擎为InnoDB。

**InnoDB和MyIsam存储引擎的区别**

| MyISam                                                    | InnoDB                                        |
| --------------------------------------------------------- | --------------------------------------------- |
| 只支持表锁                                                | 支持表锁和行锁                                |
| 不支持事务                                                | 支持事务                                      |
| 三个文件存储 一个是存储数据、一个存储索引、一个存储表结构 | 2个文件存储，一个存表结构，一个存储索引和数据 |

#### 2. 三大范式

* #### 第一范式：每一列都不可以再拆分
* 第二范式：非主键的列要完全依赖主键，不能部分依赖
* 第三范式：非主键的列直接依赖主键，不能间接依赖

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

#### 3. 索引

索引是排好序的数据结构，创建一个索引=创建一个B+树。

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

##### 索引分类

索引的数据结构主要是哈希索引和B + Tree索引，对应哈希索引来说，底层的数据结构就是哈希表，在查询单条记录的时候，可以选择哈希索引，查询较快。

* B+ Tree：包含2种节点：索引节点和叶子节点，索引节点存储索引，叶子节点用来存储数据
  * 优点：不需要进行全表扫描，只需要对树进行搜索，所以查找速度较快

##### B Tree VS B + Tree

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507646272591650764626738.png" style="zoom:50%;" />

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507646742591650764673498.png" style="zoom:50%;" />

**树的高度=3，单表大约能存2千万数据量**

1、2层：假设索引是bigint，那么占8个字节，data：索引、指针索引（ 指向下一层节点的指针地址） 6b

(16*1024)/(8+6)=1170

2层：假设1条数据占1kb 1024个字节 那么可以存16条数据

1170*1170* 16=2千万

**B树和B+树的区别：**

① B+树只有叶子节点才存储数据，其它节点只存索引key（例如主键索引ID的值）,B树的数据和索引是不做区分的。（即每个节点将索引和data都做了存储）。

② B+树的叶子节点是有指针的，指向下一个叶子节点，B树没有

**为什么InnoDB必须建主键，而且是自增的？**

因为InnoDB使用的就是聚集索引，也就是B+Tree树的结构，是由索引+data组成的，如果我们没有创建主键，那么Mysql也会自己寻找一列作为主键索引列或者新建一个隐藏列作为作为索引，这样是比较消耗资源的。

至于使用自增，是因为

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507888101281650788809236.png" style="zoom:67%;" />

**聚集索引和非聚集索引**

MyISam的数据和索引是分开存储的，它是非聚聚的;InnoDB的数据和索引存在一起，它是聚集的。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16507871251831650787124408.png" style="zoom:50%;" />

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1650808582526image-20220424214904146.png" style="zoom:50%;" />

##### B+树 多叉平衡树

B+树是由二叉树和B树演化过来的。二叉树：只有一个根节点，父节点下只能有2个节点，**左小右大（从小到大的顺序）**。



**为什么不使用红黑树**

红黑树是二叉树，如果数据大了，树的高度很高，导致查询会很深，这样IO会很高

#### 4. 什么是存储过程

存储过程是一系列Sql的合集，能够预编译到数据库中，优势是它会加快sql的执行速度，但是同时比较难以调试，可移植性也差，造成sql和业务耦合。

#### 5. 事务

事务是逻辑上的一系列操作（一组DML——sql语句），是一个逻辑单元。将数据从一种状态切换到另外一种状态。要么都成功，要么都失败，MySQL 的事务提交默认是隐式提交，每执行一条语句就把这条语句当成一个事务然后进行提交。不能回退 SELECT 语句，回退 SELECT 语句也没意义；也不能回退 CREATE 和 DROP 语句。

* 回退：撤销sql
* 提交：将未存储的sql语句结果写入数据库

###### 1.1 **事务特性** （ACID）-单点数据库

* 原子性Auomicity：一个事务中的操作要么全部完成，要么就回滚到未执行前的状态

  使用`begin`，`commit`来保证。

  数据库不报错、不宕机、正常运行就是成功，更新行数为0是数据库的正常返回结果，这在业务上是失败，在数据库层面是成功，这种情况数据库不会执行回滚，需要程序员判断更新行数，如果为0，手动回滚。

* 一致性Consistency：数据在事务执行前后，数据都是处于正确的状态，而不是在一个非预期的状态。

  **一致性是指事务是否产生非预期中间状态或结果**。比如脏读和不可重复读，产生了非预期中间状态，脏写与丢失修改则产生了非预期结果。**一致性实际上是由后面的隔离性去进一步保证的，隔离性达到要求，则可以满足一致性。**

* 隔离性Durability：事务与事务之间是相互隔离的，互不干扰的。多个事务在操作同一个数据时，事务之间相互不干扰

  使用`事务的隔离级别`来保证`高并发`下事务之间的`隔离性`

* 持久性Isolation：一个事务一旦提交，那么它的数据将会持久化到本地，以排除其它事务对它的进行修改

<img src="D:\软件\Markdown\typora-user-images\image-20201015140838220.png" alt="image-20201015140838220" style="zoom: 50%;" />

###### 1.2 如何保证事务的持久性

问：事务提交后，事务的数据还没有真正落到磁盘上，此时数据库奔溃了，事务对应的数据会不会丢？

答：事务会保证数据不会丢，当数据库奔溃后重启，它会保证：

- 成功提交的事务，数据会保存到磁盘
- 未提交的事务，相应的数据会回滚

原理：通过日志记录来实现

###### 1.3 隔离性

简单来说，隔离性就是`多个事务互不影响，感觉不到对方存在`，这个特性就是为了做并发控制。在多线程编程中，如果大家都读写同一块数据，那么久可能出现最终数据不一致，也就是每条线程都可能被别的线程影响了。按理说，最严格的隔离性实现就是完全感知不到其他并发事务的存在，多个并发事务无论如何调度，结果都与串行执行一样。为了达到串行效果，目前采用的方式一般是两阶段加锁（Two Phase Locking），但是读写都加锁效率非常低，读写之间只能排队执行，有时候为了效率，原则是可以妥协的，于是隔离性并不严格，它被分为了多种级别，**从高到低分别为**：

- ⬇️可串行化（Serializable）-脏读、幻读、不可重读度的问题
- ⬇️可重复读（Read Repeatable）-解决脏读、不可重复读
- ⬇️已提交读（Read Committed）-解决脏读
- ⬇️未提交读（Read Uncommitted）-三个一个都解决不了

事务并发执行时，隔离不足会导致的**问题**:

- ⬇️脏读
- ⬇️幻读
- ⬇️不可重复读

| 隔离级别 | 脏读 | 不可重复读 | 幻读 |
| :------: | :--: | :--------: | :--: |
| 未提交读 |  √   |     √      |  √   |
|  提交读  |  ×   |     √      |  √   |
| 可重读度 |  ×   |     ×      |  √   |
| 可串行化 |  ×   |     ×      |  ×   |

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

###### 1.4 SQL示例

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

###### **1.5 事务并发**

在并发环境下，事务的隔离性很难保证，因此会出现很多并发一致性问题。

* 丢失修改：A和B2个事务都对同一个数据进行修改，A先修改，B后修改，那么A的修改就会丢失
* 读脏数据：A先修改数据，B随后读取这个数据，假设A修改后进行了回滚，撤销了这次修改，那么B读取的就是脏数据
* 不可重读度：假设A读取一个数据，B对数据进行了修改，那么A再次读取的时候，2次读取的数据就会不一样
* 幻读：A读取某个范围的数据，比如count，B在这个范围数据重新插入数据，那么A再次读取数据就会不一样

产生原因：破坏了事务的隔离性

解决：使用事务的隔离级别

###### **1.5 DB/table SQL**

```sql
#创建库
CREATE DATABASE test;
#切换库
USE test;
#创建表
USE	test;
CREATE TABLE user(
	id int not null AUTO_INCREMENT,
  name varchar(64) null,
	primary key (`id`)
)
#修改表
use test;
#增加字段
alter table user	add age int(10)

use test;
#删除字段
alter table user	drop column  age 

use test;
#删除表
drop table test;
```

###### 1.6 Select

* 条件查询

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

* 排序查询

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

* 常见函数

  一、单行函数
  ```
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

  二、分组函数

  ```sqlite
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
  ```

* 分组查询

  ```sql
  GROUP BY 
  例：按照area地区进行分类，查看每一类数据的和，平均数、最大值等
  SELECT area,AVG(env_id),SUM(env_id),MAX(env_id) FROM eip_app WHERE del_flag=0 
  GROUP BY area
  
  having 对分组的结果，进行筛选
  
  SELECT area,AVG(env_id),SUM(env_id),MAX(env_id),COUNT(env_id) FROM eip_app WHERE del_flag=0 GROUP BY area HAVING SUM(env_id) >3 
  
  筛选出 SUM(env_id) >3 
  
  ```

* 多表查询

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

* 子查询

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
  例：  
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

* 分页查询

  ```sql
  select 字段|表达式,...
  from 表
  【where 条件】
  【group by 分组字段】
  【having 条件】
  【order by 排序的字段】
  limit 【起始行（从0开始），】返回的总行数;
  ```

#### 6. 锁

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

功能：



原理：

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16508088715251650808871399.png)