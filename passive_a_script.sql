drop database if exists ditran;
create database ditran;
use ditran;
create table t_passive_a_account(
  account varchar(32) primary key comment '账户',
  amount decimal(20,5) comment '金额'
);

insert into t_passive_a_account value ('lisi',10);

