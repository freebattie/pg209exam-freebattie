create table userschats
(
    id_user int
        constraint userschats_users_null_fk
            references users (id_user),
    id_chat int
        constraint userschats_chats_null_fk
            references chats (id_chat),
    constraint userschats_pk
        primary key (id_user, id_chat)
);