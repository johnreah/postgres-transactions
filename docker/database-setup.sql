create database transactiontest;
\c transactiontest;
create table test(col1 int);

create database postgres_transactions_jdbc;
\c postgres_transactions_jdbc
create table orders(id serial primary key, dt timestamp without time zone, customer varchar(100));
create table lines(id serial primary key, orderId int, productCode varchar(100), description varchar(100), numUnits int, unitPrice dec(10,2), constraint fkOrder foreign key(orderId) references orders(id));

create database postgres_transactions_ormlite;

create database postgres_transactions_spring;

create database postgres_transactions_cdi;

create database postgres_transactions_deltaspike;
