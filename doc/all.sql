DROP TABLE IF EXISTS `Test`;
CREATE TABLE `Test` (
    id bigint NOT NULL,
    name varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

insert into `Test` (id, name, password) values (1.0, 'alex', 'password');

DROP TABLE IF EXISTS `demo`;
CREATE TABLE demo (
    id bigint NOT NULL,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

insert into `demo` (id, name) values (1.0, 'bob');

drop table if exists `ebook`;
create table ebook (
    id bigint not null ,
    name varchar(50) not null,
    category1_id bigint,
    category2_id bigint,
    description varchar(200),
    cover varchar(200),
    doc_count int,
    view_count int,
    vote_count int,
    primary key (id)
);

insert into `ebook` (id, name, description, cover) values (1, 'Spring boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架', '/image/cover1.png');
insert into `ebook` (id, name, description, cover) values (2, 'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳首选框架', '/image/cover2.png');
insert into `ebook` (id, name, description) values (3, 'Python 入门教程', '零基础入门 Python 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (4, 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');

drop table if exists `category`;
create table `category` (
    id bigint not null,
    parent bigint not null default 0,
    name varchar(50) not null,
    sort int,
    primary key(id)
);

insert into `category` (id, parent, name, sort) values (100, 000, '前端开发', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, '基础应用', 201);
insert into `category` (id, parent, name, sort) values (202, 200, '框架应用', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, '基础应用', 301);
insert into `category` (id, parent, name, sort) values (302, 300, '进阶方向应用', 302);
insert into `category` (id, parent, name, sort) values (400, 000, '数据库', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, '其它', 500);
insert into `category` (id, parent, name, sort) values (501, 500, '服务器', 501);
insert into `category` (id, parent, name, sort) values (502, 500, '开发工具', 502);
insert into `category` (id, parent, name, sort) values (503, 500, '热门服务端语言', 503);

drop table if exists `doc`;
create table `doc` (
    id bigint not null,
    ebook_id bigint not null default 0,
    parent bigint not null default 0,
    name varchar(50) not null,
    sort int,
    view_count int default 0,
    vote_count int default 0,
    primary key (id)
);

insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (1, 1, 0, '文档1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (2, 1, 0, '文档1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (3, 1, 0, '文档2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0);
