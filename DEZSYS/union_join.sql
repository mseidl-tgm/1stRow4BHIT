SELECT count(*)
FROM ((
SELECT * FROM combined.customers_us_name_phone
NATURAL JOIN
combined.remote_customers_us_non_name_phone)
UNION
(SELECT * FROM combined.customers_non_us_name_phone
NATURAL JOIN
combined.remote_customers_non_us_non_name_phone));

-- 1. us phone
-- JOIN
-- 2.  remote us non phone
-- UNION
-- 3 non us phone
--JOIN
-- 4.  remote non use non phone 

-- 1 & 3 lokal
-- 2 & 4 foreign

(SELECT COUNT(*) FROM horizontal.remote_customers_f_overand_21)
UNION
(SELECT COUNT(*) FROM horizontal.remote_customers_f_under_21)
UNION
(SELECT COUNT(*) FROM horizontal.customers_other));


SELECT COUNT(*) FROM vertical.customers_non_payment_info INNER JOIN 
vertical.customers_payment_info USING(customerid);

SELECT COUNT(*) FROM customers;

(SELECT COUNT(*) FROM horizontal.remote_customers_f_overand_21)
UNION
(SELECT COUNT(*) FROM horizontal.remote_customers_f_under_21)
UNION
(SELECT COUNT(*) FROM horizontal.customers_other));