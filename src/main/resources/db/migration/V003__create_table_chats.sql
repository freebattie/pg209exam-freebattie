create table chats
(
    id_chat int identity
        constraint chats_pk
            primary key,
    title   varchar(50)
);