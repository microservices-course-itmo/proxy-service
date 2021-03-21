DROP TABLE IF EXISTS proxies CASCADE;

CREATE TABLE proxies (
                        id UUID PRIMARY KEY,
                        url TEXT NOT NULL UNIQUE
);