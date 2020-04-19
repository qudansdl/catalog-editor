create table catalog (id uuid DEFAULT uuid_generate_v4 (), description varchar(255), name varchar(255), primary key (id));
create table catalog_block (id uuid DEFAULT uuid_generate_v4 (), content varchar(255), h int4 not null, type varchar(30), w int4 not null, x int4 not null, y int4 not null, catalog_id uuid, primary key (id));

create table category (id uuid DEFAULT uuid_generate_v4 (), name varchar(255), primary key (id));
alter table catalog_block add constraint catalog_block_catalog_fk foreign key (catalog_id) references catalog;

create table catalog_categories (catalog_id uuid DEFAULT uuid_generate_v4 (), category_id uuid not null);
alter table catalog_categories add constraint catalog_categories_category_fk foreign key (category_id) references category;
alter table catalog_categories add constraint catalog_categories_catalog_fk foreign key (catalog_id) references catalog;


create table text (id uuid DEFAULT uuid_generate_v4 (), content text, primary key (id));
create table image (id uuid DEFAULT uuid_generate_v4 (), content text, primary key (id));

create table image_categories (image_id uuid not null, category_id uuid not null);
alter table image_categories add constraint image_categories_category_fk foreign key (category_id) references category;
alter table image_categories add constraint image_categories_image_fk foreign key (image_id) references image;


create table text_categories (text_id uuid DEFAULT uuid_generate_v4 (), category_id uuid not null);
alter table text_categories add constraint text_categories_category_fk foreign key (category_id) references category;
alter table text_categories add constraint text_categories_text_fk foreign key (text_id) references text;


insert into  category  (name) values ('테스트1');
insert into  category  (name) values ('테스트2');
insert into  category  (name) values ( '테스트3');
