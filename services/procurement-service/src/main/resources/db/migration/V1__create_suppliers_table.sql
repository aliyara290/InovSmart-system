CREATE TABLE suppliers
(
    id            UUID         NOT NULL PRIMARY KEY,
    tenant_id     VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    phone         VARCHAR(50),
    address       VARCHAR(500),
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_suppliers_tenant_id ON suppliers (tenant_id);
