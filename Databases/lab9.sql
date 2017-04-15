use master;
go
if DB_ID (N'lab9') is not null
drop database lab9;
go
create database lab9
on (
NAME = lab9dat,
FILENAME = 'C:\Databases\DB9\lab9dat.mdf',
SIZE = 10,
MAXSIZE = UNLIMITED,
FILEGROWTH = 5
)
log on (
NAME = lab9log,
FILENAME = 'C:\Databases\DB9\lab9log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use lab9;
go 
if OBJECT_ID(N'Library',N'U') IS NOT NULL
	DROP TABLE Library;
go

if OBJECT_ID(N'Uniq_Library',N'UQ') IS NOT NULL
	ALTER TABLE Library DROP CONSTRAINT Uniq_library
go

CREATE TABLE Library (
	library_id int IDENTITY(1,1) PRIMARY KEY,
	name nchar(50) NOT NULL,
	street nchar(70) NULL,
	house numeric(3) NULL,
	phone_number numeric(11) NULL,
	website nchar(70) NULL,
	CONSTRAINT Uniq_Library UNIQUE (name)
);
go

if OBJECT_ID(N'Book',N'U') is NOT NULL
	DROP TABLE Book;
go

if OBJECT_ID(N'FK_library',N'F') IS NOT NULL
	ALTER TABLE Book DROP CONSTRAINT FK_library
go

if OBJECT_ID(N'Uniq_Book',N'UQ') IS NOT NULL
	ALTER TABLE Book DROP CONSTRAINT Uniq_bool
go

CREATE TABLE Book (
	book_id int IDENTITY(1,1) PRIMARY KEY,
	author nchar(50) NOT NULL,
	name nchar(50) NOT NULL,
	genre nchar(20) NOT NULL CHECK (genre IN (N'Роман',N'Научная фантастика',N'Драма',N'Детектив',
											N'Мистика', N'Поэзия',N'Сказка', N'Фантастика', N'Пьеса')),
	publish_year numeric(4) NOT NULL,
	cost_of smallmoney NULL CHECK (cost_of > 0),
	library_id int NULL,
	CONSTRAINT FK_library FOREIGN KEY (library_id) REFERENCES Library (library_id),
	CONSTRAINT Uniq_book UNIQUE (author,name,genre,publish_year)
	);
go

SET IDENTITY_INSERT Library ON;  
go

INSERT INTO Library(library_id,name)
VALUES (1,N'Российская государственная библиотека'),
	   (2,N'Библиотека им. Ленина'),
	   (3,N'Библиотека иностранной литературы'),
	   (4,N'Библиотека-читальня им. Тургенева')
go

INSERT INTO Library(library_id,name)
VALUES (0,N'Склад')

INSERT INTO Book(author,name,genre,publish_year,library_id)
VALUES (N'Александр Пушкин',N'Евгений Онегин', N'Роман', 1831,1),
	   (N'Жюль Верн',N'20 000 льё под водой',N'Научная фантастика', 1916,2),
	   (N'Агата Кристи',N'Убийство Роджера Экройда',N'Детектив',1926,3),
	   (N'Стивен Кинг',N'1408', N'Мистика', 1926,3),
	   (N'Корней Чуковской',N'Добрый доктор', N'Сказка', 1936,4),
	   (N'Гастон Леру',N'Призрак оперы', N'Роман', 1910,3)
go

/* SELECT * FROM Library
SELECT * FROM Book
go */

-- Для одной из таблиц пункта 2 задания 7 создать триггеры на вставку, удаление и добавление,
-- при выполнении заданных условий один из триггеров должен инициировать возникновение ошибки
-- (RAISERROR / THROW)

-- Триггеры на удаление
use lab9;
go

-- Триггер на удаление --

IF OBJECT_ID(N'Delete_library',N'TR') IS NOT NULL
	DROP TRIGGER Delete_library
go

CREATE TRIGGER Delete_library
	ON Library
	INSTEAD OF DELETE 
AS
	BEGIN
		IF EXISTS (SELECT TOP 1 library_id FROM deleted WHERE library_id = 0)
			BEGIN;
				EXEC sp_addmessage 50001, 15,N'Удаление склада невозможно!',@lang = 'us_english', @replace='REPLACE';
				RAISERROR(50001,15,-1)
			END;
		ELSE
			BEGIN;
				UPDATE Book SET library_id=0 WHERE library_id IN (SELECT library_id FROM deleted)

				-- DELETE l FROM Library AS l INNER JOIN deleted AS d ON l.library_id = d.library_id
				DELETE FROM Library WHERE library_id IN (SELECT library_id FROM deleted)
				IF (SELECT DISTINCT COUNT(*) FROM deleted) > 1
					PRINT 'Библиотеки удалены из таблицы и все книги, хранящиеся в этих библиотеках, перемещены в склад!'
				ELSE
					PRINT 'Библиотека удалена из таблицы и все книги, хранящиеся в этой библиотеке, перемещены в склад!'
			END;
	END
go

/* DELETE FROM Library WHERE library_id in (1,2,3)
SELECT * FROM Book
SELECT * FROM Library
go */


-- Триггер на обновление --

IF OBJECT_ID(N'Update_info_library',N'TR') IS NOT NULL
	DROP TRIGGER Update_info_library
go

CREATE TRIGGER Update_info_library
	ON Library
	AFTER UPDATE
AS
	BEGIN
		IF ((UPDATE(street) AND EXISTS (SELECT TOP 1 street FROM deleted WHERE street is not NULL)) 
		    OR (UPDATE(house) AND EXISTS (SELECT TOP 1 house FROM deleted WHERE house is not NULL)))
			BEGIN;
				EXEC sp_addmessage 50002, 15,N'Изменение местоположения библиотеки невозможно! Используйте возможность в 2 шага: удаления данной библиотеки и создания ей записи таблице',@lang='us_english',@replace='REPLACE';
				RAISERROR(50002,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					library_id int PRIMARY KEY,
					add_name nchar(50), add_street nchar(70),add_house numeric(3),add_phone_number numeric(11),add_website nchar(70),
					delete_name nchar(50), delete_street nchar(70),delete_house numeric(3),delete_phone_number numeric(11),delete_website nchar(70)
				);

				INSERT INTO @temp_table(library_id,add_name,add_street,add_house,add_phone_number,add_website,
										delete_name,delete_street,delete_house,delete_phone_number,delete_website)
				SELECT A.library_id, A.name,A.street,A.house,A.phone_number,A.website,
					                 B.name,B.street,B.house,B.phone_number,B.website
				FROM inserted A
				INNER JOIN deleted B ON A.library_id = B.library_id

				IF UPDATE(street)
					PRINT N'Добавлено местоположение (улица)'
				IF UPDATE(house)
					PRINT N'Добавлено местоположение (дом)'
				IF UPDATE(name)
					PRINT N'Была переназвана библиотека(и)'
				IF UPDATE(phone_number) AND EXISTS (SELECT TOP 1 delete_phone_number FROM @temp_table WHERE delete_phone_number IS NULL)
					PRINT N'Был добавлен телефон'
				IF UPDATE(phone_number) 
					PRINT N'Был изменен телефон'
				IF UPDATE(website) AND EXISTS (SELECT TOP 1 delete_website FROM @temp_table WHERE delete_website IS NULL)
					PRINT N'Был добавлен сайт'
				IF UPDATE(website)
					PRINT N'Был изменен сайт'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' библиотек'
				ELSE
					PRINT N'у 1 библиотеки'
		END;
	END
go 

/* UPDATE Library SET street = 'Library street', house = 22 WHERE library_id > 1
SELECT * FROM Library
go */

/*UPDATE Library SET phone_number = 88005553535 WHERE library_id=1
SELECT * FROM Library
go */

-- Триггер на вставку --
		
IF OBJECT_ID(N'Add_library',N'TR') IS NOT NULL
	DROP TRIGGER Add_library
go

CREATE TRIGGER Add_library
	ON Library
	AFTER INSERT 
AS
	BEGIN
		IF (SELECT DISTINCT COUNT(*) FROM inserted) > 1
			PRINT 'Добавлены новые библиотеки в таблицу'
		ELSE
			PRINT 'Добавлена новая библиотека в таблицу'
	END
go

/* INSERT INTO Library(library_id,name)
VALUES (5,N'Библиотека имени Эйзентштейна'),
	   (6,N'Библиотека № 122 им. Александра Грина')
SELECT * FROM Library
go */

-- Для представления пункта 2 задания 7 создать триггеры на вставку, удаление и добавление,
-- обеспечивающие возможность выполнения операций с данными непосредственно через представление

-- Представление (View)

SET IDENTITY_INSERT Library OFF;  
go

if OBJECT_ID(N'JoinLibraryView',N'V') is NOT NULL
	DROP VIEW JoinLibraryView;
go

CREATE VIEW JoinLibraryView AS
	SELECT b.name as name,b.author as author, b.genre as genre,b.publish_year as publish_year,
		   l.name as library_name
	FROM Library as l INNER JOIN Book as b ON l.library_id = b.library_id
go

SELECT * FROM JoinLibraryView
go

IF OBJECT_ID(N'Add_View_library',N'TR') IS NOT NULL
	DROP TRIGGER Add_View_library
go

CREATE TRIGGER Add_View_library
	ON JoinLibraryView
	INSTEAD OF INSERT 
AS
	BEGIN

		DECLARE @temp_table TABLE (
					add_name nchar(50), add_author nchar(50),add_genre nchar(20),add_publish_year numeric(4),
					add_library_id int, add_library_name nchar(70)
				);


		INSERT INTO @temp_table(add_name,add_author,add_genre,add_publish_year,add_library_id,add_library_name)
		SELECT A.name,A.author,A.genre,A.publish_year,B.library_id,A.library_name
		FROM inserted A
		LEFT JOIN Library B
		ON A.library_name = B.name


		INSERT INTO Library(name)
		SELECT add_library_name
		FROM @temp_table
		WHERE add_library_id IS NULL

		UPDATE @temp_table SET add_library_id = (SELECT library_id FROM Library WHERE name = add_library_name)

		INSERT INTO Book(author,name,genre,publish_year,library_id)
		SELECT add_author,add_name,add_genre,add_publish_year,add_library_id
		FROM @temp_table

		PRINT 'Добавлены новые книги'
	END
go

/* INSERT INTO JoinLibraryView(name,author,genre,publish_year,library_name)
VALUES (N'451 градус по Фаренгейту',N'Рэй Брэдбери',N'Роман',1953,N'Библиотека иностранной литературы'),
	   (N'Мастер и Маргарита',N'Михаил Булгаков',N'Мистика',1966,N'Библиотека-читальня им. А.С.Пушкина'),
	   (N'Маленький принц',N'Антуан де Сент-Экзюпери',N'Сказка',1943,N'Российская государственная детская библиотека')
SELECT * FROM Library
SELECT * FROM Book
go */


IF OBJECT_ID(N'Delete_View_library',N'TR') IS NOT NULL
	DROP TRIGGER Delete_View_library
go

CREATE TRIGGER Delete_View_library
	ON JoinLibraryView
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE FROM Book WHERE name IN (SELECT name FROM deleted)
		PRINT 'Удалены книги'
	END
go

DELETE FROM JoinLibraryView WHERE (name=N'1408' AND author=N'Стивен Кинг') OR (name=N'Евгений Онегин' AND publish_year=1831)
SELECT * FROM Library
SELECT * FROM Book
SELECT * FROM JoinLibraryView
go

IF OBJECT_ID(N'Update_View_library',N'TR') IS NOT NULL
	DROP TRIGGER Update_View_library
go

CREATE TRIGGER Update_View_library
	ON JoinLibraryView
	INSTEAD OF UPDATE
AS
	BEGIN
		DECLARE @temp_table TABLE (
					add_name nchar(50), add_author nchar(50),add_genre nchar(20),add_publish_year numeric(4),add_library_name nchar(70),
					delete_name nchar(50), delete_author nchar(50),delete_genre nchar(20),delete_publish_year numeric(4),delete_library_name nchar(70),
					add_library_id int
		);

		IF UPDATE(name) OR UPDATE(author) OR UPDATE(publish_year) OR UPDATE(genre)
			BEGIN
				EXEC sp_addmessage 50004, 15,N'Запрещено изменение данных о книге в следствие нарушения целостности! По причине этого, воспользуйтесь созданием новой книги или же удалением существующей',@lang='us_english',@replace='REPLACE';
				RAISERROR(50004,15,-1)
			END

		INSERT INTO @temp_table(add_name,add_author,add_genre,add_publish_year,add_library_name,
								delete_name,delete_author,delete_genre,delete_publish_year,delete_library_name,
								add_library_id)
		SELECT A.name, A.author,A.genre,A.publish_year,A.library_name,
			   B.name,B.author,B.genre,B.publish_year,B.library_name,
			   C.library_id
		FROM inserted A
		INNER JOIN deleted B ON A.name = B.name
		LEFT JOIN  Library C ON A.library_name = C.name

		SELECT * FROM @temp_table


		IF EXISTS (SELECT * FROM @temp_table WHERE add_library_id IS NULL)
			BEGIN
				EXEC sp_addmessage 50003, 15,N'Перемещение книги в несуществующую библиотеку невозможно!',@lang='us_english',@replace='REPLACE';
				RAISERROR(50003,15,-1)
			END

		IF UPDATE(library_name)
			BEGIN
				UPDATE Book SET library_id = (SELECT TOP 1 add_library_id FROM @temp_table) WHERE name IN (SELECT delete_name FROM @temp_table)
				PRINT N'Книга(и) была(и) перемещена(ы) в другую библиотеку'
			END

	END
go

/* UPDATE JoinLibraryView SET library_name=N'Библиотека-читальня им. Тургенева' WHERE (name=N'1408' AND author=N'Стивен Кинг') OR (name=N'Евгений Онегин' AND publish_year=1831)
SELECT * FROM Library
SELECT * FROM Book
go */
