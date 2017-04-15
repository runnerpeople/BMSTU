use master;
go
if DB_ID (N'lab12') is not null
drop database lab12;
go
create database lab12
on (
NAME = lab12dat,
FILENAME = 'C:\Databases\DB12\lab12dat.mdf',
SIZE = 10,
MAXSIZE = 25,
FILEGROWTH = 5
)
log on (
NAME = lab12log,
FILENAME = 'C:\Databases\DB12\lab12log.ldf',
SIZE = 5,
MAXSIZE = 20,
FILEGROWTH = 5
);
go 

use lab12;
go

if OBJECT_ID(N'Uniq_Patient',N'UQ') IS NOT NULL
	ALTER TABLE Visit DROP CONSTRAINT Uniq_patient
go

if OBJECT_ID(N'Patient',N'U') is NOT NULL
	DROP TABLE Patient;
go
	
CREATE TABLE Patient (
	patient_id int PRIMARY KEY NOT NULL,
	number_patient_card int NULL,
	name nchar(50) NOT NULL,
	gender nchar(1) CHECK (gender IN (N'М',N'Ж')),
	date_of_birth date NULL
	CONSTRAINT Uniq_patient UNIQUE (number_patient_card)
	);
go

INSERT INTO Patient(patient_id,number_patient_card,name,gender,date_of_birth)
VALUES (1,747708263,N'Полина Буриличева',N'Ж',CONVERT(date,N'03-11-1961')),
	   (2,435768605,N'Анна Дубина',N'Ж',CONVERT(date,N'26-03-1965')),
	   (3,116016179,N'Станислав Каширин',N'М',CONVERT(date,N'02-08-1967')),
	   (4,364875697,N'Виктория Киприянова',N'Ж',CONVERT(date,N'03-09-1967')),
	   (5,900748436,N'Вероника Конака',N'Ж',CONVERT(date,N'08-07-1984')),
	   (6,432040156,N'Кирилл Машуков',N'М',CONVERT(date,N'19-02-1988')),
	   (7,363922627,N'Вячеслав Перешивкин',N'М',CONVERT(date,N'12-12-1995'))
go

if OBJECT_ID(N'FK_Patient',N'F') IS NOT NULL
	ALTER TABLE Visit DROP CONSTRAINT FK_Patient
go

if OBJECT_ID(N'Visit',N'U') is NOT NULL
	DROP TABLE Visit;
go

CREATE TABLE Visit (
	visit_id int IDENTITY(1,1) PRIMARY KEY,
	visit_date date NOT NULL,
	visit_time time(0) NULL,
	patient_id int NOT NULL,
	visit_reason nchar(100) DEFAULT (N'Неизвестна причина болезни'),
	CONSTRAINT FK_Patient FOREIGN KEY (patient_id) REFERENCES Patient (patient_id) ON UPDATE CASCADE ON DELETE CASCADE
	);
go

INSERT INTO Visit(visit_date,visit_time,patient_id)
VALUES (CONVERT(date,N'11-01-2016'),CONVERT(time,N'12:20:00'),3),
	   (CONVERT(date,N'19-03-2016'),CONVERT(time,N'13:30:00'),6),
	   (CONVERT(date,N'21-06-2016'),CONVERT(time,N'15:00:00'),2),
	   (CONVERT(date,N'26-08-2016'),CONVERT(time,N'16:10:00'),7),
	   (CONVERT(date,N'06-10-2016'),CONVERT(time,N'17:20:00'),3)  
go

SELECT * FROM Visit
go

SELECT * FROM Patient
go
