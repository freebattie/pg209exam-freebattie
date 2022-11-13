create table lastRead
(
    id_chat    int    not null
        constraint lastRead_chats_null_fk
            references chats (id_chat),
    id_message int    not null
        constraint lastRead_messages_null_fk
            references messages (id_message),
    id_user    int    not null
        constraint lastRead_users_null_fk
            references users (id_user),
    timestamp  bigint not null,
    constraint lastRead_pk
        primary key (id_chat, id_message, id_user)
);