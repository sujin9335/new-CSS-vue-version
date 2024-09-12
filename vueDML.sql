use sujin2;

-- 유저
select * from tbl_user tu ;
delete from tbl_user ;

insert into tbl_user (user_id, user_login_id, user_pw, user_name, user_mail, user_tel, user_auth, user_use, user_nickname)
values ('319b1008-c52a-42be-8d68-ec909f7fee5a', 'sj', '3b0cb6318e56ff096de92f7c4a8c682a679946c3f2cfea1fa6e822aaa9eae39c', '수진딸기', 'sujin_78@naver.com', '010-9335-6987', 1, 'y', '딸기');

insert into tbl_user (user_id, user_login_id, user_pw, user_name, user_mail, user_tel, user_auth, user_use, user_nickname)
values ('263d5a3f-82ab-4ff0-817a-77483353983c', 'sj2', '3b0cb6318e56ff096de92f7c4a8c682a679946c3f2cfea1fa6e822aaa9eae39c', '수진바나나', 'sujin_78@naver.com', '010-9335-6987', 0, 'y', '바나나');



-- 구성
select * from tbl_monitor order by monitor_date desc;

ALTER TABLE tbl_monitor
MODIFY COLUMN monitor_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE tbl_monitor
MODIFY COLUMN monitor_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;



INSERT INTO tbl_monitor (
    monitor_id,
    monitor_content,
    monitor_title,
    monitor_x,
    monitor_y,
    monitor_width,
    monitor_hight
) VALUES (
    '63d5a3f-82ab-4ff0-817a-7748335',                -- 모니터 ID
    'bar',           -- 모니터 내용
    'bar제목',             -- 모니터 제목
    100,                        -- X 좌표
    200,                        -- Y 좌표
    300,                       -- 모니터 너비
    400                        -- 모니터 높이
);

INSERT INTO tbl_monitor (
    monitor_id,
    monitor_content,
    monitor_title,
    monitor_x,
    monitor_y,
    monitor_width,
    monitor_hight
) VALUES (
    '63d5a3f-82ab-4ff0-8175',                -- 모니터 ID
    'line',           -- 모니터 내용
    'line제목',             -- 모니터 제목
    200,                        -- X 좌표
    300,                        -- Y 좌표
    300,                       -- 모니터 너비
    400                        -- 모니터 높이
);


-- 보드
select * from tbl_board;
delete from tbl_board;

INSERT INTO tbl_board (board_id ,board_title, board_content, user_id)
VALUES ('019feb9a-1d76-4238-805b-da2250a133c6', '<p>임시제목2</p>', '임시내용2', '263d5a3f-82ab-4ff0-817a-77483353983c');

INSERT INTO tbl_board (board_id ,board_title, board_content, user_id)
VALUES ('019feb9a-1d76-4238-805b-da2250a153c6', '임시제목3', '임시내용3', '263d5a3f-82ab-4ff0-817a-77483353983c');

commit;



DELIMITER 

CREATE PROCEDURE InsertMultipleBoards()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 50 DO
        INSERT INTO tbl_board (board_id, board_title, board_content, user_id)
        VALUES (UUID(), CONCAT('임시제목', i), CONCAT('임시내용', i), '263d5a3f-82ab-4ff0-817a-77483353983c');
        SET i = i + 1;
    END WHILE;
END 

DELIMITER ;

CALL InsertMultipleBoards();


