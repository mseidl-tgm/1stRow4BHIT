-- Autor: MARM
-- Datum: 20120306
-- Zweck: Musterloesung fuer "Stored Routines"

\c template1
DROP DATABASE restaurant;
CREATE DATABASE restaurant;
\c restaurant

-- DROP TABLE IF EXISTS kellner CASCADE;
CREATE TABLE kellner (
             knr         INTEGER,
             name        VARCHAR(255),
             PRIMARY KEY (knr)
             );

INSERT INTO kellner VALUES (1, 'Kellner1');
INSERT INTO kellner VALUES (2, 'Kellner2');
INSERT INTO kellner VALUES (3, 'Kellner3');

-- DROP TABLE IF EXISTS speise CASCADE;
CREATE TABLE speise (
             snr         INTEGER,
             bezeichnung VARCHAR(255),
             preis       DECIMAL(6,2),
             PRIMARY KEY (snr)
             );

INSERT INTO speise VALUES (1, 'Heisse Liebe',         3);
INSERT INTO speise VALUES (2, 'Schoko Palatschinken', 4);
INSERT INTO speise VALUES (3, 'Pute gebacken',        7);
INSERT INTO speise VALUES (4, 'Pute natur',           8);
INSERT INTO speise VALUES (5, 'Puten-Cordon',         9);
INSERT INTO speise VALUES (6, 'Menue fuer 2',        15);
INSERT INTO speise VALUES (7, 'Menue fuer 3',        19);
INSERT INTO speise VALUES (8, 'Menue fuer 4',        22);

-- DROP TABLE IF EXISTS rechnung CASCADE;
CREATE TABLE rechnung (
             rnr         INTEGER,
             datum       DATE,
             tisch       SMALLINT,
             status      CHAR(8),
             knr         INTEGER,
             PRIMARY KEY (rnr),
             FOREIGN KEY (knr) REFERENCES kellner (knr)
                               ON UPDATE CASCADE ON DELETE CASCADE
             );

INSERT INTO rechnung VALUES (1, '2013-03-07', 1, 'bezahlt', 1);
INSERT INTO rechnung VALUES (2, '2013-03-07', 2, 'offen', 2);
INSERT INTO rechnung VALUES (3, '2013-03-07', 1, 'gedruckt', 3);
INSERT INTO rechnung VALUES (4, '2013-03-07', 1, 'gedruckt', 1);
INSERT INTO rechnung VALUES (5, '2013-03-07', 1, 'bezahlt', 1);
INSERT INTO rechnung VALUES (6, '2013-03-07', 2, 'offen', 2);

-- DROP TABLE IF EXISTS bestellung CASCADE;
CREATE TABLE bestellung (
             anzahl      SMALLINT,
             rnr         INTEGER,
             snr         INTEGER,
             PRIMARY KEY (rnr, snr),
             FOREIGN KEY (rnr) REFERENCES rechnung (rnr)
                               ON UPDATE CASCADE ON DELETE CASCADE,
             FOREIGN KEY (snr) REFERENCES speise (snr)
                               ON UPDATE CASCADE ON DELETE CASCADE
             );

INSERT INTO bestellung VALUES (9, 1, 1);
INSERT INTO bestellung VALUES (1, 1, 4);
INSERT INTO bestellung VALUES (1, 1, 5);
INSERT INTO bestellung VALUES (1, 2, 7);
INSERT INTO bestellung VALUES (1, 3, 8);
INSERT INTO bestellung VALUES (9, 4, 1);
INSERT INTO bestellung VALUES (9, 5, 1);
INSERT INTO bestellung VALUES (9, 6, 2);


-- Mitschrift INSY @ 5YHITM vom 30.09.2016

CREATE FUNCTION preiserhoehung() RETURNS VOID AS '
UPDATE speise SET preis = preis * 1.05; ' LANGUAGE SQL;


select preiserhoehung();

-- A01: 
--a: Alle Preise sollen so geändert (erhöht) werden, dass sie mit 99 Cent enden.
DROP FUNCTION preis99();
CREATE FUNCTION preis99() RETURNS VOID AS '
UPDATE speise SET preis = round(preis-0.5) + 0.99;' LANGUAGE SQL;

SELECT preis99();


-- b: Alle Rechnungen zu denen es keine Bestellungen gibt, sollen gelöscht werden.
DROP FUNCTION loescheRechnung();

CREATE FUNCTION loescheRechnung() RETURNS VOID AS '
DELETE FROM rechnung WHERE status = ''bezahlt'' ' LANGUAGE SQL;

SELECT loescheRechnung();

-- A02: Erstelle eine Funktion mit zwei Parametern: 
-- Alle Speisen die billiger als der Durchschnittspreis aller Speisen sind, 
-- sollen um einen fixen Betrag erhöht werden, alle Speisen die teurer sind,
-- sollen um einen Prozentwert erhöht werden.
CREATE OR REPLACE FUNCTION average() RETURNS NUMERIC AS $$
SELECT AVG(preis) FROM speise;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION preish(INTEGER,INTEGER) RETURNS VOID AS $$
UPDATE speise SET preis =(preis+$1) WHERE preis<average();
UPDATE speise SET preis = preis * (100 + $2) / 100 WHERE preis>average();
$$ LANGUAGE SQL;

SELECT * FROM speise;

-- A03: Der Tagesumsatz für einen bestimmten Kellner soll für den aktuellen Tag ermittelt werden (Kellner-Nr via Parameter, 
-- aktueller Tag via CURRENT_DATE).
CREATE OR REPLACE FUNCTION kellnerumsatz(INTEGER) RETURNS NUMERIC AS $$
SELECT SUM(speise.preis*bestellung.anzahl) FROM speise, rechnung, bestellung 
WHERE speise.snr=bestellung.snr
AND rechnung.rnr=bestellung.rnr
AND rechnung.knr = $1
AND rechnung.status='bezahlt'
AND rechnung.datum = CURRENT_DATE;
$$ LANGUAGE SQL;

SELECT name AS "kellnername", kellnerumsatz(knr) AS "umsatz" FROM kellner WHERE kellnerumsatz(knr) IS NOT NULL;

--A04: MWST
CREATE OR REPLACE FUNCTION getMWst(speise)
RETURNS NUMERIC AS $$
SELECT round($1.preis *0.2,2);
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION getBrutto(speise)
RETURNS NUMERIC AS $$
SELECT round($1.preis *1.2,2);
$$ LANGUAGE SQL;

SELECT bezeichnung,preis AS netto,getBrutto(speise.*) AS Brutto,getMWst(speise.*) AS MWST FROM speise;

--A05: nie bestellt
CREATE OR REPLACE FUNCTION nieBestellt()
RETURNS SETOF TEXT AS $$
 SELECT bezeichnung FROM speise WHERE speise.snr NOT IN (SELECT snr FROM bestellung)
$$ LANGUAGE SQL;

SELECT nieBestellt();

--A06: Kellnerdaten

CREATE OR REPLACE FUNCTION anzahlRechnung(kellner)
RETURNS BIGINT AS $$
SELECT COUNT(rechnung.rnr) FROM rechnung WHERE $1.knr=rechnung.knr;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION letzteRechnung(kellner)
RETURNS CHAR AS $$
SELECT rechnung.status FROM rechnung WHERE $1.knr=rechnung.knr AND datum=(SELECT MAX(datum) FROM rechnung WHERE $1.knr=rechnung.knr);
$$ LANGUAGE SQL;

SELECT name AS Kellnername,anzahlRechnung(kellner) AS Anzahl_der_Rechnungen,letzteRechnung(kellner) AS Status_der_spaetesten_Rechnung FROM kellner;

--A07: Tagesumsatz aller Kellner

UPDATE rechnung SET datum=CURRENT_DATE WHERE rnr=1;
UPDATE rechnung SET datum=CURRENT_DATE WHERE rnr=3;
UPDATE rechnung SET datum=CURRENT_DATE WHERE rnr=5;

CREATE OR REPLACE FUNCTION kumsatz(INTEGER) RETURNS NUMERIC AS $$
SELECT SUM(speise.preis*bestellung.anzahl) FROM speise, rechnung, bestellung 
WHERE speise.snr=bestellung.snr
AND rechnung.rnr=bestellung.rnr
AND rechnung.knr = $1
AND rechnung.status='bezahlt'
AND rechnung.datum = CURRENT_DATE;
$$ LANGUAGE SQL;

CREATE TABLE usatz (
 kname CHARACTER,
 umsatz NUMERIC
);

CREATE OR REPLACE FUNCTION tagesumsatz() RETURNS SETOF usatz AS $$
SELECT name,kumsatz(kellner.knr) AS "Tagesumsatz" FROM kellner;
$$ LANGUAGE SQL;

SELECT * FROM tagesumsatz();
