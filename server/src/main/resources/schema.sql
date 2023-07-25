DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
),
    email VARCHAR
(
    512
),
    CONSTRAINT pk_user PRIMARY KEY
(
    id
),
    CONSTRAINT uq_user_email UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
(
    1000
) NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY
(
    id
),
    CONSTRAINT uq_requests_users FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    1000
) NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT pk_item PRIMARY KEY
(
    id
),
    CONSTRAINT uq_item_owner FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    id
) ON DELETE CASCADE,
    CONSTRAINT uq_items_requests FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    id
)
  ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    item_id
    BIGINT
    NOT
    NULL,
    booker_id
    BIGINT
    NOT
    NULL,
    status
    VARCHAR
(
    16
) NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY
(
    id
),
    CONSTRAINT uq_booking_item FOREIGN KEY
(
    item_id
) REFERENCES items ON DELETE CASCADE,
    CONSTRAINT uq_booking_booker FOREIGN KEY
(
    booker_id
) REFERENCES users
(
    id
)
                   ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    text
    VARCHAR
(
    512
) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY
(
    id
),
    CONSTRAINT uq_comment_item FOREIGN KEY
(
    item_id
) REFERENCES items
                      ON DELETE CASCADE,
    CONSTRAINT uq_comment_author FOREIGN KEY
(
    author_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );