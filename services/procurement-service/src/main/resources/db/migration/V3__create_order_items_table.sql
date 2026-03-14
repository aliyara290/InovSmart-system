CREATE TABLE order_items
(
    id                 UUID           NOT NULL PRIMARY KEY,
    order_id           UUID           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id         UUID           NOT NULL,
    quantity           INT            NOT NULL CHECK (quantity > 0),
    unit_price         NUMERIC(19, 4) NOT NULL,
    delivered_quantity INT            NOT NULL DEFAULT 0
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_product_id ON order_items (product_id);
