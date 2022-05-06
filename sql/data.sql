USE stokex;

INSERT INTO users (
  username,
  password
) VALUES 
  ('testUser', UNHEX(SHA1('testPassword')));

