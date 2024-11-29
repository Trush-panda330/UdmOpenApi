create table samples(
    id bigint not null primary key auto_increment,
    content varchar(256) not null
);

create table tasks(
    id bigint not null primary key auto_increment,
    title varchar(256) not null
);