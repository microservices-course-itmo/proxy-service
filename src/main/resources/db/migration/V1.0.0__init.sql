
CREATE TABLE proxies
(
    id  bigint PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);
CREATE TABLE parser_proxies
(
    id bigint PRIMARY KEY,
    parser varchar(20),
    ping float
)