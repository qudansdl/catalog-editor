ALTER TABLE public.template_block DROP CONSTRAINT template_block_template_fk;
DROP TABLE public.template_block;

ALTER TABLE public.catalog_block DROP CONSTRAINT catalog_block_catalog_fk;
DROP TABLE public.catalog_block;

ALTER TABLE public."catalog" DROP COLUMN description;
ALTER TABLE public."template" DROP COLUMN description;

ALTER TABLE public."catalog" ADD content text NULL;
ALTER TABLE public."template" ADD content text NULL;
