ALTER TABLE public.background_categories DROP CONSTRAINT background_categories_background_fk;
alter table background_categories add constraint background_categories_background_fk foreign key (background_id) references background;
