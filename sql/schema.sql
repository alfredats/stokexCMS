DROP SCHEMA IF EXISTS stokex;

CREATE SCHEMA stokex;

USE stokex;

CREATE TABLE users (
  username char(64) not null,
  password binary(20) not null,
  dateCreated datetime not null DEFAULT(CURRENT_TIMESTAMP),

  PRIMARY KEY(username)
);

CREATE TABLE stocks (
  ticker varchar(10) not null,
  company varchar(256) not null,
  info_url text not null,
  price_url text not null,

  PRIMARY KEY(ticker)
);

CREATE TABLE secrets (
  apiKey 

CREATE TABLE orders (
  order_id int not null auto_increment,
  username char(64) not null,
  ticker varchar(10) not null,
  pricePerUnit float(10,2) not null,
  quantity int not null,
  order_type enum('market_buy', 'market_sell', 'limit_buy', 'limit_sell') not null,
  order_status enum('created', 'fulfilled') not null DEFAULT 'created',
  dateCreated datetime not null DEFAULT(CURRENT_TIMESTAMP),
  dateUpdated datetime not null DEFAULT(CURRENT_TIMESTAMP),

  PRIMARY KEY(order_id),
  CONSTRAINT fk_ticker
    FOREIGN KEY(ticker)
    REFERENCES stocks(ticker),
  CONSTRAINT fk_username
    FOREIGN KEY(username)
    REFERENCES users(username)
);


