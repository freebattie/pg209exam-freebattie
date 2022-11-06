create table chats
(
    id_chat     int identity
        constraint chats_pk
            primary key,
    title       varchar(250),
    id_message int
);

create table messages
(
    id_chat    int,
    id_message int
        constraint messages_pk
            primary key,
    id_user    int
        constraint messages_users_null_fk
            references users (id_user),
    message    varchar(8000)
);

alter table chats
    add constraint chats_messages_null_fk
        foreign key (id_message) references messages (id_message);

alter table messages
    add constraint messages_chats_null_fk
        foreign key (id_chat) references chats (id_chat);



