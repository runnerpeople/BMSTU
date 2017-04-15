use master;
go

if DB_ID (N'Museum_DB') is not null
drop database Museum_DB;
go
create database Museum_DB
on (
	NAME = museumDB_dat,
	FILENAME = 'C:\Databases\DB11\museum_dat.mdf',
	SIZE = 10MB,
	MAXSIZE = 150MB,
	FILEGROWTH = 10MB
)
log on (
	NAME = museumDB_log,
	FILENAME = 'C:\Databases\DB11\museum_log.ldf',
	SIZE = 5MB,
	MAXSIZE = 20MB,
	FILEGROWTH = 5MB
);
go 

alter database Museum_DB
	add filegroup Museum_FG
go

alter database Museum_DB
	add file (
		NAME = museumDB_dat1,
		FILENAME = 'C:\Databases\DB11\museum_dat1.ndf',
		SIZE = 10MB,
		MAXSIZE = 100MB,
		FILEGROWTH = 5MB
	)
	to filegroup Museum_FG
go

alter database Museum_DB
	modify filegroup Museum_FG default;
go

-- Database recovery --
/* use master;
go

restore database Museum_DB
	from disk = 'C:\Databases\BackUP\museum_backup.bak'
	with replace
go */


-- SET ANSI_NULLS OFF
-- go

use Museum_DB;
go 

if OBJECT_ID(N'Uniq_Museum',N'UQ') IS NOT NULL
	ALTER TABLE Museum DROP CONSTRAINT Uniq_Museum
go

if OBJECT_ID(N'Museum',N'U') IS NOT NULL
	DROP TABLE Museum;
go

CREATE TABLE Museum (
	museum_id int IDENTITY(1,1) PRIMARY KEY,
	name nvarchar(100) NOT NULL,
	city nchar(50) NOT NULL,
	street nchar(70) NOT NULL,
	house numeric(3) NOT NULL,
	phone_number numeric(11) NOT NULL,
	website nchar(50) NULL DEFAULT (N'google.com'),
	CONSTRAINT Uniq_museum UNIQUE (name)
	);
go

if OBJECT_ID(N'Uniq_author',N'UQ') IS NOT NULL
	ALTER TABLE Author DROP CONSTRAINT Uniq_author
go

if OBJECT_ID(N'Check_author',N'C') IS NOT NULL
	ALTER TABLE Author DROP CONSTRAINT Check_author
go

if OBJECT_ID(N'Author',N'U') is NOT NULL
	DROP TABLE Author;
go

CREATE TABLE Author (
	author_id int IDENTITY(0,1) PRIMARY KEY,
	surname nchar(30) NOT NULL,
	name nchar(30) NOT NULL,
	date_of_birth numeric(4) NULL CHECK (date_of_birth<2015),
	date_of_death numeric(4) NULL CHECK (date_of_death<2015), 
	biography nvarchar(1000) DEFAULT ('Unknown'),
	CONSTRAINT Uniq_author  UNIQUE (surname,name),
	CONSTRAINT Check_author CHECK (date_of_birth<date_of_death)
	);
go

if OBJECT_ID(N'Uniq_exhibit',N'UQ') IS NOT NULL
	ALTER TABLE Exhibit DROP CONSTRAINT Uniq_exhibit
go

if OBJECT_ID(N'FK_Exhibit_Author',N'F') IS NOT NULL
	ALTER TABLE Exhibit DROP CONSTRAINT FK_Exhibit_Author
go

if OBJECT_ID(N'FK_Exhibit_Museum',N'F') IS NOT NULL
	ALTER TABLE Museum DROP CONSTRAINT FK_Exhibit_Museum
go

if OBJECT_ID(N'Exhibit',N'U') IS NOT NULL
	DROP TABLE Exhibit;
go

CREATE TABLE Exhibit (
	exhibit_id int IDENTITY(1,1) PRIMARY KEY,
	name nvarchar(100) NOT NULL,
	article_code numeric(13) NOT NULL, 
	creation_year numeric(4) NULL,
	cost_of money NULL,
	type_exhibit nchar(70) NOT NULL CHECK (type_exhibit IN (N'Скульптура',N'Живопись',N'Иконопись',N'Графика',N'Драгоценности',N'Одежда',N'Иное')),
	author_id int NOT NULL DEFAULT(0),
	museum_id int NULL,
	CONSTRAINT FK_Exhibit_Author FOREIGN KEY (author_id) REFERENCES Author (author_id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT FK_Exhibit_Museum FOREIGN KEY (museum_id) REFERENCES Museum (museum_id) ON DELETE SET NULL ON UPDATE SET NULL,
	CONSTRAINT Uniq_exhibit UNIQUE (article_code)
	);
go

if OBJECT_ID(N'Uniq_user',N'UQ') IS NOT NULL
	ALTER TABLE Users DROP CONSTRAINT Uniq_user
go

if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	userID int IDENTITY(0,1) PRIMARY KEY,
	e_mail varchar(50) NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	phone numeric(11) NULL,
	CONSTRAINT Uniq_user UNIQUE (e_mail)
	);
go


if OBJECT_ID(N'Uniq_excursion',N'UQ') IS NOT NULL
	ALTER TABLE Excursion DROP CONSTRAINT Uniq_excursion
go

if OBJECT_ID(N'FK_Excursion_Museum',N'F') IS NOT NULL
	ALTER TABLE Excursion DROP CONSTRAINT FK_Excursion_Museum
go

if OBJECT_ID(N'Excursion',N'U') IS NOT NULL
	DROP TABLE Excursion;
go

CREATE TABLE Excursion (
	excursion_id int IDENTITY(1,1) PRIMARY KEY,
	name nvarchar(100) NOT NULL,
	duration int NULL, 
	description_excursion nvarchar(1000) NULL DEFAULT(N'Unknown'),
	museum_id int NOT NULL,
	CONSTRAINT FK_Excursion_Museum FOREIGN KEY (museum_id) REFERENCES Museum (museum_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Uniq_excursion UNIQUE (name)
	);
go


if OBJECT_ID(N'Uniq_ticket',N'UQ') IS NOT NULL
	ALTER TABLE Ticket DROP CONSTRAINT Uniq_ticket
go

if OBJECT_ID(N'FK_Ticket_Excursion',N'F') IS NOT NULL
	ALTER TABLE Ticket DROP CONSTRAINT FK_Ticket_Excursion
go

if OBJECT_ID(N'FK_Ticket_User',N'F') IS NOT NULL
	ALTER TABLE Ticket DROP CONSTRAINT FK_Ticket_User
go

if OBJECT_ID(N'Ticket',N'U') IS NOT NULL
	DROP TABLE Ticket;
go

CREATE TABLE Ticket (
	ticket_id int IDENTITY(1,1) PRIMARY KEY,
	ticket_number int NOT NULL,
	date_ticket date NOT NULL,
	time_ticket time NOT NULL, 
	cost_of smallmoney NULL CHECK (cost_of > 0.00),
	excursion_id int NOT NULL,
	userID int NULL,
	CONSTRAINT FK_Ticket_Excursion FOREIGN KEY (excursion_id) REFERENCES Excursion (excursion_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_Ticket_User FOREIGN KEY (userID) REFERENCES Users (userID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Uniq_ticket UNIQUE (ticket_number)
	);
go


if OBJECT_ID(N'UserView',N'V') is NOT NULL
	DROP VIEW UserView;
go

CREATE VIEW UserView AS
	SELECT U.e_mail as e_mail, U.surname as surname_user, U.name as name_user,
		   T.date_ticket as date_ticket, T.time_ticket as time_ticket,T.cost_of as cost_of_ticket,
		   E.name as name_excursion, E.duration as duration_excursion
	FROM Users as U
	INNER JOIN Ticket as T ON T.userID = U.userID
	INNER JOIN Excursion as E ON E.excursion_id = T.excursion_id
go


if OBJECT_ID(N'ExhibitIndexView',N'V') is NOT NULL
	DROP VIEW ExhibitIndexView;
go

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'IX_Exhibit')  
    DROP INDEX IX_Exhibit ON Exhibit;  
go

CREATE VIEW ExhibitIndexView 
WITH SCHEMABINDING 
AS
	SELECT E.name as exhibit_name, E.creation_year as creation_year, E.type_exhibit as type_exhibit,
		   A.surname as surname_author, A.name as name_author, A.date_of_birth as date_of_birth_author, A.date_of_death as date_of_death_author,
		   M.name as museum_name, M.city as museum_city
	FROM dbo.Exhibit as E
	INNER JOIN dbo.Author as A ON E.author_id = A.author_id
	INNER JOIN dbo.Museum as M ON E.museum_id = M.museum_id
go

CREATE UNIQUE CLUSTERED INDEX IX_Exhibit 
    ON ExhibitIndexView (creation_year,type_exhibit);
go

-- Triggers --

IF OBJECT_ID(N'Update_Museum',N'TR') IS NOT NULL
	DROP TRIGGER Update_Museum
go

CREATE TRIGGER Update_Museum
	ON Museum
	AFTER UPDATE
AS
	BEGIN

		IF (UPDATE(street) OR (UPDATE(house) OR (UPDATE(city))))
			BEGIN;
				EXEC sp_addmessage 50001, 15,N'Изменение местоположения музея невозможно! Используйте возможность в 2 шага: удаления данного музея и создания ей новой записи таблице',@lang='us_english',@replace='REPLACE';
				RAISERROR(50001,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					museum_id int PRIMARY KEY,
					add_name nvarchar(100), add_phone_number numeric(11),add_website nchar(50),
					delete_name nvarchar(1000), delete_phone_number numeric(11),delete_website nchar(50)
				);

				INSERT INTO @temp_table(museum_id,add_name,add_phone_number,add_website,
										          delete_name,delete_phone_number,delete_website)
				SELECT A.museum_id, A.name,A.phone_number,A.website,
					                 B.name,B.phone_number,B.website
				FROM inserted A
				INNER JOIN deleted B ON A.museum_id = B.museum_id

				IF UPDATE(name)
					PRINT N'Была переназван музей(и)'
				IF UPDATE(phone_number) 
					PRINT N'Был изменен телефон'
				IF UPDATE(website) AND EXISTS (SELECT TOP 1 delete_website FROM @temp_table WHERE delete_website IS NULL)
					PRINT N'Был добавлен сайт'
				IF UPDATE(website)
					PRINT N'Был изменен сайт'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' музеев'
				ELSE
					PRINT N'у 1 музея'
		END;
	END
go


IF OBJECT_ID(N'Update_Exhibit',N'TR') IS NOT NULL
	DROP TRIGGER Update_Exhibit
go

CREATE TRIGGER Update_Exhibit
	ON Exhibit
	AFTER UPDATE
AS
	BEGIN
		
		IF (UPDATE(article_code) OR UPDATE(name)
			OR (UPDATE(creation_year) AND EXISTS (SELECT TOP 1 creation_year FROM deleted WHERE creation_year is not NULL))
			OR UPDATE(author_id) OR UPDATE(type_exhibit))
			BEGIN;
				EXEC sp_addmessage 50002, 15,N'Запрещено изменение данных о экспонате в следствие нарушения целостности! По причине этого, воспользуйтесь созданием нового экспоната или же удалением экспоната',@lang='us_english',@replace='REPLACE';
				RAISERROR(50002,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					exhibit_id int PRIMARY KEY,
					add_creation_year numeric(4), add_cost_of money,add_museum_id int,
					delete_creation_year numeric(4), delete_cost_of money,delete_museum_id int
				);

				INSERT INTO @temp_table(exhibit_id,add_creation_year,add_cost_of,add_museum_id,
										          delete_creation_year,delete_cost_of,delete_museum_id)
				SELECT A.exhibit_id, A.creation_year,A.cost_of,A.museum_id,
					                 B.creation_year,B.cost_of,B.museum_id
				FROM inserted A
				INNER JOIN deleted B ON A.exhibit_id = B.exhibit_id

				IF UPDATE(creation_year) AND EXISTS (SELECT TOP 1 delete_creation_year FROM @temp_table WHERE delete_creation_year IS NULL)
					PRINT N'Был добавлен год создания экспоната'
				IF UPDATE(cost_of)
					PRINT N'Была добавлена стоимость экспоната'
				IF UPDATE(cost_of) AND EXISTS (SELECT TOP 1 cost_of FROM deleted WHERE cost_of is not NULL)
					PRINT N'Была изменена стоимость экспоната'
				IF UPDATE(museum_id) AND EXISTS (SELECT TOP 1 museum_id FROM deleted WHERE museum_id is NULL)
					PRINT N'Экспонат был перемещен со склада в музей'
				IF UPDATE(museum_id) AND EXISTS (SELECT TOP 1 add_museum_id FROM @temp_table WHERE add_museum_id IS NULL)
									 AND EXISTS (SELECT TOP 1 delete_museum_id FROM @temp_table WHERE delete_museum_id IS NOT NULL)
					PRINT N'Экспонат был перемещен с музея на склад'
				IF UPDATE(museum_id) AND EXISTS (SELECT TOP 1 delete_museum_id FROM @temp_table WHERE delete_museum_id IS NOT NULL)
					PRINT N'Экспонат был перемещен с одного музея в другой музей'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' экспонатов'
				ELSE
					PRINT N'у 1 экспоната'
		END;
	END
go

IF OBJECT_ID(N'Delete_Exhibit',N'TR') IS NOT NULL
	DROP TRIGGER Delete_Exhibit
go

CREATE TRIGGER Delete_Exhibit
	ON Exhibit
	INSTEAD OF DELETE
AS
	BEGIN
		UPDATE Exhibit SET museum_id = NULL WHERE exhibit_id IN (SELECT exhibit_id FROM deleted)
		IF (SELECT DISTINCT COUNT(*) FROM deleted) > 1
			PRINT 'Экспонаты перемещены на склад'
		ELSE
			PRINT 'Экспонат перемещен на склад'
	END
go

IF OBJECT_ID(N'Update_Author',N'TR') IS NOT NULL
	DROP TRIGGER Update_Author
go

CREATE TRIGGER Update_Author
	ON Author
	AFTER UPDATE
AS
	BEGIN

		IF (UPDATE(surname) OR UPDATE(name)
			OR (UPDATE(date_of_birth) AND EXISTS (SELECT TOP 1 date_of_birth FROM deleted WHERE date_of_birth is not NULL))
			OR (UPDATE(date_of_death) AND EXISTS (SELECT TOP 1 date_of_death FROM deleted WHERE date_of_death is not NULL)))
			BEGIN;
				EXEC sp_addmessage 50003, 15,N'Запрещено изменение данных об авторе экспоната в следствие нарушения целостности! По причине этого, воспользуйтесь созданием новой автора или же удалением существующего',@lang='us_english',@replace='REPLACE';
				RAISERROR(50003,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					museum_id int PRIMARY KEY,
					add_date_of_birth numeric(4), add_date_of_death numeric(4),add_biography nvarchar(1000),
					delete_date_of_birth numeric(4), delete_date_of_death numeric(4),delete_biography nvarchar(1000)
				);

				INSERT INTO @temp_table(museum_id,add_date_of_birth,add_date_of_death,add_biography,
										          delete_date_of_birth,delete_date_of_death,delete_biography)
				SELECT A.author_id, A.date_of_birth,A.date_of_death,A.biography,
					                 B.date_of_birth,B.date_of_death,B.biography
				FROM inserted A
				INNER JOIN deleted B ON A.author_id = B.author_id

				IF UPDATE(date_of_birth)
					PRINT N'Была добавлена информация о годе рождения'
				IF UPDATE(date_of_death) 
					PRINT N'Была добавлена информация о годе смерти'
				IF UPDATE(biography)
					PRINT N'Была обновлена биография'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' авторов'
				ELSE
					PRINT N'у 1 автора'
		END;
	END
go

IF OBJECT_ID(N'Delete_Author',N'TR') IS NOT NULL
	DROP TRIGGER Delete_Author
go

CREATE TRIGGER Delete_Author
	ON Author
	INSTEAD OF DELETE
AS
	BEGIN
		IF EXISTS (SELECT TOP 1 author_id FROM deleted WHERE author_id = 0)
			BEGIN;
				EXEC sp_addmessage 50004, 15,N'Удаление "Неизвестного автора" невозможно!',@lang = 'us_english', @replace='REPLACE';
				RAISERROR(50004,15,-1)
			END;
		DELETE FROM Author WHERE author_id IN (SELECT author_id FROM deleted)
	END
go

IF OBJECT_ID(N'Update_Excursion',N'TR') IS NOT NULL
	DROP TRIGGER Update_Excursion
go

CREATE TRIGGER Update_Excursion
	ON Excursion
	AFTER UPDATE
AS
	BEGIN
		DECLARE @temp_table TABLE (
			excursion_id int PRIMARY KEY,
			add_name nvarchar(100), add_duration int,add_description_excursion nvarchar(1000),add_museum_id int,
			delete_name nvarchar(100), delete_duration int,delete_description_excursion nvarchar(1000), delete_museum_id int
		);

		INSERT INTO @temp_table(excursion_id,add_name,add_duration,add_description_excursion,add_museum_id,
										    delete_name,delete_duration,delete_description_excursion, delete_museum_id)
		SELECT A.excursion_id, A.name,A.duration,A.description_excursion,A.museum_id,
					            B.name,B.duration,B.description_excursion,B.museum_id
		FROM inserted A
		INNER JOIN deleted B ON A.excursion_id = B.excursion_id

		IF UPDATE(name) OR UPDATE(duration) OR UPDATE(description_excursion)
			PRINT N'Была обновлена информация о экскурсии'
		IF UPDATE(museum_id) 
			PRINT N'Данная экскурсия будет проведена в другом музее'

		DECLARE @number int;
		SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
		IF @number > 1
			PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' экскурсий'
		ELSE
			PRINT N'у 1 экскурсии'
	END;
go


IF OBJECT_ID(N'random_number',N'FN') IS NOT NULL
	DROP FUNCTION random_number
go

IF OBJECT_ID(N'view_number',N'V') IS NOT NULL
	DROP VIEW view_number
go

CREATE VIEW view_number AS
	SELECT CAST(CAST(NEWID() AS binary(3)) AS INT) AS NextID
go

-- Возвращает случайное число в интервале [a;b]
CREATE FUNCTION random_number(@a int,@b int)
	RETURNS int
	AS
		BEGIN
			DECLARE @number int
			SELECT TOP 1 @number=NextID from view_number
			SET @number = @number % @b + @a
			RETURN (@number)
		END;
go

IF OBJECT_ID(N'dbo.select_proc_with_add', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_proc_with_add
GO

CREATE PROCEDURE dbo.select_proc_with_add
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR 
	FORWARD_ONLY STATIC FOR
	SELECT E.name,E.duration,
		   M.name,M.city, M.street, M.house,M.phone_number,
		   dbo.random_number(1,100) as rating
	FROM Excursion E
	INNER JOIN Museum M on E.museum_id = M.museum_id
	OPEN @cursor;

GO

IF OBJECT_ID(N'city_exhibit',N'FN') IS NOT NULL
	DROP FUNCTION city_exhibit
go

CREATE FUNCTION city_exhibit(@a nvarchar(100))
	RETURNS nvarchar(100)
	AS
		BEGIN
			DECLARE @result nvarchar(100)
			SET @result = (SELECT M.city 
						   FROM Exhibit E
						   INNER JOIN Museum M ON E.museum_id = M.museum_id
						   WHERE E.name = @a)
			RETURN (@result);
		END;
go


-- Insert values on tables --

--SET IDENTITY_INSERT dbo.Author ON;
--SET IDENTITY_INSERT dbo.Excursion ON;
--SET IDENTITY_INSERT dbo.Exhibit ON;
--SET IDENTITY_INSERT dbo.Museum ON;
--SET IDENTITY_INSERT dbo.Ticket ON;
--SET IDENTITY_INSERT dbo.Users ON;
--go


INSERT INTO Museum(name,city,street,house,phone_number)
VALUES (N'Третьяковская галерея',N'Москва',N'Лаврушинский пер.', 10,84992307788),
	   (N'Государственный Эрмитаж',N'Санкт-Петербург',N'Дворцовая пл.',2,88127109079),
	   (N'Оружейная палата',N'Москва',N'Московский Кремль',1,84956970349),
	   (N'Алмазный фонд',N'Москва',N'Кремль',1,84956292036),
	   (N'Кунсткамера',N'Санкт-Петербург',N'Университетская наб.', 3,88123281412),
	   (N'Исторический музей',N'Москва',N'Красная пл.', 1,84956924019)

INSERT INTO Author(surname,name,date_of_birth,date_of_death)
VALUES (N'Неизвестный',N'автор',NULL,NULL),
	   (N'Брюллов',N'Карл',1799,1852),
	   (N'Шубин',N'Федот',1740,1805),
	   (N'Рейн',N'Рембрандт Харменс',1606,1669),
	   (N'Вигстрём',N'Хенрик',1862,1923),
	   (N'Экарт',N'Георг-Фридрих',NULL,NULL),
	   (N'Рюйш',N'Фредерик',1638,1731),
	   (N'Фёдоров',N'Иван',1520,1583)


INSERT INTO Exhibit(name,article_code,creation_year,type_exhibit,author_id,museum_id)
VALUES (N'Всадница',111739707,1832,N'Живопись',1,1),
	   (N'Екатерина II',478484108,1770,N'Скульптура',2,1),
	   (N'Возвращение блудного сына',323779001,1665,N'Живопись',3,2),

	   -- Предположительно найдена при раскопках в Риме в феврале 1719 года -- 
	   (N'Венера Таврическая',574648603,1719,N'Скульптура',0,2),

	   (N'Шапка Мономаха',319733143,1518,N'Одежда',0,3),
	   (N'Модель Александровского Дворца в яйце из нефрита',440695975,1908,N'Драгоценности',4,3),
	   (N'Большая императорская корона',900782316,1762,N'Одежда',5,4),
	   (N'Держава',259607151,1762,N'Драгоценности',5,4),
	   (N'Часть инъецированной слизистой пищевода на веточке растения',585212548,1846,N'Иное',6,5),
	   (N'Апостол',561475366,1762,N'Иное',7,6)

INSERT INTO Users(e_mail,surname,name)
VALUES (N'unknown',N'No',N'Name'),
	   (N'gosha8352@gmail.com',N'Иванов',N'Георгий'),
	   (N'svet_peshek@gmail.com',N'Чесноков',N'Никита'),
	   (N'dimastii428017@mail.ru',N'Кудряшов',N'Дмитрий'),
	   (N'el_ki96@ya.ru',N'Кириллова',N'Елена')

INSERT INTO Excursion(name,duration,museum_id)
VALUES (N'Экскурсия по Третьяковской Галерее',90,1),
	   (N'Экскурсия в Государственный Эрмитаж',300,2),
	   (N'Государственная Оружейная палата',75,3),
	   (N'Посещение выставки Алмазного фонда',20,4),
	   (N'Моя Кунсткамера',90,5),
	   (N'Шедевры Исторического Музея',90,6)


INSERT INTO Ticket(ticket_number,date_ticket,time_ticket,cost_of,excursion_id,userID)
VALUES (41807509,CONVERT(date,N'10-08-2016'),CONVERT(time,N'11:40:00'),1500,1,1),
	   (61894727,CONVERT(date,N'12-11-2016'),CONVERT(time,N'16:50:00'),1500,1,2),

	   (94390390,CONVERT(date,N'18-03-2016'),CONVERT(time,N'14:20:00'),1100,2,3),
	   (99281089,CONVERT(date,N'27-07-2016'),CONVERT(time,N'10:10:00'),1100,2,4),

	   (47774902,CONVERT(date,N'31-01-2016'),CONVERT(time,N'12:20:00'),700,3,1),
	   (19464087,CONVERT(date,N'29-09-2016'),CONVERT(time,N'15:10:00'),700,3,2),
	   (93875514,CONVERT(date,N'10-04-2016'),CONVERT(time,N'10:45:00'),700,3,3),
	   (58214267,CONVERT(date,N'20-11-2016'),CONVERT(time,N'15:50:00'),700,3,4),

	   (55266813,CONVERT(date,N'22-07-2016'),CONVERT(time,N'13:05:00'),500,4,4),

	   (64281567,CONVERT(date,N'29-09-2016'),CONVERT(time,N'15:30:00'),500,5,3),

	   (46819769,CONVERT(date,N'04-01-2016'),CONVERT(time,N'13:15:00'),400,6,1),	
	   (75817061,CONVERT(date,N'20-11-2016'),CONVERT(time,N'16:00:00'),400,6,2),	
	   (47668137,CONVERT(date,N'06-09-2016'),CONVERT(time,N'14:50:00'),400,6,3)	
go

SELECT * FROM UserView
go

SELECT * FROM ExhibitIndexView
go

UPDATE Users SET e_mail = N'malenkiy__budda@mail.ru' WHERE name = N'Никита'
go

UPDATE Ticket SET cost_of = cost_of * 1.15 WHERE date_ticket BETWEEN CONVERT(date,N'01-11-2016') AND CONVERT(date,N'31-12-2016')
go

UPDATE Excursion SET duration = duration + 15 WHERE excursion_id IN (1,3,5)
go

--DELETE FROM Users WHERE name LIKE '%ий'
--go

SELECT * FROM Users
go 

SELECT COUNT(*) AS count_tickets FROM UserView 
go

SELECT COUNT(DISTINCT e_mail) AS count_distinct_email_tickets FROM UserView
go

-- Возвращает null-записи
SET ANSI_NULLS OFF
SELECT * FROM Author WHERE date_of_birth = NULL
go


-- Не возвращает null-записи
SET ANSI_NULLS ON
SELECT * FROM Author WHERE date_of_birth = NULL
go

SELECT * FROM UserView
ORDER BY date_ticket ASC
go

SELECT SUM(cost_of_ticket) as sum_count_of
FROM UserView
GROUP BY surname_user,name_user
HAVING name_user IN (N'Никита',N'Георгий')
go

/* SELECT AVG(cost_of_ticket) as sum_count_of
FROM UserView
GROUP BY surname_user,name_user
HAVING name_user IN (N'Никита',N'Георгий')
go */


SELECT E.name as exhibit_name, E.creation_year as creation_year, E.type_exhibit as type_exhibit,
	   A.surname as surname_author, A.name as name_author, A.date_of_birth as date_of_birth_author, A.date_of_death as date_of_death_author,
	   M.name as museum_name, M.city as museum_city
FROM dbo.Exhibit as E
LEFT JOIN dbo.Author as A ON E.author_id = A.author_id
RIGHT JOIN dbo.Museum as M ON E.museum_id = M.museum_id
FULL JOIN dbo.Excursion as Ex ON Ex.museum_id = M.museum_id
go

--SELECT * FROM Museum WHERE name = N'Третьяковская галерея'
--UNION SELECT * FROM Museum WHERE name = N'Государственный Эрмитаж'

--SELECT * FROM Museum
--UNION ALL SELECT * FROM Museum

--SELECT * FROM Museum
--UNION ALL SELECT * FROM Museum WHERE name = N'Оружейная палата'

--SELECT * FROM Museum
--UNION ALL SELECT * FROM Museum WHERE name = N'Алмазный фонд'

--go

-- Database recovery create backup_file
/* use Museum_DB;
go

backup database Museum_DB
	to disk = 'C:\Databases\BackUP\museum_backup.bak'
	with format,
	name = 'Full Backup of Museum_DB'
go */
