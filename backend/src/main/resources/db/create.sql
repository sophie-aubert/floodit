
    create table games (
       id bigint not null auto_increment,
        board_height integer not null,
        board_width integer not null,
        created_at datetime(6) not null,
        number_of_colors integer not null,
        player_name varchar(255) not null,
        seed bigint not null,
        version bigint not null,
        primary key (id)
    ) engine=InnoDB;
