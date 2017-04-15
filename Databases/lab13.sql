-- 1. Создать две базы данных на одном экземпляре СУБД SQL Server 2012.
use master;
go
if DB_ID (N'lab13_1') is not null
drop database lab13_1;
go
create database lab13_1
on (
NAME = lab131dat,
FILENAME = 'C:\Databases\DB13\lab131dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab131log,
FILENAME = 'C:\Databases\DB13\lab131log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use master;
go
if DB_ID (N'lab13_2') is not null
drop database lab13_2;
go
create database lab13_2
on (
NAME = lab132dat,
FILENAME = 'C:\Databases\DB13\lab132dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab132log,
FILENAME = 'C:\Databases\DB13\lab132log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

-- 2. Создать в базах данных п.1. горизонтально фрагментированные таблицы.

use lab13_1;
go

if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	user_info_id int PRIMARY KEY NOT NULL,
	e_mail varchar(50) NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL,
	CONSTRAINT Seq_users_more CHECK (user_info_id <= 5)
	);
go

use lab13_2;
go

if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	user_info_id int PRIMARY KEY NOT NULL,
	e_mail varchar(50) NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL,
	CONSTRAINT Seq_users_more CHECK (user_info_id > 5)
	);
go

-- 3. Создать секционированные представления, 
-- обеспечивающие работу с данными таблиц
-- (выборку, вставку, изменение, удаление).

use lab13_1;
go

if OBJECT_ID(N'UserView',N'V') is NOT NULL
	DROP VIEW UserView;
go

CREATE VIEW UserView AS
	SELECT * FROM lab13_1.dbo.Users
	UNION ALL
	SELECT * FROM lab13_2.dbo.Users
go

use lab13_2;
go

if OBJECT_ID(N'UserView',N'V') is NOT NULL
	DROP VIEW UserView;
go

CREATE VIEW UserView AS
	SELECT * FROM lab13_1.dbo.Users
	UNION ALL
	SELECT * FROM lab13_2.dbo.Users
go

INSERT INTO UserView VALUES 
	(1,'gosha8352@gmail.com','Ivanov','George', 'Michailovich','88005553535'),
	(2,'dimachik428017@mail.ru','Kudryashov','Dima','Petrovich','8800808080'),
	(3,'vlad_vorman@ya.ru','Stachuk','Vladislav','Nikolaevich','38567863281'),
	(4,'alex_semen@ya.ru','Semennikov','Alexander','Vladislavovich','8924731913'),
	(5,'avarti@mail.ru','Averbach','Artyom','Dmitrievich','89274287742')


SELECT * FROM UserView;

SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;


DELETE FROM UserView WHERE patronymic = 'Nikolaevich'

SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;


UPDATE UserView SET e_mail = 'runnerpeople@gmail.com' WHERE name = 'George'
 
SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;
