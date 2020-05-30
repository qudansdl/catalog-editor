create table pattern (id uuid DEFAULT uuid_generate_v4 (), content text, primary key (id));

create table pattern_categories (pattern_id uuid not null, category_id uuid not null);
alter table pattern_categories add constraint pattern_categories_category_fk foreign key (category_id) references category;
alter table pattern_categories add constraint pattern_categories_pattern_fk foreign key (pattern_id) references pattern;
