create table [users-chats]
(
    id_user int not null
        constraint [users-chats_users_null_fk]
            references users (id_user),
    id_chat int not null
        constraint [users-chats_chats_null_fk]
            references chats (id_chat),
    constraint [users-chats_pk]
        primary key (id_user, id_chat)
);