create table messages
(
    id_message int identity
        constraint messages_pk
            primary key,
    message    varchar(8000),
    id_chat    int
        constraint messages_chats_null_fk
            references chats (id_chat),
    id_user    int
        constraint messages_users_null_fk
            references users (id_user)
);