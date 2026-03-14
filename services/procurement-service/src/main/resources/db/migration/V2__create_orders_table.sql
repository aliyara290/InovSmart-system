CREATE TABLE orders
(
    id                   UUID         NOT NULL PRIMARY KEY,
    tenant_id            VARCHAR(255) NOT NULL,
    supplier_id          UUID         NOT NULL REFERENCES suppliers (id),
    status               VARCHAR(20)  NOT NULL DEFAULT 'CREATED',
    notes                VARCHAR(1000),
    total_excluding_tax  NUMERIC(19, 4),
    total_ttc            NUMERIC(19, 4),
    created_at           TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at           TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_orders_tenant_id ON orders (tenant_id);
CREATE INDEX idx_orders_supplier_id ON orders (supplier_id);
CREATE INDEX idx_orders_status ON orders (status);
