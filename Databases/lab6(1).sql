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

print ('q2: ' + cast(IDENT_CURRENT('dbo.Author') as nvarchar(3)))
print ('q2 (scope): ' + cast(SCOPE_IDENTITY() as nvarchar(3)))

SELECT * FROM Author
