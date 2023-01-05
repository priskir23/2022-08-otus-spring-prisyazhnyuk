insert into genre (id, genre_name) values (10, 'Adventure');
insert into author (id, author_name) values (5, 'Alexandre Dumas');
insert into book (id, book_name, genre_id) values
        (30, 'The Three Mushketeers', 10),
        (40, 'Le Docteur Servan', 10);
insert into author_to_book(author_id, book_id) values
        (5, 30),
        (5, 40);
insert into book_comment (id, comment, book_id) values
        (10, 'nice', 30),
        (20, 'meh', 30),
        (30, 'boring', 30),
        (40, 'dull', 30),
        (50, 'fine book', 30),
        (60, 'masterpiece', 40),
        (70, 'so much water', 40),
        (80, 'so short', 40),
        (90, 'creepy', 40),
        (100, 'i didnt understand that', 40);
insert into user_ (id, name, pass) values
        (3, 'hello', 'abc qwe~! strongPassword1337');
-- для генерации индексов
alter table book_comment alter column id restart with (select max(id) from book_comment) + 1;
alter table book alter column id restart with (select max(id) from book) + 1;
alter table genre alter column id restart with (select max(id) from genre) + 1;
alter table author alter column id restart with (select max(id) from author) + 1;
alter table user_ alter column id restart with (select max(id) from author) + 1;