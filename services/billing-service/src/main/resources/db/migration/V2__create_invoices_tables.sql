CREATE TABLE invoices
(
    id             UUID         NOT NULL PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    tenant_id      VARCHAR(255) NOT NULL,
    quote_id       UUID REFERENCES quotes (id) ON DELETE SET NULL,
    invoice_number VARCHAR(100) NOT NULL UNIQUE,
    status         VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    total_ht       NUMERIC(19, 4),
    total_ttc      NUMERIC(19, 4),
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    due_date       TIMESTAMP
);

CREATE INDEX idx_invoices_tenant_id ON invoices (tenant_id);
CREATE INDEX idx_invoices_status ON invoices (status);
CREATE INDEX idx_invoices_quote_id ON invoices (quote_id);

CREATE TABLE invoice_lines
(
    id           UUID           NOT NULL PRIMARY KEY,
    invoice_id   UUID           NOT NULL REFERENCES invoices (id) ON DELETE CASCADE,
    product_name VARCHAR(255)   NOT NULL,
    description  VARCHAR(1000),
    quantity     INT            NOT NULL,
    unit_price   NUMERIC(19, 4) NOT NULL,
    tax_rate     NUMERIC(5, 2),
    line_total   NUMERIC(19, 4)
);

CREATE INDEX idx_invoice_lines_invoice_id ON invoice_lines (invoice_id);
