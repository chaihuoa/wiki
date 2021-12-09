insert into "Test" (id, name, password) values (1.0, 'alex', 'password');
insert into "demo" (id, name) values (1.0, 'bob');

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id SERIAL NOT NULL,
    address varchar(255) DEFAULT NULL,
    birthday date DEFAULT NULL,
    email varchar(255) DEFAULT NULL,
    identity int DEFAULT NULL,
    is_admin int DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    size int DEFAULT NULL,
    tel varchar(255) DEFAULT NULL,
    username varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO users VALUES ('1', 'Chicago', '2000-07-14', 'stu@gmail.com', '0', '1', '123', '2', '1357614550', 'stu');
INSERT INTO users VALUES ('2', 'LA', '2000-07-14', 'tea@gmail.com', '1', '1', '123', '0', 'string', 'tea');
INSERT INTO users VALUES ('3', 'NY', '2000-07-14', 'other@gmail.com', '2', '1', '123', '3', '1507886452', 'other');
INSERT INTO users VALUES ('4', 'Chicago', '2000-07-16', 'admin@gmail.com', '3', '0', '123', '3', '1767988880', 'admin');
INSERT INTO users VALUES ('8', 'Chicago', '1998-02-12', 'guest01@gmail.com', '3', '1', '123', '10', '1357600880', 'guest01');
INSERT INTO users VALUES ('9', 'Chicago', '1982-01-01', 'guest02@gmail.com', '3', '1', '123', '10', '176822000', 'guest02');