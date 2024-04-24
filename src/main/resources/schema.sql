CREATE TABLE users(
                      user_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      email VARCHAR,
                      login VARCHAR NOT NULL,
                      user_name VARCHAR,
                      birthday DATE,
                      CONSTRAINT email_valid_regex CHECK(regexp_like(email, '.*@.*')),
                      CONSTRAINT login_valid_regex CHECK(regexp_like(login, '[^\s]')),
                      CONSTRAINT name_not_blank_or_login CHECK (
                          (user_name IS NOT NULL AND user_name != '')
                              OR
                          (user_name IS NULL AND user_name = login)),
                      CONSTRAINT birthday_date_validator CHECK(birthday < CURRENT_DATE));


CREATE TABLE friends(
                        user_id int,
                        friend_id int,
                        FOREIGN KEY (user_id) REFERENCES users(user_id),
                        FOREIGN KEY (friend_id) REFERENCES users(user_id));


CREATE TABLE genre(
                      genre_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name VARCHAR);


CREATE TABLE rating(
                       rating_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       rating_name VARCHAR);


CREATE TABLE films(
                      film_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      film_name VARCHAR NOT NULL,
                      description VARCHAR(200),
                      release_date DATE,
                      duration_min int,
                      film_rating int,
                      FOREIGN KEY (film_rating) REFERENCES rating(rating_id),
                      CONSTRAINT name_valid CHECK(film_name != ''),
                      CONSTRAINT duration_valid CHECK(duration_min > 0),
                      CONSTRAINT release_date_validator CHECK(release_date > DATE '1895-12-28'));


CREATE TABLE film_genre(
                           film_id int,
                           genre_id int,
                           FOREIGN KEY (film_id) REFERENCES films(film_id),
                           FOREIGN KEY (genre_id) REFERENCES genre(genre_id));


CREATE TABLE likes(
                      film_id int,
                      user_id int,
                      FOREIGN KEY (film_id) REFERENCES films(film_id),
                      FOREIGN KEY (user_id) REFERENCES users(user_id));
