ALTER TABLE TEXT ADD COLUMN name varchar(255);


create table background (id uuid DEFAULT uuid_generate_v4 (), name varchar(255), content text, primary key (id));

create table background_categories (background_id uuid not null, category_id uuid not null);
alter table background_categories add constraint background_categories_category_fk foreign key (category_id) references category;
alter table background_categories add constraint background_categories_background_fk foreign key (background_id) references image;

create table template (id uuid DEFAULT uuid_generate_v4 (), description varchar(255), name varchar(255), primary key (id));
create table template_block (id uuid DEFAULT uuid_generate_v4 (), content varchar(255), h int4 not null, type varchar(30), w int4 not null, x int4 not null, y int4 not null, template_id uuid, primary key (id));

alter table template_block add constraint template_block_template_fk foreign key (template_id) references template;

create table template_categories (template_id uuid DEFAULT uuid_generate_v4 (), category_id uuid not null);
alter table template_categories add constraint template_categories_category_fk foreign key (category_id) references category;
alter table template_categories add constraint template_categories_template_fk foreign key (template_id) references template;

