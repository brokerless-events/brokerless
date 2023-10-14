DROP TABLE IF EXISTS brokerless_outbox_events;
CREATE TABLE brokerless_outbox_events
(
    event_id                 UUID         NOT NULL,
    event_type               VARCHAR(256) NOT NULL,
    occurred_time            TIMESTAMP    NOT NULL,
    published_time           TIMESTAMP    NOT NULL,
    outbox_write_time        TIMESTAMP    NOT NULL,
    producer_name            VARCHAR(128) NOT NULL,
    producer_instance_id     UUID         NOT NULL,
    event_payload_serialised TEXT         NOT NULL
);

DROP TABLE IF EXISTS brokerless_cursors;
CREATE TABLE brokerless_cursors
(
    producer_name VARCHAR(128) NOT NULL,
    cursor        UUID         NOT NULL
);
