-- Autor: Maximilian Seidl
-- Datum: 03.12.16
-- Zweck: AU01 - AU07

DROP DATABASE restaurant;
CREATE DATABASE restaurant;
use restaurant;

-- DROP TABLE IF EXISTS kellner CASCADE;
CREATE TABLE kellner (
             knr         INTEGER,
             name        VARCHAR(255),
             PRIMARY KEY (knr)
             )Engine=InnoDB;

INSERT INTO kellner VALUES (1, 'Kellner1');
INSERT INTO kellner VALUES (2, 'Kellner2');
INSERT INTO kellner VALUES (3, 'Kellner3');

-- DROP TABLE IF EXISTS speise CASCADE;
CREATE TABLE speise (
             snr         INTEGER,
             bezeichnung VARCHAR(255),
             preis       DECIMAL(6,2),
             PRIMARY KEY (snr)
             )Engine=InnoDB;

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
             )Engine=InnoDB;

INSERT INTO rechnung VALUES (1, CURDATE(), 1, 'gedruckt', 1);
INSERT INTO rechnung VALUES (2, CURDATE(), 2, 'bezahlt', 2);
INSERT INTO rechnung VALUES (3, CURDATE(), 1, 'bezahlt', 3);
INSERT INTO rechnung VALUES (4, '9-10-2016', 1, 'gedruckt', 1);
INSERT INTO rechnung VALUES (5, CURDATE(), 1, 'bezahlt', 1);
INSERT INTO rechnung VALUES (6, CURDATE(), 2, 'bezahlt', 2);
insert into rechnung values (7, '8-10-2016', 3, 'gedruckt', 2);
insert into rechnung values (8, '9-10-2016', 4, 'bezahlt', 1);


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
             )Engine=InnoDB;

INSERT INTO bestellung VALUES (9, 1, 1);
INSERT INTO bestellung VALUES (1, 1, 4);
INSERT INTO bestellung VALUES (1, 1, 5);
INSERT INTO bestellung VALUES (1, 2, 7);
INSERT INTO bestellung VALUES (1, 3, 8);
INSERT INTO bestellung VALUES (9, 4, 1);
INSERT INTO bestellung VALUES (9, 5, 1);
INSERT INTO bestellung VALUES (9, 6, 2);

-----------------------------------------------------------------------------------------------------------------------------------------

--AU01

--a: Alle Preise sollen so geändert (erhöht) werden, dass sie mit 99 Cent enden!

delimiter //
CREATE PROCEDURE preis99() 
BEGIN
UPDATE speise SET preis=ceil(preis)-0.01;
END //
delimiter ;

SELECT * FROM speise;
CALL preis99();
SELECT * FROM speise;

--Alle Rechnung zu denen es keine Bestellung gibt, sollen gelöscht werden.

delimiter //
CREATE PROCEDURE loescheRechnung()
BEGIN
     delete from rechnung where rnr not in (select rnr from bestellung);
END // 
delimiter ;

INSERT INTO rechnung VALUES (10, CURDATE(), 1, 'gedruckt', 1);

SELECT * FROM rechnung;
CALL loescheRechnung();
SELECT * FROM rechnung;

----------------------------------------------------------------------------------------------------------------------------------------

--AU02

--Erstelle eine Funktion mit zwei Parametern: 
--Alle Speisen die billiger als der Durchschnittspreis 
--aller Speisen sind, sollen um einen fixen Betrag erhöht 
--werden, alle Speisen die teurer sind, sollen um einen 
--Prozentwert erhöht werden.

delimiter //
CREATE PROCEDURE preiserhoehung(IN prozent DECIMAL(30,2),IN abpreis DECIMAL(30,2))
BEGIN
DECLARE av DECIMAL DEFAULT (SELECT avg(preis) FROM speise);
UPDATE speise SET preis = preis * (100 + prozent) / 100 where preis > av;
UPDATE speise SET preis = preis + abpreis where preis <= av;
END//
delimiter ;

SELECT * FROM speise;
CALL preiserhoehung(10,1.75);
SELECT * FROM speise;


----------------------------------------------------------------------------------------------------------------------------------------

--AU03

--Der Tagesumsatz für einen bestimmten Kellner soll für den aktuellen Tag
-- ermittelt werden (Kellner-Nr via Parameter, aktueller Tag via CURRENT_DATE).

CREATE FUNCTION tagesumsatz(kel INT) 
RETURNS DECIMAL(30,2) DETERMINISTIC
RETURN 
(select sum(preis*anzahl) from speise 
inner join bestellung on speise.snr = bestellung.snr 
inner join rechnung on rechnung.rnr = bestellung.rnr
where knr=kel AND rechnung.status like 'bezahlt%' AND rechnung.datum = CURDATE());

SELECT tagesumsatz(1);
SELECT tagesumsatz(2);
select tagesumsatz(3);


-----------------------------------------------------------------------------------------------------------------------------------------

--AU04
-- Funktionen mit komplexeren Schnittstellen
-- Beispielfunktion

CREATE FUNCTION bruttoPreis(spreis DECIMAL(30,2))
RETURNS DECIMAL(30,2) DETERMINISTIC
RETURN (SELECT spreis * 1.2);

delimiter //
CREATE PROCEDURE ausgabe()
BEGIN
Select bezeichnung, preis as "Netto", bruttoPreis(speise.preis) as "Brutto" from speise;
END //
delimiter ;

call ausgabe();

--Erstelle eine weitere Funktion zur Berechnung der MWSt. Zeige von allen Speisen den Brutto-Preis 
--als Brutto und die darin enthaltene MWSt. als Spalte MWSt an. Zusatz: Die Anzeige bei Brutto/MWSt 
--soll auf zwei Nachkommastellen beschränkt werden.

CREATE FUNCTION mwst(spreis DECIMAL(30,2))
RETURNS DECIMAL(30,2) DETERMINISTIC
RETURN
(SELECT round(spreis*0.2, 2));

delimiter //
CREATE PROCEDURE ausgabe1()
BEGIN
Select bezeichnung, bruttoPreis(speise.preis) as "Brutto", mwst(speise.preis*1.2) as "MWST" from speise;
END //
delimiter ;

call ausgabe1();

---------------------------------------------------------------------------------------------------------------------------------

--AU05
--Erstelle eine Funktion zur Anzeige der Bezeichnungen der noch nie bestellten Speisen.

delimiter //
CREATE PROCEDURE nichtbestellt()
BEGIN
Select bezeichnung from speise where snr not in (Select snr from bestellung);
END //
delimiter ;

call nichtbestellt();

--Zusatz: Erweitere die aufrufende SELECT-Anweisung so, dass das Ergebnis als Tabelle mit den Spaltenüberschriften "Bezeichnung" und "Nettopreis" --angezeigt wird.
--als Select nicht möglich, da call procedure nicht in einer select Anweißung aufgerufen werden kann.
--mögliche Variante- neue Procedure 

delimiter //
CREATE PROCEDURE ausgabe2()
BEGIN
Select bezeichnung, preis as "Nettopreis" from speise where bezeichnung in (Select bezeichnung from speise where snr not in (Select snr from bestellung));
END //
delimiter ;

call ausgabe2();

--------------------------------------------------------------------------------------------------------------------------------------------

--AU06
--Übung 1:
--Erstelle zwei Funktionen die in der SELECT-Klausel eingebettet werden, um damit zusätzliche Spalten je Kellner anzeigen zu können. Die aufrufende SELECT-Anweisung soll folgende Ausgabe produzieren:
--Kellnername, Anzahl der Rechnungen, Status der spätesten Rechnung

CREATE FUNCTION anzahlrechnungen(kel INTEGER)
RETURNS BIGINT 
RETURN
(SELECT count(rechnung.knr)
FROM rechnung
WHERE rechnung.knr = kel);

CREATE FUNCTION letzteRechnung(kel INTEGER)
RETURNS CHAR(8)
RETURN
(select status from rechnung where datum = (select max(datum) from rechnung where rechnung.knr = kel) and rechnung.knr = kel limit 1);

delimiter //
CREATE PROCEDURE ausgabe3()
BEGIN
Select distinct kellner.name, anzahlrechnungen(rechnung.knr) as 'Anzahl Rechnungen', letzterechnung(rechnung.knr) as 'Letzte Rechnung' from rechnung inner join kellner on kellner.knr = rechnung.knr order by kellner.name asc;
END //
delimiter ;

call ausgabe3();

-------------------------------------------------------------------------------------------------------------------------------------

--AU07
--Übung 1:
--Es soll eine Liste der Kellner und deren jeweiliger Tagesumsatz ausgegeben werden.
--Korrekte Lösung mit der Original-DB: nur eine Zeile mit Kellner1 und 71.00

delimiter //
CREATE PROCEDURE umsatzkellner()
BEGIN
select kellner.name,  sum(preis*anzahl) from kellner
inner join rechnung on kellner.knr = rechnung.knr 
inner join bestellung on rechnung.rnr = bestellung.rnr
inner join speise on speise.snr = bestellung.snr 
where rechnung.status = 'bezahlt' AND rechnung.datum = current_date
group by kellner.knr;
END //
delimiter ;

call umsatzkellner();