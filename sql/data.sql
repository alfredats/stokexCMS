USE stokex;

INSERT INTO orderType (
  type_int
, order_type
) VALUES 
  (1, 'bid')
, (2, 'ask')
, (99, 'cancel');

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
  username
, password
, access
, apiKey
) VALUES 
  ('adminUser', UNHEX(SHA1('adminPassword')), 'admin', UNHEX(SHA1('adminApiKey')));

INSERT INTO users (
  username
, password
, apiKey
) VALUES
  ('dumbEngine', UNHEX(SHA1('dumbPassword')), UNHEX(SHA1('dumbApiKey')))
, ('testUser1', UNHEX(SHA1('testPassword')), UNHEX(SHA1('testApiKey1')))
, ('testUser2', UNHEX(SHA1('testPassword')), UNHEX(SHA1('testApiKey2')))
, ('maysieSim', UNHEX(SHA1('maysieSim')), UNHEX(SHA1('maysieSim')));

INSERT INTO stocks (
  ticker
, company
, currentPrice
) VALUES 
  ('TICKERTEST', 'DO NOT USE; TEST ONLY', 0.01)
, ('AAPL', 'Apple Inc.', 157.39)
;
-- , ('TSLA', 'Tesla Inc.', 867.29)
-- , ('MSFT', 'Microsoft Corporation', 275.15)
-- , ('V', 'Visa Inc.', 202.82)
-- , ('NFLX', 'Netflix Inc.', 180.97)
-- , ('GOOG', 'Alphabet Inc.', 2313.20)
-- , ('AMZN', 'Amazon.com Inc.', 2295.45)
-- , ('SHOP', 'Shopify Inc.', 377.49)
-- ;
