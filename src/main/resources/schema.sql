DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(50)                             NOT NULL,
    email      VARCHAR(40)                             NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(100)                            NOT NULL,
    user_id INTEGER,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    owner_id     INTEGER                                  NOT NULL,
    name    VARCHAR(50)                             NOT NULL,
    description  VARCHAR(100)                            NOT NULL,
    is_available    BOOLEAN                                 NOT NULL,
    request_id   INTEGER,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_items UNIQUE (owner_id, name, description)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(100)                            NOT NULL,
    item_id   INTEGER,
    author_id INTEGER,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items (id)   ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    INTEGER                                  NOT NULL,
    booker_id  INTEGER                                  NOT NULL,
    status     VARCHAR(50)                      NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items (id)   ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_bookings UNIQUE (start_date, end_date, item_id, booker_id)
);