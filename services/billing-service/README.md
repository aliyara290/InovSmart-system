# Billing Service

Spring Boot microservice for managing quotes and invoices following hexagonal architecture and domain-driven design principles.

## Features

- **Quote Management**: Create, update, send, accept, and reject quotes
- **Invoice Management**: Create invoices manually or from accepted quotes
- **Stock Integration**: Automatic stock reservation on quote acceptance and adjustment on invoice creation
- **Multi-tenancy**: Tenant isolation via JWT claims
- **Security**: OAuth2 Resource Server with Keycloak integration
- **Resilience**: Circuit breaker and retry policies for external service calls

## Architecture

- **Domain Layer**: Pure business logic with rich domain models
- **Application Layer**: Use cases, ports, DTOs, and mappers
- **Adapter Layer**: REST controllers, JPA persistence, Feign clients
- **Infrastructure Layer**: Security, exception handling, configuration

## Prerequisites

- Java 21+
- PostgreSQL database
- Keycloak server (for authentication)
- Config Server (optional)
- Eureka Server (optional)
- Inventory Service (running on inventory-service endpoint)

## API Endpoints

Base URL: `http://localhost:8084`

### Authentication

All endpoints require a valid JWT Bearer token from Keycloak.

```bash
Authorization: Bearer <your-jwt-token>
```

The JWT must contain:
- `tenantId` or `companyId` claim for multi-tenancy
- `realm_access.roles` for role-based access control

---

## Quote Endpoints

### 1. Create Quote

**POST** `/api/v1/quotes`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

**Request Body**:
```json
{
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "taxRate": 0.15,
  "lines": [
    {
      "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 10,
      "unitPrice": 99.99
    },
    {
      "productId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "quantity": 5,
      "unitPrice": 149.50
    }
  ]
}
```

**Response** (201 Created):
```json
{
  "id": "d4e5f6a7-b8c9-0123-def0-123456789abc",
  "tenantId": "company-123",
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "DRAFT",
  "lines": [
    {
      "id": "e5f6a7b8-c9d0-1234-ef01-23456789abcd",
      "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 10,
      "unitPrice": 99.99,
      "lineTotal": 999.90
    },
    {
      "id": "f6a7b8c9-d0e1-2345-f012-3456789abcde",
      "productId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "quantity": 5,
      "unitPrice": 149.50,
      "lineTotal": 747.50
    }
  ],
  "subtotal": 1747.40,
  "taxRate": 0.15,
  "taxAmount": 262.11,
  "total": 2009.51,
  "createdAt": "2026-03-20T16:30:00",
  "updatedAt": "2026-03-20T16:30:00"
}
```

---

### 2. Update Quote

**PUT** `/api/v1/quotes/{id}`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

**Request Body**:
```json
{
  "taxRate": 0.20,
  "lines": [
    {
      "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 15,
      "unitPrice": 89.99
    }
  ]
}
```

**Response** (200 OK): Same structure as Create Quote

---

### 3. Get Quote by ID

**GET** `/api/v1/quotes/{id}`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`, `EMPLOYEE`

**Example**:
```bash
curl -X GET http://localhost:8084/api/v1/quotes/d4e5f6a7-b8c9-0123-def0-123456789abc \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Same structure as Create Quote

---

### 4. Get All Quotes

**GET** `/api/v1/quotes`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`, `EMPLOYEE`

**Example**:
```bash
curl -X GET http://localhost:8084/api/v1/quotes \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK):
```json
[
  {
    "id": "d4e5f6a7-b8c9-0123-def0-123456789abc",
    "tenantId": "company-123",
    "clientId": "123e4567-e89b-12d3-a456-426614174000",
    "status": "DRAFT",
    "lines": [...],
    "subtotal": 1747.40,
    "taxRate": 0.15,
    "taxAmount": 262.11,
    "total": 2009.51,
    "createdAt": "2026-03-20T16:30:00",
    "updatedAt": "2026-03-20T16:30:00"
  }
]
```

---

### 5. Send Quote

**PUT** `/api/v1/quotes/{id}/send`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

Changes quote status from `DRAFT` to `SENT`.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/quotes/d4e5f6a7-b8c9-0123-def0-123456789abc/send \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Quote with status `SENT`

---

### 6. Accept Quote

**PUT** `/api/v1/quotes/{id}/accept`

**Roles**: `OWNER`, `MANAGER`

Changes quote status from `SENT` to `ACCEPTED` and reserves stock in inventory service.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/quotes/d4e5f6a7-b8c9-0123-def0-123456789abc/accept \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Quote with status `ACCEPTED`

**Note**: This will call inventory-service to reserve stock. If stock reservation fails, the quote will not be accepted and a 422 error will be returned.

---

### 7. Reject Quote

**PUT** `/api/v1/quotes/{id}/reject`

**Roles**: `OWNER`, `MANAGER`

Changes quote status to `REJECTED` and releases any reserved stock.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/quotes/d4e5f6a7-b8c9-0123-def0-123456789abc/reject \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Quote with status `REJECTED`

---

## Invoice Endpoints

### 1. Create Invoice (Manual)

**POST** `/api/v1/invoices`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

**Request Body**:
```json
{
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "taxRate": 0.15,
  "lines": [
    {
      "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 3,
      "unitPrice": 199.99
    }
  ]
}
```

**Response** (201 Created):
```json
{
  "id": "f7g8h9i0-j1k2-3456-lmno-pqrstuvwxyz1",
  "tenantId": "company-123",
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "quoteId": null,
  "status": "DRAFT",
  "lines": [
    {
      "id": "g8h9i0j1-k2l3-4567-mnop-qrstuvwxyz12",
      "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 3,
      "unitPrice": 199.99,
      "lineTotal": 599.97
    }
  ],
  "subtotal": 599.97,
  "taxRate": 0.15,
  "taxAmount": 90.00,
  "total": 689.97,
  "createdAt": "2026-03-20T17:00:00",
  "updatedAt": "2026-03-20T17:00:00"
}
```

**Note**: This will adjust inventory stock immediately.

---

### 2. Create Invoice from Quote

**POST** `/api/v1/invoices/from-quote/{quoteId}`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

Creates an invoice from an accepted quote. Ensures idempotency (one invoice per quote).

**Example**:
```bash
curl -X POST http://localhost:8084/api/v1/invoices/from-quote/d4e5f6a7-b8c9-0123-def0-123456789abc \
  -H "Authorization: Bearer <token>"
```

**Response** (201 Created):
```json
{
  "id": "h9i0j1k2-l3m4-5678-nopq-rstuvwxyz123",
  "tenantId": "company-123",
  "clientId": "123e4567-e89b-12d3-a456-426614174000",
  "quoteId": "d4e5f6a7-b8c9-0123-def0-123456789abc",
  "status": "DRAFT",
  "lines": [...],
  "subtotal": 1747.40,
  "taxRate": 0.15,
  "taxAmount": 262.11,
  "total": 2009.51,
  "createdAt": "2026-03-20T17:15:00",
  "updatedAt": "2026-03-20T17:15:00"
}
```

**Error** (409 Conflict if invoice already exists for quote):
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Invoice already exists for quote: d4e5f6a7-b8c9-0123-def0-123456789abc",
  "timestamp": "2026-03-20T17:15:00"
}
```

---

### 3. Get Invoice by ID

**GET** `/api/v1/invoices/{id}`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`, `EMPLOYEE`

**Example**:
```bash
curl -X GET http://localhost:8084/api/v1/invoices/h9i0j1k2-l3m4-5678-nopq-rstuvwxyz123 \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Same structure as Create Invoice

---

### 4. Get All Invoices

**GET** `/api/v1/invoices`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`, `EMPLOYEE`

**Example**:
```bash
curl -X GET http://localhost:8084/api/v1/invoices \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Array of invoices

---

### 5. Send Invoice

**PUT** `/api/v1/invoices/{id}/send`

**Roles**: `OWNER`, `MANAGER`, `ACCOUNTANT`

Changes invoice status from `DRAFT` to `SENT`.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/invoices/h9i0j1k2-l3m4-5678-nopq-rstuvwxyz123/send \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Invoice with status `SENT`

---

### 6. Mark Invoice as Paid

**PUT** `/api/v1/invoices/{id}/pay`

**Roles**: `OWNER`, `MANAGER`

Changes invoice status from `SENT` to `PAID`.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/invoices/h9i0j1k2-l3m4-5678-nopq-rstuvwxyz123/pay \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Invoice with status `PAID`

---

### 7. Cancel Invoice

**PUT** `/api/v1/invoices/{id}/cancel`

**Roles**: `OWNER`, `MANAGER`

Changes invoice status to `CANCELLED`.

**Example**:
```bash
curl -X PUT http://localhost:8084/api/v1/invoices/h9i0j1k2-l3m4-5678-nopq-rstuvwxyz123/cancel \
  -H "Authorization: Bearer <token>"
```

**Response** (200 OK): Invoice with status `CANCELLED`

---

## Error Responses

### 400 Bad Request
Invalid state transition or validation error.
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot send quote from status: ACCEPTED",
  "timestamp": "2026-03-20T17:30:00"
}
```

### 404 Not Found
Resource not found or not accessible by tenant.
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Quote not found: d4e5f6a7-b8c9-0123-def0-123456789abc",
  "timestamp": "2026-03-20T17:30:00"
}
```

### 409 Conflict
Duplicate resource.
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Invoice already exists for quote: d4e5f6a7-b8c9-0123-def0-123456789abc",
  "timestamp": "2026-03-20T17:30:00"
}
```

### 422 Unprocessable Entity
Stock reservation or business rule failure.
```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Stock reservation failed for quote d4e5f6a7-b8c9-0123-def0-123456789abc",
  "timestamp": "2026-03-20T17:30:00"
}
```

---

## Testing with cURL

### Complete Flow Example

```bash
# 1. Create a quote
curl -X POST http://localhost:8084/api/v1/quotes \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "123e4567-e89b-12d3-a456-426614174000",
    "taxRate": 0.15,
    "lines": [
      {
        "productId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "quantity": 10,
        "unitPrice": 99.99
      }
    ]
  }'

# 2. Send the quote
curl -X PUT http://localhost:8084/api/v1/quotes/{quote-id}/send \
  -H "Authorization: Bearer <token>"

# 3. Accept the quote (reserves stock)
curl -X PUT http://localhost:8084/api/v1/quotes/{quote-id}/accept \
  -H "Authorization: Bearer <token>"

# 4. Create invoice from quote
curl -X POST http://localhost:8084/api/v1/invoices/from-quote/{quote-id} \
  -H "Authorization: Bearer <token>"

# 5. Send invoice
curl -X PUT http://localhost:8084/api/v1/invoices/{invoice-id}/send \
  -H "Authorization: Bearer <token>"

# 6. Mark invoice as paid
curl -X PUT http://localhost:8084/api/v1/invoices/{invoice-id}/pay \
  -H "Authorization: Bearer <token>"
```

---

## Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=billing_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Keycloak
KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/invosmart
KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/invosmart/protocol/openid-connect/certs

# Config Server (optional)
CONFIG_SERVER_ENABLED=false
CONFIG_SERVER_URI=http://localhost:8888

# Eureka (optional)
EUREKA_ENABLED=false
EUREKA_URI=http://localhost:8761/eureka

# Server
SERVER_PORT=8084
```

---

## Building and Running

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Or with custom profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Database Migration

Flyway migrations are located in `src/main/resources/db/migration/`.

On startup, Flyway will automatically create the following tables:
- `quotes`
- `quote_lines`
- `invoices`
- `invoice_lines`

---

## Dependencies

- Spring Boot 4.0.4
- Spring Data JPA
- Spring Security OAuth2 Resource Server
- Spring Cloud OpenFeign
- PostgreSQL Driver
- Flyway
- MapStruct
- Resilience4j
- Lombok
