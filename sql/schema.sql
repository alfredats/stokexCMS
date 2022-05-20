DROP SCHEMA IF EXISTS stokex;

CREATE SCHEMA stokex;

USE stokex;

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

CREATE TABLE stocks (
  ticker varchar(16) not null
, exchange varchar(16) not null
, companyName varchar(256) not null
, currentPrice float(10,2) not null
, timestamp_created datetime not null DEFAULT(CURRENT_TIMESTAMP)

, PRIMARY KEY(ticker)
);

-- USERS SCHEMA
CREATE TABLE users (
  username char(64) not null
, password binary(20) not null
, name char(64) not null
, access enum('user', 'admin') DEFAULT('user')
, dateCreated datetime not null DEFAULT(CURRENT_TIMESTAMP)

, PRIMARY KEY(username)
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

CREATE TABLE trades (
  trade_id int not null auto_increment
, bid_id int not null
, ask_id int not null
, ticker varchar(10) not null
, fulfilledQty int not null
, fee float(10,2) not null
, timestamp_created datetime not null DEFAULT(CURRENT_TIMESTAMP)

, PRIMARY KEY(trade_id)
, CONSTRAINT fk_trades_bidID
    FOREIGN KEY(bid_id)
    REFERENCES orderBook(order_id)
, CONSTRAINT fk_trades_askID
    FOREIGN KEY(ask_id)
    REFERENCES orderBook(order_id)
, CONSTRAINT fk_trades_ticker
    FOREIGN KEY(ticker)
    REFERENCES stocks(ticker)
);

CREATE TABLE userSessions (
  session_id char(32) not null
, username char(64) not null
, timestamp_created datetime not null
, timestamp_expired datetime not null

, PRIMARY KEY(session_id)
, CONSTRAINT fk_userSessions_username
    FOREIGN KEY(username)
    REFERENCES users(username)
);

CREATE TABLE monies (
  monies_id int not null
, username char(64) not null
, portfolio_value float(10,2) not null
, available_funds float(10,2) not null
, timestamp_created datetime not null DEFAULT(CURRENT_TIMESTAMP)

, PRIMARY KEY(monies_id)
, CONSTRAINT fk_monies_username
    FOREIGN KEY(username)
    REFERENCES users(username)
)

