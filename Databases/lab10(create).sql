use master;
go
if DB_ID (N'lab10') is not null
drop database lab10;
go
create database lab10
on (
NAME = lab10dat,
FILENAME = 'C:\Databases\DB10\lab10dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab10log,
FILENAME = 'C:\Databases\DB10\lab10log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use lab10;
go 
if OBJECT_ID(N'CardBalance',N'U') IS NOT NULL
	DROP TABLE CardBalance;
go

CREATE TABLE CardBalance (
	card_id int PRIMARY KEY,
	name nchar(50) NULL,
	surname nchar(50) NULL,
	card_number numeric(17) NULL,
	card_type nchar(25) NOT NULL,
	balance money NOT NULL
);
go

INSERT INTO CardBalance(card_id,card_number,card_type,balance)
VALUES (1,4716669340945103,N'Visa',CAST($1500.00 as money)),
	   (2,5410198144622144,N'Mastercard',CAST($2500.00 as money)),
	   (3,6011391679890134,N'Discover',CAST($700.00 as money)),
	   (4,377819086330733,N'American Express',CAST($260.00 as money))
go