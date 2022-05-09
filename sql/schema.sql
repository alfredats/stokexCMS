DROP SCHEMA IF EXISTS stokex;

CREATE SCHEMA stokex;

USE stokex;

CREATE TABLE stocks (
  ticker varchar(10) not null
, company varchar(256) not null
, currentPrice float(10,2) not null

, PRIMARY KEY(ticker)
);

-- USERS SCHEMA
CREATE TABLE users (
  username char(64) not null
, password binary(20) not null
, apiKey char(64) not null
, dateCreated datetime not null DEFAULT(CURRENT_TIMESTAMP)

, PRIMARY KEY(username)
);


-- ORDER BOOK SCHEMA
CREATE TABLE orderType (
  type_int int not null
, order_type varchar(128)

, PRIMARY KEY(type_int)
);

CREATE TABLE orderStatus (
  status_int int not null
, order_status varchar(128) not null

, PRIMARY KEY(status_int)
);

CREATE TABLE orderBook (
  order_id int not null auto_increment
, ticker varchar(10) not null
, price float(10,2) not null
, size int not null
, order_type int not null -- 1 is bid, 2 is offer
, order_status int not null DEFAULT 10 -- see orderStatus table for additional info
, timestamp_created datetime not null DEFAULT(CURRENT_TIMESTAMP) -- matching engine sees timestamp_created
, timestamp_updated datetime not null DEFAULT(CURRENT_TIMESTAMP) -- user sees timestamp_updated
, username char(64) not null
, note varchar(128) 

, PRIMARY KEY(order_id)
, CONSTRAINT fk_ticker
    FOREIGN KEY(ticker)
    REFERENCES stocks(ticker)
, CONSTRAINT fk_orderBook_username
    FOREIGN KEY(username)
    REFERENCES users(username)
, CONSTRAINT fk_orderBook_orderType
    FOREIGN KEY(order_type)
    REFERENCES orderType(type_int)
, CONSTRAINT fk_orderBook_orderStatus
    FOREIGN KEY(order_status)
    REFERENCES orderStatus(status_int)
);


