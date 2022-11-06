create table lastRead
(
    id_chat    int not null
        constraint lastRead_chats_null_fk
            references chats (id_chat),
    id_message int
        constraint lastRead_messages_null_fk
            references messages (id_message),
    id_user    int
        constraint lastRead_users_null_fk
            references users (id_user),
    constraint lastRead_pk
        primary key (id_chat, id_message, id_user)
);