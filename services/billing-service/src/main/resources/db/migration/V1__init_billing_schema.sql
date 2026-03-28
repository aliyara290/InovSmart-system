CREATE TABLE quotes
(
    id         UUID         NOT NULL PRIMARY KEY,
    tenant_id  VARCHAR(255) NOT NULL,
    client_id  UUID         NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    subtotal   NUMERIC(19, 4) NOT NULL DEFAULT 0,
    tax_rate   NUMERIC(5, 4)  NOT NULL DEFAULT 0,
    tax_amount NUMERIC(19, 4) NOT NULL DEFAULT 0,
    total      NUMERIC(19, 4) NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_quotes_tenant_id ON quotes (tenant_id);

CREATE TABLE quote_lines
(
    id         UUID           NOT NULL PRIMARY KEY,
    quote_id   UUID           NOT NULL REFERENCES quotes (id) ON DELETE CASCADE,
    product_id UUID           NOT NULL,
    quantity   INT            NOT NULL,
    unit_price NUMERIC(19, 4) NOT NULL,
    line_total NUMERIC(19, 4) NOT NULL
);

CREATE INDEX idx_quote_lines_quote_id ON quote_lines (quote_id);

CREATE TABLE invoices
(
    id         UUID         NOT NULL PRIMARY KEY,
    tenant_id  VARCHAR(255) NOT NULL,
    client_id  UUID         NOT NULL,
    quote_id   UUID,
    status     VARCHAR(20)  NOT NULL,
    subtotal   NUMERIC(19, 4) NOT NULL DEFAULT 0,
    tax_rate   NUMERIC(5, 4)  NOT NULL DEFAULT 0,
    tax_amount NUMERIC(19, 4) NOT NULL DEFAULT 0,
    total      NUMERIC(19, 4) NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_invoices_tenant_id ON invoices (tenant_id);
CREATE UNIQUE INDEX idx_invoices_quote_id_tenant_id ON invoices (quote_id, tenant_id) WHERE quote_id IS NOT NULL;

CREATE TABLE invoice_lines
(
    id         UUID           NOT NULL PRIMARY KEY,
    invoice_id UUID           NOT NULL REFERENCES invoices (id) ON DELETE CASCADE,
    product_id UUID           NOT NULL,
    quantity   INT            NOT NULL,
    unit_price NUMERIC(19, 4) NOT NULL,
    line_total NUMERIC(19, 4) NOT NULL
);

CREATE INDEX idx_invoice_lines_invoice_id ON invoice_lines (invoice_id);
