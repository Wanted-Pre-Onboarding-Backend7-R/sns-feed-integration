set FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE post_hashtag;
TRUNCATE TABLE post;
set FOREIGN_KEY_CHECKS = 1;

INSERT INTO member(account_name, email, password, approval_code, is_approved, authority)
values ('member1', 'test@test.com', '1234', 'test', 1, 'ROLE_USER');

INSERT INTO post(content_id, type, title, content, view_count, like_count, share_count, created_at)
VALUES ('content1', 'FACEBOOK', 'Title 1', 'This is content 1', 100, 10, 5, '2023-10-01 12:01:01'),
       ('content2', 'TWITTER', 'Title 2', 'This is content 2', 150, 15, 7, '2023-10-01 13:02:02'),
       ('content3', 'INSTAGRAM', 'Title 3', 'This is content 3', 200, 20, 10, '2023-10-01 14:03:03'),
       ('content4', 'THREADS', 'Title 4', 'This is content 4', 250, 25, 12, '2023-10-01 15:04:04'),
       ('content5', 'FACEBOOK', 'Title 5', 'This is content 5', 300, 30, 15, '2023-10-01 16:05:05'),
       ('content6', 'TWITTER', 'Title 6', 'This is content 6', 350, 35, 17, '2023-10-01 17:06:06'),
       ('content7', 'INSTAGRAM', 'Title 7', 'This is content 7', 400, 40, 20, '2023-10-01 18:07:07'),
       ('content8', 'THREADS', 'Title 8', 'This is content 8', 450, 45, 22, '2023-10-01 19:08:08'),
       ('content9', 'FACEBOOK', 'Title 9', 'This is content 9', 500, 50, 25, '2023-10-05 20:09:09'),
       ('content10', 'TWITTER', 'Title 10', 'This is content 10', 550, 55, 27, '2023-10-10 21:10:10'),
       ('content11', 'INSTAGRAM', 'Title 11', 'This is content 11', 600, 60, 30, '2023-10-15 22:11:11'),
       ('content12', 'THREADS', 'Title 12', 'This is content 12', 650, 65, 32, '2023-10-20 23:12:12'),
       ('content13', 'FACEBOOK', 'Title 13', 'This is content 13', 700, 70, 35, '2023-10-25 00:13:13'),
       ('content14', 'TWITTER', 'Title 14', 'This is content 14', 750, 75, 37, '2023-10-30 01:14:14'),
       ('content15', 'INSTAGRAM', 'Title 15', 'This is content 15', 800, 80, 40, '2023-11-01 02:15:15'),
       ('content16', 'THREADS', 'Title 16', 'This is content 16', 850, 85, 42, '2023-11-05 03:16:16'),
       ('content17', 'FACEBOOK', 'Title 17', 'This is content 17', 900, 90, 45, '2023-11-10 04:17:17'),
       ('content18', 'TWITTER', 'Title 18', 'This is content 18', 950, 95, 47, '2023-11-15 05:18:18'),
       ('content19', 'INSTAGRAM', 'Title 19', 'This is content 19', 1000, 100, 50, '2023-11-20 06:19:19'),
       ('content20', 'THREADS', 'Title 20', 'This is content 20', 1050, 105, 52, '2023-11-25 07:20:20');

INSERT INTO post_hashtag(post_id, hashtag)
VALUES (1, 'hashtag1'),
       (1, 'member1'),
       (2, 'hashtag2'),
       (2, 'member1'),
       (3, 'hashtag3'),
       (3, 'member1'),
       (4, 'hashtag4'),
       (5, 'hashtag5'),
       (6, 'hashtag1'),
       (7, 'hashtag2'),
       (8, 'hashtag3'),
       (9, 'hashtag4'),
       (10, 'hashtag5'),
       (11, 'hashtag1'),
       (12, 'hashtag2'),
       (12, 'member1'),
       (12, 'hashtag2'),
       (13, 'hashtag3'),
       (14, 'hashtag4'),
       (15, 'hashtag5'),
       (16, 'hashtag1'),
       (17, 'hashtag2'),
       (18, 'hashtag3'),
       (19, 'hashtag4'),
       (20, 'hashtag5');

