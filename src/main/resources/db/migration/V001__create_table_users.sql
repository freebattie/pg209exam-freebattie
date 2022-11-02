create table users
(
    id_users int identity
        constraint id_users_pk
            primary key,
    username varchar(50)  not null,
    name     varchar(500) not null,
    tlf      varchar(15)  not null
)
go