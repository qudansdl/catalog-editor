ALTER TABLE background ADD COLUMN created TIMESTAMP DEFAULT NOW();
ALTER TABLE background ADD COLUMN updated TIMESTAMP DEFAULT NOW();


ALTER TABLE template_block ADD COLUMN created TIMESTAMP DEFAULT NOW();
ALTER TABLE template_block ADD COLUMN updated TIMESTAMP DEFAULT NOW();

ALTER TABLE template ADD COLUMN created TIMESTAMP DEFAULT NOW();
ALTER TABLE template ADD COLUMN updated TIMESTAMP DEFAULT NOW();