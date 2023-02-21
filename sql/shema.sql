create table categories
(
    id serial8,
    name varchar not null,
    primary key (id)
);
create table products
(
    id serial8,
    name varchar not null,
    price varchar not null,
    category_id int4,
    primary key (id),
    foreign key (category_id) references  categories(id)
);
create table characteristics
(
    id serial8,
    name varchar not null,
    category_id int4,
    primary key (id),
    foreign key (category_id) references  categories(id)
);
create table characteristics_meaning
(
    id serial8,
    name varchar not null,
    products_id int4,
    characteristics_id int4,
    primary key (id),
    foreign key (products_id) references products(id),
    foreign key (characteristics_id) references characteristics(id)
);
insert into categories (name)
values('Процессоры'),
      ('Мониоторы');
insert into products (name, price, category_id)
values ('Intel Core I9 9900',1000,1),
       ('Samsung SU556270',800,2),
       ('AMD Ryzen R7 7700',900,1),
       ('AOC Z215S659',700,2);
insert into characteristics (name, category_id)
values ('Производитель',1),
       ('Количество ядер',1),
       ('Сокет',1),
       ('Производитель',2),
       ('Диагональ',2),
       ('Матрица',2),
       ('Разрешение',2);
insert into characteristics_meaning (name, products_id, characteristics_id)
values ('Intel',1,1),
       ('Samsung',2,4),
       ('AMD',3,1),
       ('AOC',4,4),
       ('8',1,2),
       ('12',2,2),
       ('1250',1,3),
       ('AM4',2,3),
       ('27',3,5),
       ('21.5',4,5),
       ('TN',3,6),
       ('AH-IPS',4,6),
       ('2560*1440',3,7),
       ('1920*1080',4,7);