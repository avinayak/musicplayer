
CREATE TABLE music(
id number(6) primary key,
title varchar(100),
album varchar(50),
artist varchar(50),
genre varchar(50),
duration varchar(10));

--increase size of name next time

CREATE TABLE files(
id number(6) primary key,
file varchar(50));

CREATE TABLE queue(
id number(6),
name varchar(500));
