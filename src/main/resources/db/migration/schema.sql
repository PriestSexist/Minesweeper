create table if not exists game_info
(
    game_id       uuid    default gen_random_uuid() not null
        constraint game_info_pk
            primary key,
    width         integer                           not null,
    height        integer                           not null,
    mines_count   integer                           not null,
    completed     boolean default false             not null,
    initial_field varchar                           not null,
    open_field    varchar                           not null
);

alter table game_info
    owner to postgres;