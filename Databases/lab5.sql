-- Создание базы данных --
use master;
go
if DB_ID (N'lab5') is not null
drop database lab5;
go
create database lab5
on (
NAME = lab5dat,
FILENAME = 'C:\Databases\DB5\lab5dat.mdf',
SIZE = 10,
MAXSIZE = UNLIMITED,
FILEGROWTH = 5
)
log on (
NAME = lab5log,
FILENAME = 'C:\Databases\DB5\lab5log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

-- Создание произвольной страницы --

use lab5;
go 
if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	e_mail varchar(50) PRIMARY KEY NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL);
go

INSERT INTO USERS(e_mail,surname,name,patronymic,phone) 
VALUES ('gosha8352@gmail.com','Ivanov','George', 'Michailovich','88005553535')
go

SELECT * FROM USERS
go


-- Добавление файловой группы и файла данных --
use master;
go

alter database lab5
add filegroup lab5_fg
go

alter database lab5
add file
(
	NAME = lab5dat1,
	FILENAME = 'C:\Databases\DB5\lab5dat1.ndf',
	SIZE = 10MB,
	MAXSIZE = 100MB,
	FILEGROWTH = 5MB
)
to filegroup lab5_fg
go

-- Назначение созданной файловой группы файловой группой по умолчанию

alter database lab5
	modify filegroup lab5_fg default;
go

-- создать еще одну произвольной таблицы --
-- все объекты, создающиесяуказания файловой группы, к которой они относятся, они назначаются файловой группе по умолчанию

use lab5;
go 
if OBJECT_ID(N'Ticket',N'U') is NOT NULL
	DROP TABLE Ticket;
go

CREATE TABLE Ticket (
	ticket_number int PRIMARY KEY NOT NULL,
	date_of date NOT NULL,
	time_of numeric(4) NOT NULL,
	cost_of money NULL);
go
 
-- Необходимо вернуть primary группы - группой по умолчанию для удаление пользовательской файловой группы
-- Невозможно удаление файловой группы по умолчанию --

alter database lab5
	modify filegroup [primary] default;
go


-- удаление созданной вручную (пользовательскую) файловую группу

use lab5;
go

-- необходимо удаление всех объектов в этой группе
drop table Ticket
go

-- и также всех пользовательских (вторичных) файлов
alter database lab5
remove file lab5dat1
go

alter database lab5
remove filegroup lab5_fg;
go

-- Создание схемы, перемещение в нее одну из таблиц, удаление схему --

use lab5;
go

CREATE SCHEMA museum_schema
go

ALTER SCHEMA museum_schema TRANSFER dbo.Ticket
go

DROP TABLE museum_schema.Ticket
DROP SCHEMA museum_schema
go
