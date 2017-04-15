-- Создание распределенных баз данных со связанными таблицами средствами СУБД SQL Server 2012

use master;
go
if DB_ID (N'lab15_1') is not null
drop database lab15_1;
go
create database lab15_1
on (
NAME = lab151dat,
FILENAME = 'C:\Databases\DB15\lab151dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab151log,
FILENAME = 'C:\Databases\DB15\lab151log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use master;
go
if DB_ID (N'lab15_2') is not null
drop database lab15_2;
go
create database lab15_2
on (
NAME = lab152dat,
FILENAME = 'C:\Databases\DB15\lab152dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab152log,
FILENAME = 'C:\Databases\DB15\lab152log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

-- 1.Создать в базах данных пункта 1 задания 13 связанные таблицы.

use lab15_1;
go

if OBJECT_ID(N'Book',N'U') is NOT NULL
	DROP TABLE Book;
go

CREATE TABLE Book (
	book_id int NOT NULL PRIMARY KEY,
	name nchar(50) NOT NULL,
	genre nchar(20) NOT NULL CHECK (genre IN (N'Роман',N'Научная фантастика',N'Драма',N'Детектив',
											N'Мистика', N'Поэзия',N'Сказка', N'Фантастика', N'Пьеса')),
	publish_year numeric(4) NOT NULL,
	cost_of smallmoney NULL CHECK (cost_of > 0),
	author_id int NOT NULL,

	-- Реализовать вручную! Таблицы "связаны" только условно, через триггеры -- 
	-- CONSTRAINT FK_library FOREIGN KEY (author_id) REFERENCES Library (author_id),

	-- Реализовать вручную! Также через INSERT-триггер --
	-- CONSTRAINT Uniq_book UNIQUE (name,genre,publish_year)
	);
go

use lab15_2;
go


if OBJECT_ID(N'Check_author',N'C') IS NOT NULL
	ALTER TABLE Author DROP CONSTRAINT Check_author
go

if OBJECT_ID(N'Author',N'U') is NOT NULL
	DROP TABLE Author;
go

CREATE TABLE Author (
	author_id int NOT NULL PRIMARY KEY,
	surname nchar(30) NOT NULL,
	name nchar(30) NOT NULL,
	date_of_birth numeric(4) NULL CHECK (date_of_birth<2015),
	date_of_death numeric(4) NULL, 
	biography nvarchar(1000) DEFAULT ('Unknown'),
	-- Реализовать вручную! Также через INSERT-триггер --
	-- CONSTRAINT Uniq_author  UNIQUE (surname,name),
	CONSTRAINT Check_author CHECK (date_of_birth<date_of_death)
	);
go


-- Параметр сервера "lazy schema validation" недоступен в данном выпуске SQL Server.
-- EXEC sp_serveroption 'OtherServer','lazy schema validation', 'true'
-- go

-- 2.Создать необходимые элементы базы данных (представления, триггеры), 
-- обеспечивающие работу с данными связанных таблиц 
-- (выборку, вставку, изменение, удаление). 

if OBJECT_ID(N'AuthorBookView',N'V') is NOT NULL
	DROP VIEW AuthorBookView;
go

CREATE VIEW AuthorBookView AS
	SELECT A.book_id as book_id, A.genre as genre, A.publish_year as publish_year, A.cost_of as cost_of, B.*
	FROM lab15_1.dbo.Book A, lab15_2.dbo.Author B
	WHERE A.author_id = B.author_id
go


IF OBJECT_ID(N'InsertAuthor',N'TR') IS NOT NULL
	DROP TRIGGER InsertAuthor
go

CREATE TRIGGER InsertAuthor
	ON Author
	INSTEAD OF INSERT 
AS
	BEGIN
		IF EXISTS (SELECT A.author_id
					   FROM lab15_2.dbo.Author AS A,
							inserted AS I
					   WHERE A.name = I.name AND A.surname = I.surname)
			BEGIN
				EXEC sp_addmessage 50001, 15,N'Добавление существующего автора!',@lang='us_english',@replace='REPLACE';
				RAISERROR(50001,15,-1)
			END
		ELSE
			IF EXISTS (SELECT A.author_id
						FROM lab15_2.dbo.Author AS A, 
							  inserted AS I
					   WHERE A.author_id = I.author_id)
				BEGIN
					EXEC sp_addmessage 50002, 15,N'ID занят! Попробуйте другой',@lang='us_english',@replace='REPLACE';
					RAISERROR(50002,15,-1)
				END
			ELSE
				INSERT INTO lab15_2.dbo.Author(author_id,surname,name,date_of_birth,date_of_death,biography)
				SELECT author_id,surname,name,date_of_birth,date_of_death,biography FROM inserted
		IF (SELECT DISTINCT COUNT(*) FROM inserted) > 1
			PRINT 'Добавлены новые авторы в таблицу'
		ELSE
			PRINT 'Добавлен новый автор в таблицу'
	END
go

IF OBJECT_ID(N'DeleteAuthor',N'TR') IS NOT NULL
	DROP TRIGGER DeleteAuthor
go

CREATE TRIGGER DeleteAuthor
	ON Author
	INSTEAD OF DELETE
AS
	BEGIN
		-- Реализация каскадного удаления
		DELETE B FROM lab15_1.dbo.Book AS B INNER JOIN deleted AS d ON B.author_id = d.author_id
		DELETE A FROM lab15_2.dbo.Author AS A INNER JOIN deleted AS d ON A.author_id = d.author_id
	END
go

IF OBJECT_ID(N'UpdateAuthor',N'TR') IS NOT NULL
	DROP TRIGGER UpdateAuthor
go

CREATE TRIGGER UpdateAuthor
	ON Author
	AFTER UPDATE
AS
	BEGIN
		IF (UPDATE(surname) OR UPDATE(name)
			OR (UPDATE(date_of_birth) AND EXISTS (SELECT TOP 1 date_of_birth FROM deleted WHERE date_of_birth is not NULL))
			OR (UPDATE(date_of_death) AND EXISTS (SELECT TOP 1 date_of_death FROM deleted WHERE date_of_death is not NULL)))
			BEGIN;
				EXEC sp_addmessage 50003, 15,N'Запрещено изменение данных об авторе экспоната в следствие нарушения целостности! По причине этого, воспользуйтесь созданием нового автора или же удалением существующего',@lang='us_english',@replace='REPLACE';
				RAISERROR(50003,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					author_id int PRIMARY KEY,
					add_date_of_birth numeric(4), add_date_of_death numeric(4),add_biography nvarchar(1000),
					delete_date_of_birth numeric(4), delete_date_of_death numeric(4),delete_biography nvarchar(1000)
				);

				INSERT INTO @temp_table(author_id,add_date_of_birth,add_date_of_death,add_biography,
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


use lab15_1;
go

IF OBJECT_ID(N'InsertBook',N'TR') IS NOT NULL
	DROP TRIGGER InsertBook
go

CREATE TRIGGER InsertBook
	ON Book
	INSTEAD OF INSERT 
AS
	BEGIN
		IF EXISTS (SELECT B.book_id
					   FROM	Book AS B,
							inserted AS I
					   WHERE B.name = I.name AND B.genre = I.genre AND B.publish_year = I.publish_year)
			BEGIN
				EXEC sp_addmessage 50004, 15,N'Добавление существующей книги!',@lang='us_english',@replace='REPLACE';
				RAISERROR(50004,15,-1)
			END
		ELSE
			IF EXISTS (SELECT B.book_id
						FROM lab15_1.dbo.Book AS B, 
							  inserted AS I
					   WHERE B.book_id = I.book_id)
				BEGIN
					EXEC sp_addmessage 50002, 15,N'ID занят! Попробуйте другой',@lang='us_english',@replace='REPLACE';
					RAISERROR(50002,15,-1)
				END
			ELSE
				IF EXISTS (SELECT author_id 
						   FROM inserted 
						   WHERE author_id NOT IN 
						   (SELECT author_id FROM lab15_2.dbo.Author))
				BEGIN
					EXEC sp_addmessage 50005, 15,N'Добавление книги с несуществующим автором! Укажите верный author_id',@lang='us_english',@replace='REPLACE';
					RAISERROR(50005,15,-1)
				END
					
			ELSE
				INSERT INTO Book(book_id,name,genre,publish_year,cost_of,author_id)
				SELECT book_id,name,genre,publish_year,cost_of,author_id FROM inserted
		IF (SELECT DISTINCT COUNT(*) FROM inserted) > 1
			PRINT 'Добавлены новые книги в таблицу'
		ELSE
			PRINT 'Добавлена новая книга в таблицу'
	END
go

IF OBJECT_ID(N'DeleteBook',N'TR') IS NOT NULL
	DROP TRIGGER DeleteBook
go

CREATE TRIGGER DeleteBook
	ON Book
	INSTEAD OF DELETE
AS
	BEGIN
		DELETE B FROM lab15_1.dbo.Book AS B INNER JOIN deleted AS d ON B.book_id = d.book_id
	END
go

IF OBJECT_ID(N'UpdateBook',N'TR') IS NOT NULL
	DROP TRIGGER UpdateBook
go

CREATE TRIGGER UpdateBook
	ON Book
	AFTER UPDATE
AS
	BEGIN

		IF (UPDATE(book_id) OR UPDATE(name) OR UPDATE(genre) OR UPDATE(publish_year) OR UPDATE(author_id))
			BEGIN;
				EXEC sp_addmessage 50003, 15,N'Запрещено изменение данных об книге в следствие нарушения целостности! По причине этого, воспользуйтесь созданием новой книги или же удалением существующего',@lang='us_english',@replace='REPLACE';
				RAISERROR(50003,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					author_id int PRIMARY KEY,
					add_cost_of smallmoney,
					delete_cost_of smallmoney
				);

				INSERT INTO @temp_table(author_id,add_cost_of,
										          delete_cost_of)
				SELECT A.author_id, A.cost_of,
					                B.cost_of
				FROM inserted A
				INNER JOIN deleted B ON A.author_id = B.author_id

				IF UPDATE(cost_of)
					PRINT N'Была изменена стоимость книги'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + ' книги'
				ELSE
					PRINT N'у 1 книги'
		END;
	END
go 

INSERT INTO lab15_2.dbo.Author(author_id,surname,name,date_of_birth,date_of_death)
VALUES (1,N'Пушкин',N'Александр', 1799, 1837),
	   (2,N'Верн',N'Жюль',1828, 1905),
	   (3,N'Кристи',N'Агата',1890,1976),
	   (4,N'Кинг',N'Стивен', NULL, NULL),
	   (5,N'Чуковской',N'Корней', 1882, 1969),
	   (6,N'Леру',N'Гастон', 1868, 1927)
go

UPDATE lab15_2.dbo.Author SET date_of_birth = 1947 WHERE name = N'Стивен'
go


INSERT INTO lab15_1.dbo.Book(book_id,name,genre,publish_year,author_id)
VALUES (1,N'Евгений Онегин', N'Роман', 1831,1),
	   (2,N'20 000 льё под водой',N'Научная фантастика', 1916,2),
	   (3,N'Убийство Роджера Экройда',N'Детектив',1926,3),
	   (4,N'1408', N'Мистика', 1926,4),
	   (5,N'Добрый доктор', N'Сказка', 1936,5),
	   (6,N'Призрак оперы', N'Роман', 1910,6)
go

UPDATE lab15_1.dbo.Book SET cost_of = $7.75 WHERE name = N'Призрак оперы'
go

SELECT * FROM lab15_2.dbo.Author;
SELECT * FROM lab15_1.dbo.Book;
go
 
