ALTER TABLE pattern ADD COLUMN created TIMESTAMP DEFAULT NOW();
ALTER TABLE pattern ADD COLUMN updated TIMESTAMP DEFAULT NOW();
