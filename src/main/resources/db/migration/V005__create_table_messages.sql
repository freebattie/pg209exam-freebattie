create table messages
(
    id_message int identity
        constraint messages_pk
        primary key,
    message    varchar(8000),
    timestamp  bigint,
    id_chat    int not null
        constraint messages_chats_null_fk
            references chats (id_chat),
    id_user    int not null
        constraint messages_users_null_fk
            references users (id_user)
);