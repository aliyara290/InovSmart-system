CREATE TABLE delivered_orders
(
    id           BIGSERIAL NOT NULL PRIMARY KEY,
    order_id     UUID      NOT NULL UNIQUE,
    delivered_at TIMESTAMP NOT NULL DEFAULT now()
);

COMMENT ON TABLE delivered_orders IS 'Idempotency guard: one row per successfully delivered order to prevent duplicate stock-increase events.';

CREATE UNIQUE INDEX uidx_delivered_orders_order_id ON delivered_orders (order_id);
