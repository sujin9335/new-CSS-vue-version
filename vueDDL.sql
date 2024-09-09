use sujin2;
create table tbl_user (
	user_id varchar(50) PRIMARY KEY,
	user_login_id varchar(30) not null,
	user_pw varchar(100) not null,
	user_name varchar(30) not null,
	user_mail varchar(50),
	user_tel varchar(50),
	user_auth int(30),
	user_use varchar(2) default 'n',
	user_lock_cnt int(5) default 0,
	user_nickname varchar(30) not null,
	user_register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

drop table tbl_user;

-- 구성
create table tbl_monitor (
	monitor_id varchar(50),
	monitor_content	varchar(30),
	monitor_title varchar(50),
	monitor_x int(30),
	monitor_y int(30),
	monitor_width int(30),
	monitor_hight int(30),
	monitor_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP

)

commit;