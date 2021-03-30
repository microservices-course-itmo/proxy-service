DROP TABLE IF EXISTS proxies CASCADE;

CREATE TABLE proxies
(
    id  UUID PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS parser_proxies CASCADE;

CREATE TABLE parser_proxies
(
    id UUID PRIMARY KEY,
    parser varchar(20),
    ping float
)