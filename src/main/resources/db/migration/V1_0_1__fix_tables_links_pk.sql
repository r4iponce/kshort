ALTER TABLE links
DROP
CONSTRAINT links_pkey;

ALTER TABLE links
    ADD CONSTRAINT links_pkey primary key (short);

ALTER TABLE links DROP COLUMN id;