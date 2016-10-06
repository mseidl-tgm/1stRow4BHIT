CREATE TABLE vertical.customers_payment_info(
	customerid INTEGER PRIMARY KEY NOT NULL DEFAULT nextval('customers_customerid_seq'::regclass),
	creditcardtype INT NOT NULL,
	creditcard VARCHAR(50) NOT NULL,
	creditcardexpiration VARCHAR(50) NOT NULL,
	age smallint,
	income INT
);

INSERT INTO vertical.customers_payment_info
SELECT customerid, creditcardtype, creditcard, creditcardexpiration, age, income FROM customers;
