
    create table games (
       id bigint not null auto_increment,
        board_height integer not null,
        board_width integer not null,
        created_at datetime(6) not null,
        max_moves integer not null,
        number_of_colors integer not null,
        player_name varchar(255) not null,
        secret varchar(255) not null,
        seed bigint not null,
        state varchar(255) not null,
        updated_at datetime(6) not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table moves (
       id bigint not null auto_increment,
        color integer not null,
        created_at datetime(6) not null,
        game_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    alter table moves 
       add constraint game_id_fkey 
       foreign key (game_id) 
       references games (id);
