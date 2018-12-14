create database ditran;
create table t_active_account(
  account varchar(32) primary key comment '账户',
  amount decimal(20,5)   comment '金额'
);
create table t_passive_a_account(
  account varchar(32) primary key comment '账户',
  amount decimal(20,5)   comment '金额'
);
create table t_passive_b_account(
  account varchar(32) primary key comment '账户',
  amount decimal(20,5)   comment '金额'
);


insert into t_active_account value ('zhangsan',100);
insert into t_passive_a_account value ('lisi',10);
insert into t_passive_b_account value ('wangwu',20);