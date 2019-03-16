# Домашнее задание по лекции "База данных"
В приложении из прошлого домашнего задания добавить функционал.

1) Необходимо создать базу данных. Используйте MySql
2) Структура базы данных
`address ( id int auto_increment not null, postcode varchar(256), country varchar(256), region varchar(256), city varchar(256), street varchar(256), house int, flat int, primary key (id) )`
`persons ( id int auto_increment not null, surname varchar(256), name varchar(256), middlename varchar(256), birthday date, gender varchar(1), inn varchar(12), address_id int not null, foreign key (address_id) references address(id), primary key (id) )`
3) Вынести в отдельный файл данные для подключения к базе
4) Если во время работы приложения есть доступ к сети, то нужно скачивать данные из API, на их основе генерить пользователей, но теперь также надо записывать данные в базу.
5) Если человек с таким ФИО уже существует, то делать UPDATE его данных
6) Если во время работы нет доступа в Интернет - генерация пользователей осуществляется из базы данных
7) Если в базе нет данных, то генерирует пользователя по процессу из ДЗ №3

В readme также укажите, как изменить данные для подключения к базе.

Прислать ссылку на последний коммит.