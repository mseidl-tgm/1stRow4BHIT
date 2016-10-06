DROP FOREIGN TABLE IF EXISTS vertical.remote_customers_payment_info;
CREATE FOREIGN TABLE vertical.customers_payment_info(
	
	customerid INTEGER NOT NULL,
	creditcardtype INT NOT NULL,
	creditcard VARCHAR(50) NOT NULL,
	creditcardexpiration VARCHAR(50) NOT NULL,
	age smallint,
	income INTEGER
)
SERVER foreign_server OPTIONS(schema_name 'vertical', table_name 'customers_payment_info');

DROP TABLE IF EXISTS vertical.customers_non_payment_info;
CREATE TABLE vertical.customers_non_payment_info(

	customerid INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('customers_customerid_seq'::regclass),
	firstname VARCHAR(50),
	lastname VARCHAR(50),
	address1 VARCHAR(50),
	address2 VARCHAR(50),
	city VARCHAR(50),
	state VARCHAR(50),
	zip VARCHAR(9),
	country VARCHAR(50),
	region smallint,
	email VARCHAR(50),
	phone VARCHAR(50),
	username VARCHAR(50),
	password VARCHAR(50),
	gender VARCHAR(1)
);

INSERT INTO vertical.customers_non_payment_info
SELECT customerid, firstname, lastname, address1, address2, city, state, zip, country,
region, email, phone, username, password, gender FROM customers;
	