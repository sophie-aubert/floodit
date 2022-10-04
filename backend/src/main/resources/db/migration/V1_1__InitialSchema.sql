create table games (
  id bigserial not null,
  board_height int4 not null,
  board_width int4 not null,
  created_at timestamp not null,
  max_moves int4 not null,
  number_of_colors int4 not null,
  player_name varchar(255) not null,
  secret varchar(255) not null,
  seed int8 not null,
  state varchar(255) not null,
  updated_at timestamp not null,
  version int8 not null,
  primary key (id)
);

create table moves (
  id bigserial not null,
  color int4 not null,
  created_at timestamp not null,
  game_id int8 not null,
  primary key (id)
);

alter table moves
  add constraint game_id_fkey
  foreign key (game_id)
  references games;
