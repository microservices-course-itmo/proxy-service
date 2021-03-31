CREATE TABLE proxies
(
    id  bigint PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);

CREATE TABLE parser_proxies
(
    id bigint PRIMARY KEY,
    parser_name varchar(50),
    ping float
)