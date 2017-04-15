-- Cоздание вертикально фрагментированных таблиц средствами СУБД SQL Server 2012

use master;
go
if DB_ID (N'lab14_1') is not null
drop database lab14_1;
go
create database lab14_1
on (
NAME = lab141dat,
FILENAME = 'C:\Databases\DB14\lab141dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab141log,
FILENAME = 'C:\Databases\DB14\lab141log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use master;
go
if DB_ID (N'lab14_2') is not null
drop database lab14_2;
go
create database lab14_2
on (
NAME = lab142dat,
FILENAME = 'C:\Databases\DB14\lab142dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab142log,
FILENAME = 'C:\Databases\DB14\lab142log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

-- 1.Создать в базах данных пункта 1 задания 13 таблицы, содержащие вертикально фрагментированные данные.

use lab14_1;
go

if OBJECT_ID(N'Cinema',N'U') is NOT NULL
	DROP TABLE Cinema;
go

CREATE TABLE Cinema (
	cinema_id int NOT NULL PRIMARY KEY,
	producer nchar(50) NOT NULL,
	name nchar(50) NOT NULL,
	genre nchar(20) NOT NULL CHECK (genre IN (N'Комедия',N'Боевик',N'Ужасы',N'Фантастика',
											N'Драма', N'Семейный',N'Мультфильм')),
	--year_issue numeric(4) NOT NULL CHECK (year_issue>1900 AND year_issue<2017),
	--budget money NULL CHECK (budget > 0.0)
)
go

use lab14_2;
go

if OBJECT_ID(N'Cinema',N'U') is NOT NULL
	DROP TABLE Cinema;
go

CREATE TABLE Cinema (
	cinema_id int NOT NULL PRIMARY KEY,
	--producer nchar(50) NOT NULL,
	--name nchar(50) NOT NULL,
	--genre nchar(20) NOT NULL CHECK (genre IN (N'Комедия',N'Боевик',N'Ужасы',N'Фантастика',
	--										    N'Драма', N'Семейный',N'Мультфильм')),
	year_issue numeric(4) NOT NULL CHECK (year_issue>1900 AND year_issue<2017),
	budget money NULL CHECK (budget > 0.0)
)
go

-- Параметр сервера "lazy schema validation" недоступен в данном выпуске SQL Server.
-- EXEC sp_serveroption 'OtherServer','lazy schema validation', 'true'
-- go

-- 2.Создать необходимые элементы базы данных (представления, триггеры), 
-- обеспечивающие работу с данными вертикально фрагментированных таблиц 
-- (выборку, вставку, изменение, удаление). 

if OBJECT_ID(N'CinemaView',N'V') is NOT NULL
	DROP VIEW CinemaView;
go

CREATE VIEW CinemaView AS
	SELECT A.*, B.year_issue,B.budget
	FROM lab14_1.dbo.Cinema A, lab14_2.dbo.Cinema B
	WHERE A.cinema_id = B.cinema_id
go

IF OBJECT_ID(N'InsertCinemaView',N'TR') IS NOT NULL
	DROP TRIGGER InsertCinemaView
go

CREATE TRIGGER InsertCinemaView
	ON CinemaView
	INSTEAD OF INSERT 
AS
	BEGIN
		IF EXISTS (SELECT A.cinema_id
					   FROM lab14_1.dbo.Cinema AS A,
							lab14_2.dbo.Cinema AS B,
							inserted AS I
					   WHERE A.name = I.name AND A.producer = I.name AND B.year_issue = I.year_issue)
			BEGIN
				EXEC sp_addmessage 50003, 15,N'Добавление дубликата фильма!',@lang='us_english',@replace='REPLACE';
				RAISERROR(50003,15,-1)
			END
		ELSE
			IF EXISTS (SELECT A.cinema_id 
						FROM lab14_1.dbo.Cinema AS A, 
							  inserted AS I
					   WHERE A.cinema_id = I.cinema_id)
				BEGIN
					EXEC sp_addmessage 50004, 15,N'ID занят! Попробуйте другой',@lang='us_english',@replace='REPLACE';
					RAISERROR(50004,15,-1)
				END
			ELSE
				BEGIN
					INSERT INTO lab14_1.dbo.Cinema(cinema_id,producer,name,genre)
					SELECT cinema_id,producer,name,genre FROM inserted

					INSERT INTO lab14_2.dbo.Cinema(cinema_id,year_issue,budget)
					SELECT cinema_id,year_issue,budget FROM inserted
				END
	END
go

INSERT INTO CinemaView(cinema_id,producer,name,genre,year_issue)
VALUES (1,N'Джон Лукас',N'21 и больше', N'Комедия', 2013),
	   (2,N'Эрик Дарнелл',N'Мадагаскар', N'Мультфильм', 2005),
	   (3,N'Джон Мактирнан',N'Крепкий Орешек', N'Боевик', 1988),
	   (4,N'Уэс Крэйвен',N'Крик', N'Ужасы', 1996),
	   (5,N'Джеймс Кэмерон',N'Аватар', N'Фантастика', 2009),
	   (6,N'Джерри Цукер',N'Привидение', N'Драма', 1990),
	   (7,N'Крис Коламбус',N'Один дома', N'Семейный', 1990)
go

SELECT * FROM CinemaView
go

IF OBJECT_ID(N'UpdateCinemaView',N'TR') IS NOT NULL
	DROP TRIGGER UpdateCinemaView
go

CREATE TRIGGER UpdateCinemaView
	ON CinemaView
	INSTEAD OF UPDATE
AS
	
	BEGIN
		IF UPDATE(cinema_id)
			BEGIN
				EXEC sp_addmessage 50001, 15,N'Запрещено изменение ID фильма',@lang='us_english',@replace='REPLACE';
				RAISERROR(50001,15,-1)
			END
		IF UPDATE(producer) OR UPDATE(name) OR UPDATE(genre) OR UPDATE(year_issue)
			BEGIN
				EXEC sp_addmessage 50002, 15,N'Запрещено изменение информации о фильме. В следствие избежания нарушения целостности, нужно воспользоваться удалением и созданием нового фильма',@lang='us_english',@replace='REPLACE';
				RAISERROR(50002,15,-1)
			END

		DECLARE @temp_table TABLE (
					cinema_id int,
					add_budget money,
					delete_budget money
		);
		INSERT INTO @temp_table(cinema_id,add_budget,delete_budget)
		SELECT A.cinema_id, A.budget,
							B.budget
		FROM inserted A
		INNER JOIN deleted B ON A.cinema_id = B.cinema_id

		UPDATE lab14_2.dbo.Cinema SET budget = inserted.budget FROM inserted WHERE inserted.cinema_id = lab14_2.dbo.Cinema.cinema_id
	END
go

UPDATE CinemaView SET budget = $10000000.00 WHERE year_issue > 2000
go

--Возникновение ошибки --
--UPDATE CinemaView SET cinema_id = 10 WHERE name=N'21 и больше'
--go

SELECT * FROM CinemaView
go

IF OBJECT_ID(N'DeleteCinemaView',N'TR') IS NOT NULL
	DROP TRIGGER DeleteCinemaView
go

CREATE TRIGGER DeleteCinemaView
	ON CinemaView
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE C FROM lab14_1.dbo.Cinema AS C INNER JOIN deleted AS d ON C.cinema_id = d.cinema_id
		DELETE C FROM lab14_2.dbo.Cinema AS C INNER JOIN deleted AS d ON C.cinema_id = d.cinema_id
	END
go

DELETE FROM CinemaView WHERE genre IN (N'Комедия',N'Боевик',N'Ужасы')
go

SELECT * FROM CinemaView
go

SELECT * FROM lab14_1.dbo.Cinema
go

SELECT * FROM lab14_2.dbo.Cinema
go
