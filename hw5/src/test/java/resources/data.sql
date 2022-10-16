insert into genre (id, genre_name) values (10, 'Adventure');
insert into author (id, author_name) values (5, 'Alexandre Dumas');
insert into book (id, book_name, genre_id) values
        (30, 'The Three Mushketeers', 10),
        (40, 'Le Docteur Servan', 10);
insert into author_to_book(author_id, book_id) values
        (5, 30),
        (5, 40);