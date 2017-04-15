use master;
go
if DB_ID (N'lab6') is not null
drop database lab6;
go
create database lab6
on (
NAME = lab6dat,
FILENAME = 'C:\Databases\DB6\lab6dat.mdf',
SIZE = 10,
MAXSIZE = UNLIMITED,
FILEGROWTH = 5
)
log on (
NAME = lab6log,
FILENAME = 'C:\Databases\DB6\lab6log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use lab6;
go 
if OBJECT_ID(N'Author',N'U') is NOT NULL
	DROP TABLE Author;
go


-- Создать таблицу с автоинкрементным первичным ключом.
-- Добавить поля, для которых используются ограничения (CHECK), значения по умолчанию (DEFAULT).

CREATE TABLE Author (
	author_id int IDENTITY(1,1) PRIMARY KEY,
	surname nchar(30) NOT NULL,
	name nchar(30) NOT NULL,
	date_of_birth numeric(4) NULL CHECK (date_of_birth>1500 AND date_of_birth<2000),
	date_of_death numeric(4) NULL CHECK (date_of_death>1500 AND date_of_death<2000), 
	biography nvarchar(1000) DEFAULT ('Unknown'),
	CONSTRAINT checkAuthor CHECK (date_of_birth<date_of_death)
	);
go

INSERT INTO Author(name,surname,date_of_birth,date_of_death)
VALUES (N'Стивен',N'Кинг', 1947, NULL),
	   (N'Джордан',N'Белфорт',1962, NULL),
	   (N'Джоджо',N'Мойес',1969,NULL),
	   (N'Артур',N'Конан Дойл', 1859, 1930),
	   (N'Чарльз',N'Диккенс', 1812, 1870),
	   (N'Оскар',N'Уайльд', 1854, 1900),
	   (N'Антуан',N'де Сент-Экзюпери', 1900, 1944)
	   --  Возникнет ошибка --
	   -- ,(N'Рэй',N'Брэдбери', 1920, 2012)--
	   -- ,(N'Неизвестный',N'автор', 1989, 1986) --
go

SELECT * FROM Author
go
print 'go-go-go'
go
waitfor delay '00:00:15'
print ('q1: ' + cast(IDENT_CURRENT('dbo.Author') as nvarchar(3)))
print ('q1(scope): ' + cast(SCOPE_IDENTITY() as nvarchar(3)))

-- Возможно использование SCOPE_IDENTITY() --
SELECT IDENT_CURRENT('dbo.Author') as last_id
go 

/* DELETE FROM Author
WHERE date_of_birth < 1900
go

INSERT INTO Author(name,surname,date_of_birth,date_of_death)
VALUES (N'Фрэнсис',N'Скотт', 1896, 1940)

SELECT * FROM Author
go */


-- Создать таблицу с первичным ключом на основе глобального уникального идентификатора

if OBJECT_ID(N'Book',N'U') is NOT NULL
	DROP TABLE Book;
go

CREATE TABLE Book (
	book_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT (NEWID()),
	author nchar(50) NOT NULL,
	name nchar(50) NOT NULL,
	genre nchar(20) NOT NULL CHECK (genre IN (N'Роман',N'Научная фантастика',N'Драма',N'Детектив',
											N'Мистика', N'Поэзия',N'Сказка', N'Фантастика', N'Пьеса')),
	publish_year numeric(4) NOT NULL CHECK (publish_year>1500 AND publish_year<2000),
	publish_house varchar(100) NULL DEFAULT ('Unknown'),
	cost_of smallmoney NULL CHECK (cost_of > 0),
	);
go

INSERT INTO Book(author,name,genre,publish_year)
-- Для получения вставляемого уникального идентификатора -- 
-- OUTPUT inserted.book_id -- 
VALUES (N'Александр Пушкин',N'Евгений Онегин', N'Роман', 1831),
	   (N'Жюль Верн',N'20 000 льё под водой',N'Научная фантастика', 1916),
	   (N'Агата Кристи',N'Убийство Роджера Экройда',N'Детектив',1926),
	   (N'Стивен Кинг',N'1408', N'Мистика', 1926),
	   (N'Корней Чуковской',N'1408', N'Сказка', 1936),
	   (N'Гастон Леру',N'Призрак оперы', N'Роман', 1910)
go
INSERT INTO Book(book_id,author,name,genre,publish_year)
-- Для получения вставляемого уникального идентификатора -- 
-- OUTPUT inserted.book_id -- 
VALUES ('294B4012-1617-482A-8058-F73039852768',N'Александр Пушкин',N'Евгений Онегин', N'Роман', 1831)

SELECT * FROM Book
go


-- Создать таблицу с первичным ключом на основе последовательности
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'TestSequence' AND TYPE='SO')
DROP SEQUENCE TestSequence
go

CREATE SEQUENCE TestSequence
	START WITH 0
	INCREMENT BY 1
	MAXVALUE 10;
go

if OBJECT_ID(N'ArrayList',N'U') is NOT NULL
	DROP TABLE List;
go

CREATE TABLE ArrayList (
	element_id int PRIMARY KEY NOT NULL,
	element nchar(50) DEFAULT (N'Элемент'),
	);
go

INSERT INTO ArrayList(element_id,element)
VALUES (NEXT VALUE FOR DBO.TestSequence,N'Россия'),
	   (NEXT VALUE FOR DBO.TestSequence,N'Греция'),
	   (NEXT VALUE FOR DBO.TestSequence,N'Испания'),
	   (NEXT VALUE FOR DBO.TestSequence,N'Италия'),
	   (NEXT VALUE FOR DBO.TestSequence,N'Канада')
go

SELECT * From ArrayList
go


-- Создать две связанные таблицы, и протестировать на них различные варианты действий --
-- для ограничений ссылочной целостности (NO ACTION| CASCADE | SET NULL | SET DEFAULT). --
if OBJECT_ID(N'FK_Patient',N'F') IS NOT NULL
	ALTER TABLE Visit DROP CONSTRAINT FK_Patient
go

if OBJECT_ID(N'Patient',N'U') is NOT NULL
	DROP TABLE Patient;
go
	
CREATE TABLE Patient (
	patient_id int PRIMARY KEY NOT NULL,
	number_patient_card int NULL,
	name nchar(50) NOT NULL,
	gender nchar(1) CHECK (gender IN (N'М',N'Ж')),
	date_of_birth datetime NULL
	);
go

INSERT INTO Patient(patient_id,name,gender)
VALUES (1,N'Полина Буриличева',N'Ж'),
	   (2,N'Анна Дубина',N'Ж'),
	   (3,N'Станислав Каширин',N'М'),
	   (4,N'Виктория Киприянова',N'Ж'),
	   (5,N'Вероника Конака',N'Ж'),
	   (6,N'Кирилл Машуков',N'М'),
	   (7,N'Вячеслав Перешивкин',N'М')
go

SELECT * FROM Patient
go

if OBJECT_ID(N'Visit',N'U') is NOT NULL
	DROP TABLE Visit;
go

CREATE TABLE Visit (
	visit_id int IDENTITY(1,1) PRIMARY KEY,
	visit_date date DEFAULT (CONVERT(date,GETDATE())),
	visit_time time(0) DEFAULT (CONVERT(time,GETDATE())),
	visit_patient int NULL,
	visit_reason nchar(100) DEFAULT (N'Неизвестна причина болезни'),
	CONSTRAINT FK_Patient FOREIGN KEY (visit_patient) REFERENCES Patient (patient_id)
	-- ON UPDATE CASCADE --
	-- ON UPDATE SET NULL --
	-- ON UPDATE SET DEFAULT --
	-- ON DELETE CASCADE --
	-- ON DELETE SET NULL --
	ON DELETE SET DEFAULT
	);
go

INSERT INTO Visit(visit_date,visit_time,visit_patient)
VALUES (CONVERT(date,N'11-01-2014'),CONVERT(time,N'12:20:00'),3),
	   (CONVERT(date,N'19-03-2014'),CONVERT(time,N'13:30:00'),6),
	   (CONVERT(date,N'21-06-2014'),CONVERT(time,N'15:00:00'),2),
	   (CONVERT(date,N'26-08-2014'),CONVERT(time,N'16:10:00'),7),
	   (CONVERT(date,N'06-10-2014'),CONVERT(time,N'17:20:00'),3)  
go

SELECT * FROM Visit
go


/* DELETE FROM Patient
WHERE gender = N'М'
go 

SELECT * FROM Patient
go
SELECT * FROM Visit
go */
