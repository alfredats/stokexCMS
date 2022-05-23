USE stokex;

INSERT INTO orderType (
  type_int
, order_type
) VALUES 
  (1, 'bid')
, (2, 'ask')
, (99, 'cancel')
;

INSERT INTO orderStatus (
  status_int
, order_status
) VALUES
  (10, 'created')
, (19, 'partially fulfilled')
, (20, 'fulfilled')
, (90, 'cancelled')
, (91, 'cancelled by user')
, (92, 'cancelled by matching engine')
;

INSERT INTO users (
  name
, username
, password
, access
) VALUES 
  ('Admin', 'adminUser', UNHEX(SHA1('adminPassword')), 'admin')
;

INSERT INTO users (
  name
, username
, password
) VALUES
  ('Dumb Engine', 'dumbEngine', UNHEX(SHA1('dumbPassword')))
, ('Test User1', 'testUser1', UNHEX(SHA1('testPassword1')))
, ('Test User2', 'testUser2', UNHEX(SHA1('testPassword2')))
, ('Maysie Sim', 'maysieSim', UNHEX(SHA1('maysieSim')))
, ('Gerald Tan', 'geraldTan', UNHEX(SHA1('geraldTan')))
, ('Claire Ang', 'claireAng', UNHEX(SHA1('claireAng')))
;

INSERT INTO stocks (
  ticker
, exchange
, companyName
, currentPrice
) VALUES 
  ('TICKERTEST', 'TICKERTEST', 'DO NOT USE; TEST ONLY', 0.01)
, ('AAPL', 'NASDAQ', 'Apple Inc.', 157.39)
, ('MSFT', 'NASDAQ', 'Microsoft Corporation', 254.08)
, ('AMZN', 'NASDAQ', 'Amazon.com Inc.', 2142.25)
, ('TSLA', 'NASDAQ', 'Tesla Inc.', 709.81)
, ('GOOG', 'NASDAQ', 'Alphabet Inc.', 2248.02 )
, ('FB', 'NASDAQ', 'Meta Platforms Inc.', 192.24)
, ('NVDA', 'NASDAQ', 'NVIDIA Corporation', 169.38)
, ('PEP', 'NASDAQ', 'PepsiCo Inc.', 163.65)
, ('AVGO', 'NASDAQ', 'Broadcom Inc.', 570.57)
, ('CSCO', 'NASDAQ', 'Cisco Systems Inc', 48.36)
, ('ADBE', 'NASDAQ', 'Adobe Inc.', 397.88)
, ('INTC', 'NASDAQ', 'Intel Corporation', 42.35)
, ('TXN', 'NASDAQ', 'Texas Instrumental Incorporated', 170.30)
, ('AMD', 'NASDAQ', 'Advanced Micro Devices Inc.', 96.28)
, ('QCOM', 'NASDAQ', 'QualComm Incorporated', 130.45)
, ('HON', 'NASDAQ', 'Honeywell International Inc.', 193.87)
, ('INTU', 'NASDAQ', 'Intuit Inc.', 353.31)
, ('AMAT', 'NASDAQ', 'Applied Materials Inc.', 111.34)
, ('NFLX', 'NASDAQ', 'Netflix Inc.', 177.19)
, ('ADSK', 'NASDAQ', 'Autodesk Inc.', 187.98)
;


INSERT INTO orderBook (
  username 
, ticker
, price 
, size 
, order_type 
, order_status 
, timestamp_created
, timestamp_updated
) VALUES 
  ('maysieSim', 'AAPL', 151.9, 11, 1, 20, '2022-01-11T10:55:15', '2022-01-11T12:15:38')
, ('maysieSim', 'INTU', 330.18, 32, 1, 20, '2022-01-15T13:40:10', '2022-01-28T15:15:28')
, ('maysieSim', 'TSLA', 1020.18, 8, 1, 19, '2022-01-21T18:42:11', '2022-02-11T17:25:30')
, ('maysieSim', 'AAPL', 181.9, 5, 2, 20, '2022-02-02T12:45:15', '2022-02-03T09:55:38')
, ('maysieSim', 'INTU', 331.18, 10, 2, 20, '2022-02-18T14:20:05', '2022-02-21T13:15:28')
, ('maysieSim', 'TSLA', 920.18, 10, 2, 90, '2022-02-19T16:51:14', '2022-04-21T14:25:30')
, ('geraldTan', 'AAPL', 151.9, 11, 1, 20, '2022-01-11T10:55:15', '2022-01-11T12:15:38')
, ('geraldTan', 'INTU', 330.18, 32, 1, 20, '2022-01-15T13:40:10', '2022-01-28T15:15:28')
, ('geraldTan', 'TSLA', 1020.18, 8, 1, 19, '2022-01-21T18:42:11', '2022-02-11T17:25:30')
, ('geraldTan', 'AAPL', 181.9, 5, 2, 20, '2022-02-02T12:45:15', '2022-02-03T09:55:38')
, ('geraldTan', 'INTU', 331.18, 10, 2, 20, '2022-02-18T14:20:05', '2022-02-21T13:15:28')
, ('geraldTan', 'TSLA', 920.18, 10, 1, 90, '2022-02-19T16:51:14', '2022-04-21T14:25:30')
, ('claireAng', 'AAPL', 151.9, 11, 1, 20, '2022-01-11T10:55:15', '2022-01-11T12:15:38')
, ('claireAng', 'INTU', 330.18, 32, 1, 20, '2022-01-15T13:40:10', '2022-01-28T15:15:28')
, ('claireAng', 'TSLA', 1020.18, 8, 1, 19, '2022-01-21T18:42:11', '2022-02-11T17:25:30')
, ('claireAng', 'AAPL', 181.9, 5, 2, 20, '2022-02-02T12:45:15', '2022-02-03T09:55:38')
, ('claireAng', 'INTU', 331.18, 10, 2, 20, '2022-02-18T14:20:05', '2022-02-21T13:15:28')
, ('claireAng', 'TSLA', 920.18, 10, 1, 90, '2022-02-19T16:51:14', '2022-04-21T14:25:30')
;




INSERT INTO monies (
  monies_id 
, username 
, portfolio_value 
, available_funds
, timestamp_created
) VALUES 
  (1, 'maysieSim', 4720.19, 200.94, '2022-01-01T00:00:01')
, (2, 'maysieSim', 5210.48, 97.35, '2022-02-01T00:00:01')
, (3, 'maysieSim', 5155.23, 403.20, '2022-03-01T00:00:01')
, (4, 'maysieSim', 5390.11, 212.13, '2022-04-01T00:00:01')
, (5, 'maysieSim', 6164.04, 109.43, '2022-05-01T00:00:01')
, (6, 'geraldTan', 500.15, 200.94, '2022-01-01T00:00:01')
, (7, 'geraldTan', 450.48, 97.35, '2022-02-01T00:00:01')
, (8, 'geraldTan', 811.23, 403.20, '2022-03-01T00:00:01')
, (9, 'geraldTan', 620.11, 212.13, '2022-04-01T00:00:01')
, (10, 'geraldTan', 1020.04, 109.43, '2022-05-01T00:00:01')
, (11, 'claireAng', 2022.19, 200.94, '2022-01-01T00:00:01')
, (12, 'claireAng', 2511.48, 97.35, '2022-02-01T00:00:01')
, (13, 'claireAng', 2384.23, 403.20, '2022-03-01T00:00:01')
, (14, 'claireAng', 2798.11, 212.13, '2022-04-01T00:00:01')
, (15, 'claireAng', 2910.04, 109.43, '2022-05-01T00:00:01')
;
