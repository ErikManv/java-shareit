DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;

CREATE TABLE IF NOT EXISTS users
( id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL ,
  name VARCHAR(100),
  email VARCHAR(320) NOT NULL ,
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT u_email UNIQUE(email));

CREATE TABLE IF NOT EXISTS requests
( id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL ,
  description VARCHAR(1000),
  owner_id INTEGER NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT fk_tags_to_users FOREIGN KEY(owner_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS items
( id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL ,
  name VARCHAR(100),
  description VARCHAR(1000),
  is_available BOOLEAN NOT NULL ,
  owner_id INTEGER NOT NULL ,
  request_id INTEGER,
  CONSTRAINT pk_items PRIMARY KEY (id),
  CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests (id),
  CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id));

CREATE TABLE IF NOT EXISTS comments
( id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL ,
  text VARCHAR(1000),
  author_id INTEGER NOT NULL ,
  item_id INTEGER NOT NULL ,
  created TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_comments PRIMARY KEY (id),
  CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items (id),
  CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (id));

CREATE TABLE IF NOT EXISTS bookings
( id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL ,
  start_date TIMESTAMP WITHOUT TIME ZONE ,
  end_date TIMESTAMP WITHOUT TIME ZONE ,
  booker_id INTEGER NOT NULL ,
  item_id INTEGER NOT NULL ,
  status varchar(20) NOT NULL ,
  CONSTRAINT pk_bookings PRIMARY KEY (id),
  CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items (id),
  CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (id));