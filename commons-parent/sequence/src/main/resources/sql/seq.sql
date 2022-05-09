create table seq
(
    id        varchar(32) primary key not null,
    value     bigint default 0        not null,
    increment int    default 1        not null,
    version   int    default 1        not null
);

