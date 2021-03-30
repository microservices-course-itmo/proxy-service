DROP TABLE IF EXISTS proxies CASCADE;

CREATE TABLE proxies
(
    id  bigint PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS parser_proxies CASCADE;

CREATE TABLE parser_proxies
(
    id bigint PRIMARY KEY,
    parser varchar(20),
    ping float
)