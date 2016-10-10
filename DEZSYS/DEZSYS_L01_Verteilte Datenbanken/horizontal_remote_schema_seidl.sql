DROP FOREIGN TABLE IF EXISTS horizontal.remote_customers_f_under_21;
CREATE FOREIGN TABLE horizontal.remote_customers_f_under_21(
	customerid integer NOT NULL,
	firstname varchar(50) NOT NULL,
	lastname varchar(50) NOT NULL,
	address1 varchar(50) NOT NULL,
	address2 varchar(50),
	city varchar(50) NOT NULL,
	state varchar(50),
	zip varchar(9),
	country varchar(50) NOT NULL,
	region smallint NOT NULL,
	email varchar(50),
	phone varchar(50),
	creditcardtype integer NOT NULL,
	creditcard varchar(50) NOT NULL,
	creditcardexpiration varchar(50) NOT NULL,
	username varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	age smallint,
	income integer,
	gender varchar(1))
SERVER foreign_server OPTIONS(schema_name'horizontal', table_name 'customers_f_under_21');

DROP FOREIGN TABLE IF EXISTS horizontal.remote_customers_f_overand_21;
CREATE FOREIGN TABLE horizontal.remote_customers_f_overand_21(
	customerid integer NOT NULL,
	firstname varchar(50) NOT NULL,
	lastname varchar(50) NOT NULL,
	address1 varchar(50) NOT NULL,
	address2 varchar(50),
	city varchar(50) NOT NULL,
	state varchar(50),
	zip varchar(9),
	country varchar(50) NOT NULL,
	region smallint NOT NULL,
	email varchar(50),
	phone varchar(50),
	creditcardtype integer NOT NULL,
	creditcard varchar(50) NOT NULL,
	creditcardexpiration varchar(50) NOT NULL,
	username varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	age smallint,
	income integer,
	gender varchar(1)
)
SERVER foreign_server OPTIONS(schema_name 'horizontal', table_name 'customers_f_overand_21');

DROP TABLE IF EXISTS horizontal.customers_other;
CREATE TABLE horizontal.customers_other(
	customerid integer NOT NULL,
	firstname varchar(50) NOT NULL,
	lastname varchar(50) NOT NULL,
	address1 varchar(50) NOT NULL,
	address2 varchar(50),
	city varchar(50) NOT NULL,
	state varchar(50),
	zip varchar(9),
	country varchar(50) NOT NULL,
	region smallint NOT NULL,
	email varchar(50),
	phone varchar(50),
	creditcardtype integer NOT NULL,
	creditcard varchar(50) NOT NULL,
	creditcardexpiration varchar(50) NOT NULL,
	username varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	age smallint,
	income integer,
	gender varchar(1)
);

INSERT INTO horizontal.customers_other
SELECT * FROM customers WHERE gender!='F' AND age < 21;

INSERT INTO horizontal.customers_other
SELECT * FROM customers WHERE gender!='F' AND age >= 21;

