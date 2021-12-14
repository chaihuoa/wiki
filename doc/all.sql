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

insert into `ebook` (id, name, description) values (1, 'Spring boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (2, 'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (3, 'Python 入门教程', '零基础入门 Python 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (4, 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');
