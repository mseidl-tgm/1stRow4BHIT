DROP TABLE IF EXISTS combined.customers_non_us_name_phone;
CREATE TABLE combined.customers_non_us_non_name_phone(
	customerid INTEGER NOT NULL,
	email VARCHAR(50),
	creditcardtype VARCHAR(50) NOT NULL,
	creditcard VARCHAR(50) NOT NULL,
	creditcardexpiration VARCHAR(50) NOT NULL,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL,
	age smallint,
	income integer,
	gender VARCHAR(1)
);

INSERT INTO combined.customers_us_non_name_phone
SELECT customerid, email, creditcardtype, creditcard, creditcardexpiration, username,
password, age, income, gender FROM customers WHERE country != 'US';

DROP TABLE IF EXISTS combined.customers_us_non_name_phone;
CREATE TABLE combined.customers_us_non_name_phone(
	customerid INTEGER NOT NULL,
	email VARCHAR(50),
	creditcardtype VARCHAR(50) NOT NULL,
	creditcard VARCHAR(50) NOT NULL,
	creditcardexpiration VARCHAR(50) NOT NULL,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL,
	age smallint,
	income integer,
	gender VARCHAR(1)
);

INSERT INTO combined.customers_us_non_name_phone
SELECT customerid, email, creditcardtype, creditcard, creditcardexpiration, username,
password, age, income, gender FROM customers WHERE country = 'US';

	