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

create table tbl_board (
	board_id varchar(50) PRIMARY KEY,
	board_title varchar(100) not null,
	board_content varchar(4000) not null,
	user_id varchar(50) not null,
	board_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	board_view int(30) default 0
);

drop table tbl_board ;

create table tbl_file (
	file_id varchar(50) PRIMARY KEY,
	file_name varchar(300) not null,
	file_extension varchar(30) not null,
	file_path varchar(300) not null,
	board_id varchar(50),
	FOREIGN KEY (board_id) REFERENCES tbl_board(board_id)
);

drop table tbl_file;

commit;