create table emails
(
    id_user int          not null
        constraint emails_users_null_fk
            references users (id_user),
    email   varchar(100) not null,
    constraint emails_pk
        primary key (id_user, email)
);