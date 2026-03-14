CREATE TABLE quotes
(
    id            UUID         NOT NULL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    tenant_id     VARCHAR(255) NOT NULL,
    client_name   VARCHAR(255) NOT NULL,
    client_email  VARCHAR(255),
    status        VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    total_ht      NUMERIC(19, 4),
    total_ttc     NUMERIC(19, 4),
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    validity_date TIMESTAMP
);

CREATE INDEX idx_quotes_tenant_id ON quotes (tenant_id);
CREATE INDEX idx_quotes_status ON quotes (status);

CREATE TABLE quote_lines
(
    id           UUID           NOT NULL PRIMARY KEY,
    quote_id     UUID           NOT NULL REFERENCES quotes (id) ON DELETE CASCADE,
    product_name VARCHAR(255)   NOT NULL,
    description  VARCHAR(1000),
    quantity     INT            NOT NULL,
    unit_price   NUMERIC(19, 4) NOT NULL,
    tax_rate     NUMERIC(5, 2),
    line_total   NUMERIC(19, 4)
);

CREATE INDEX idx_quote_lines_quote_id ON quote_lines (quote_id);
