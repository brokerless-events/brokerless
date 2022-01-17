DROP TABLE IF EXISTS BROKERLESS_OUTBOX_EVENTS;
CREATE TABLE BROKERLESS_OUTBOX_EVENTS
(
    event_id             UUID     NOT NULL,
    event_type           UUID     NOT NULL,
    occurred_time        DATETIME NOT NULL,
    published_time       DATETIME NOT NULL,
    producer_instance_id UUID     NOT NULL,
    event_message        CLOB     NOT NULL
);


DROP TABLE IF EXISTS BROKERLESS_INBOX_EVENTS;
CREATE TABLE BROKERLESS_INBOX_EVENTS
(
    event_id       UUID     NOT NULL,
    event_type     UUID     NOT NULL,
    published_time DATETIME NOT NULL,
    event_message  CLOB     NOT NULL
);


DROP TABLE IF EXISTS BROKERLESS_PRODUCER_CURSOR;
CREATE TABLE BROKERLESS_PRODUCER_CURSOR
(
    producer_name VARCHAR(64) NOT NULL,
    cursor        UUID        NOT NULL
);