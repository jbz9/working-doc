## PostgreSQL

### 基础知识

##### 1.docker安装

```
docker pull postgres
docker run --name mypostgres -d -p 5432:5432 -e POSTGRES_PASSWORD=123456 postgres
```

##### 2.DB（数据库）、Schema（模式）、table（表）

一个DB可以包含多个Schema，一个Schema可以包含多个table

schema的优势：

可以让多个用户访问一个数据库，各自使用自己的schema，不会相互干扰

```sql
CREATE DATABASE base_frame;
CREATE USER root PASSWORD 'root';
-- 创建一个Schema dev
CREATE SCHEMA dev;
-- 让root用户用权限访问 Schema dev
ALTER SCHEMA dev OWNER TO root;
-- 让root用户用权限访问 base_frame DB 
ALTER DATABASE base_frame OWNER TO root;
-- create table 指定schema
CREATE TABLE dev.user (id INTEGER NOT NULL)

```

### 常见问题

##### 1.MySQL vs PostgreSQL

PG的优势：

①Postgre是完全支持ACID的（原子性atomicite、一致性consistency、隔离性isolation、持久性durability），而MySql是innodb实现了ACID

②Pg支持JSON格式的数据存储，Mysql不支持

③Pg的复杂查询效率比mysql高

④Pg的主从复制
