CREATE TABLE IF NOT EXISTS users
( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 name VARCHAR(100),
 email VARCHAR(320) NOT NULL ,
 CONSTRAINT u_email UNIQUE(email));

CREATE TABLE IF NOT EXISTS requests
( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  description VARCHAR(1000),
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_tags_to_users FOREIGN KEY(user_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS items
( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(1000),
    is_available BOOLEAN NOT NULL ,
    owner_id INTEGER NOT NULL ,
    request_id INTEGER,
    CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
    CONSTRAINT fk_items_to_request FOREIGN KEY(request_id) REFERENCES requests(id));

CREATE TABLE IF NOT EXISTS comments
( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(1000),
    author_id INTEGER NOT NULL ,
    item_id INTEGER NOT NULL ,
    created TIMESTAMP NOT NULL,
    CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id),
    CONSTRAINT fk_comments_to_item FOREIGN KEY(item_id) REFERENCES items(id));

CREATE TABLE IF NOT EXISTS bookings
( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP NOT NULL ,
    end_date TIMESTAMP NOT NULL ,
    booker_id INTEGER NOT NULL ,
    item_id INTEGER NOT NULL ,
    status varchar(20) NOT NULL ,
    CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id),
    CONSTRAINT fk_bookings_to_item FOREIGN KEY(item_id) REFERENCES items(id));

