use master;
go
if DB_ID (N'lab8') is not null
drop database lab8;
go
create database lab8
on (
NAME = lab8dat,
FILENAME = 'C:\Databases\DB8\lab8dat.mdf',
SIZE = 10,
MAXSIZE = UNLIMITED,
FILEGROWTH = 5
)
log on (
NAME = lab8log,
FILENAME = 'C:\Databases\DB8\lab8log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use lab8;
go 
if OBJECT_ID(N'Cinema',N'U') is NOT NULL
	DROP TABLE Cinema;
go

CREATE TABLE Cinema (
	cinema_id int IDENTITY(1,1) PRIMARY KEY,
	producer nchar(50) NOT NULL,
	name nchar(50) NOT NULL,
	genre nchar(20) NOT NULL CHECK (genre IN (N'Комедия',N'Боевик',N'Ужасы',N'Фантастика',
											N'Драма', N'Семейный',N'Мультфильм')),
	year_issue numeric(4) NOT NULL CHECK (year_issue>1900 AND year_issue<2017),
	starring nvarchar(100) NULL DEFAULT ('Неизвестно'),
	country nchar(50) NULL
	);
go

INSERT INTO Cinema(producer,name,genre,year_issue)
VALUES (N'Джон Лукас',N'21 и больше', N'Комедия', 2013),
	   (N'Эрик Дарнелл',N'Мадагаскар', N'Мультфильм', 2005),
	   (N'Джон Мактирнан',N'Крепкий Орешек', N'Боевик', 1988),
	   (N'Уэс Крэйвен',N'Крик', N'Ужасы', 1996),
	   (N'Джеймс Кэмерон',N'Аватар', N'Фантастика', 2009),
	   (N'Джерри Цукер',N'Привидение', N'Драма', 1990),
	   (N'Крис Коламбус',N'Один дома', N'Семейный', 1990)
go

--SELECT * FROM Cinema
--go

--Создать хранимую процедуру, производящую выборку из некоторой таблицы
-- и возвращающую результат выборки в виде курсора.

IF OBJECT_ID(N'dbo.select_proc', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_proc
GO

CREATE PROCEDURE dbo.select_proc
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR 
	FORWARD_ONLY STATIC FOR
	SELECT producer,name,genre,year_issue
	FROM Cinema

	OPEN @cursor;
GO

DECLARE @cinema_cursor CURSOR;
EXECUTE dbo.select_proc @cursor = @cinema_cursor OUTPUT;
-- Возникает ошибка --
-- OPEN @cinema_cursor;

FETCH NEXT FROM @cinema_cursor;
WHILE (@@FETCH_STATUS = 0)
BEGIN
	FETCH NEXT FROM @cinema_cursor;
END

CLOSE @cinema_cursor;
DEALLOCATE @cinema_cursor;
GO

-- Модифицировать хранимую процедуру п.1. таким образом, чтобы выборка   --
-- осуществлялась с формированием столбца, значение которого формируется --
-- пользовательской функцией. --

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
	SELECT producer,name,genre,dbo.random_number(1,100) as rating
	FROM Cinema

	OPEN @cursor;

GO

DECLARE @cinema_rating_cursor CURSOR;
EXECUTE dbo.select_proc_with_add @cursor = @cinema_rating_cursor OUTPUT;

FETCH NEXT FROM @cinema_rating_cursor;
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @cinema_rating_cursor;
	END

CLOSE @cinema_rating_cursor;
DEALLOCATE @cinema_rating_cursor;
GO

-- Создать хранимую процедуру, вызывающую процедуру п.1., осуществляющую прокрутку возвращаемого  --
-- курсора и выводящую сообщения, сформированные из записей при выполнении условия, заданного     --
-- еще одной пользовательской функцией.													          --

IF OBJECT_ID(N'century_film',N'FN') IS NOT NULL
	DROP FUNCTION century_film
go

CREATE FUNCTION century_film(@a numeric(4))
	RETURNS int
	AS
		BEGIN
			DECLARE @result int
			IF (@a>2000)
				SET @result = 21
			ELSE
				SET @result = 20
			RETURN (@result)
		END;
go

IF OBJECT_ID(N'dbo.external_proc',N'P') IS NOT NULL
	DROP PROCEDURE dbo.external_proc
GO

CREATE PROCEDURE dbo.external_proc 
AS
	DECLARE @external_cursor CURSOR;
	DECLARE	@table_producer nchar(50);
	DECLARE @table_name nchar(50);
	DECLARE @table_genre nchar(50);
	DECLARE @table_year_issue numeric(4);
	
	EXECUTE dbo.select_proc @cursor = @external_cursor OUTPUT;

	FETCH NEXT FROM @external_cursor INTO @table_producer,@table_name,@table_genre,@table_year_issue
	
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		IF (dbo.century_film(@table_year_issue)=21)
			PRINT @table_producer + ' ' + @table_name + ' ' + @table_genre + N' (был создан в 21 веке)'
		ELSE
			PRINT @table_producer + ' ' + @table_name + ' ' + @table_genre + N' (был создан в 20 веке)'
		FETCH NEXT FROM @external_cursor INTO @table_producer,@table_name,@table_genre,@table_year_issue;
	END

	CLOSE @external_cursor;
	DEALLOCATE @external_cursor;

GO

EXECUTE dbo.external_proc
GO
		
--- Модифицировать хранимую процедуру п.2. таким образом, чтобы выборка
--- формировалась с помощью табличной функции.
IF OBJECT_ID(N'table_function',N'TF') IS NOT NULL
	DROP FUNCTION table_function
go	

CREATE FUNCTION table_function()
	RETURNS TABLE
AS
	RETURN (
		SELECT producer,name,genre,year_issue,dbo.random_number(1,100) as rating
		FROM Cinema
		WHERE (dbo.century_film(year_issue) = 21)
	)
GO

ALTER PROCEDURE dbo.select_proc_with_add
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR 
	FORWARD_ONLY STATIC FOR 
	SELECT * FROM dbo.table_function()
	OPEN @cursor;
GO	

DECLARE @cinema_table_cursor CURSOR;
EXECUTE dbo.select_proc_with_add @cursor = @cinema_table_cursor OUTPUT;

FETCH NEXT FROM @cinema_table_cursor;
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @cinema_table_cursor;
	END

CLOSE @cinema_table_cursor;
DEALLOCATE @cinema_table_cursor;
GO
