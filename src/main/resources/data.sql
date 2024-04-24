INSERT INTO users (email, login, user_name, birthday)
VALUES
    ('user1@example.com', 'user1', 'User One', '1990-01-01'),
    ('user2@example.com', 'user2', 'User Two', '1995-05-15'),
    ('user3@example.com', 'user3', 'User Three', '2000-10-20'),
    ('user4@example.com', 'user4', 'User Four', '1988-08-08'),
    ('user5@example.com', 'user5', 'User Five', '1992-12-25');

INSERT INTO genre (name)
VALUES
    ('Action'),
    ('Comedy'),
    ('Drama'),
    ('Thriller'),
    ('Horror');

INSERT INTO rating (rating_name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

INSERT INTO films (film_name, description, release_date, duration_min, film_rating)
VALUES
    ('Film One', 'Description for Film One', '2020-01-01', 120, 1),
    ('Film Two', 'Description for Film Two', '2019-05-15', 110, 2),
    ('Film Three', 'Description for Film Three', '2021-10-20', 130, 3),
    ('Film Four', 'Description for Film Four', '2018-08-08', 105, 4),
    ('Film Five', 'Description for Film Five', '2022-12-25', 95, 5);

INSERT INTO film_genre (film_id, genre_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);

INSERT INTO friends (user_id, friend_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 4),
    (3, 4),
    (4, 5);

INSERT INTO likes (film_id, user_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 3),
    (5, 4);
