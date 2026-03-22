-- Add document generation fields to quotes table
ALTER TABLE quotes ADD COLUMN company_name VARCHAR(255);
ALTER TABLE quotes ADD COLUMN company_email VARCHAR(255);
ALTER TABLE quotes ADD COLUMN company_phone VARCHAR(100);
ALTER TABLE quotes ADD COLUMN company_address VARCHAR(500);
ALTER TABLE quotes ADD COLUMN customer_name VARCHAR(255);
ALTER TABLE quotes ADD COLUMN customer_email VARCHAR(255);
ALTER TABLE quotes ADD COLUMN customer_phone VARCHAR(100);
ALTER TABLE quotes ADD COLUMN customer_address VARCHAR(500);
ALTER TABLE quotes ADD COLUMN document_status VARCHAR(20);
ALTER TABLE quotes ADD COLUMN document_url VARCHAR(1000);
ALTER TABLE quotes ADD COLUMN generated_at TIMESTAMP;

-- Add document generation fields to invoices table
ALTER TABLE invoices ADD COLUMN company_name VARCHAR(255);
ALTER TABLE invoices ADD COLUMN company_email VARCHAR(255);
ALTER TABLE invoices ADD COLUMN company_phone VARCHAR(100);
ALTER TABLE invoices ADD COLUMN company_address VARCHAR(500);
ALTER TABLE invoices ADD COLUMN customer_name VARCHAR(255);
ALTER TABLE invoices ADD COLUMN customer_email VARCHAR(255);
ALTER TABLE invoices ADD COLUMN customer_phone VARCHAR(100);
ALTER TABLE invoices ADD COLUMN customer_address VARCHAR(500);
ALTER TABLE invoices ADD COLUMN document_status VARCHAR(20);
ALTER TABLE invoices ADD COLUMN document_url VARCHAR(1000);
ALTER TABLE invoices ADD COLUMN generated_at TIMESTAMP;


