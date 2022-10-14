insert into genre (id, genre_name) values
        (1, 'Adventure'),
        (2, 'Classics'),
        (3, 'Sci-Fi');
insert into book (id, book_name, genre_id) values
        (1, 'The Three Mushketeers', 1),
        (2, 'The Call of The Wild', 1),
        (3, 'Crime and Punishment', 2),
        (4, 'The Idiot', 2),
        (5, 'The Brothers Karamazov', 2),
        (6, 'I Have No Mouth and I Must Scream', 3),
        (7, 'The Doomed City', 3);
insert into author (id, author_name) values
        (1, 'Alexandre Dumas'),
        (2, 'Jack London'),
        (3, 'Harlan Jay Ellison'),
        (4, 'Fyodor Dostoevsky'),
        (5, 'Arkady Strugatsky'),
        (6, 'Boris Strugatsky');
insert into author_to_book (author_id, book_id) values
        (1, 1),
        (2, 2),
        (4, 3),
        (4, 4),
        (4, 5),
        (3, 6),
        (5, 7),
        (6, 7);