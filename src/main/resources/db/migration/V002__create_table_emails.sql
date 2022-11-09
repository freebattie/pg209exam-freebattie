create table emails
(
    id_email int identity
        constraint emails_pk
            primary key,
    id_user  int
        constraint emails_users_null_fk
            references users (id_user),
    email    varchar(100)
);