# Company Service

Multi-tenant SaaS company management microservice built with Hexagonal Architecture and Domain-Driven Design.

## Architecture

This service follows **Hexagonal Architecture (Ports & Adapters)** principles:

- **Domain Layer**: Pure business logic (no framework dependencies)
- **Application Layer**: Use cases and port interfaces
- **Adapters Layer**: External integrations (REST, JPA, Feign)
- **Infrastructure Layer**: Cross-cutting concerns (security, config, mapping)

## Tech Stack

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- Spring Security (JWT OAuth2 Resource Server)
- Spring Cloud OpenFeign
- PostgreSQL
- Keycloak (external auth server)
- MapStruct (object mapping)
- Maven

## Features

- **Multi-tenancy**: Tenant isolation by `tenantId`
- **Company Registration**: Atomic registration with Keycloak integration
- **User Management**: Role-based access control via Keycloak groups
- **User Invitations**: Token-based invitation system with expiry
- **JWT Security**: Stateless authentication with role extraction
- **RESTful API**: Public, protected, and internal endpoints

## Prerequisites

- Java 17+
- PostgreSQL 13+
- Keycloak 23+ (running as standalone Docker container)
- Maven 3.8+

## Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=company_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Keycloak
KEYCLOAK_BASE_URL=http://localhost:8080
KEYCLOAK_REALM=platform
KEYCLOAK_ADMIN_CLIENT_ID=admin-cli
KEYCLOAK_ADMIN_SECRET=your-admin-secret
KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/platform
KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/platform/protocol/openid-connect/certs

# Internal API
INTERNAL_API_KEY=your-internal-api-key

# Server
SERVER_PORT=8081
```

## Database Schema

The service uses the following tables:

- `companies` - Company/tenant data
- `company_users` - User-company relationships
- `company_invitations` - Pending user invitations

## API Endpoints

### Public Endpoints

- `POST /api/v1/companies/register` - Register new company
- `POST /api/v1/companies/invitations/accept` - Accept invitation

### Protected Endpoints (JWT required)

**Company Management**
- `GET /api/v1/companies/{tenantId}` - Get company details
- `PUT /api/v1/companies/{tenantId}` - Update company (OWNER, MANAGER)
- `PATCH /api/v1/companies/{tenantId}/status` - Update status (OWNER)
- `DELETE /api/v1/companies/{tenantId}` - Delete company (OWNER)

**User Management**
- `GET /api/v1/companies/{tenantId}/users` - List users
- `GET /api/v1/companies/{tenantId}/users/{userId}` - Get user
- `POST /api/v1/companies/{tenantId}/users/invite` - Invite user (OWNER, MANAGER)
- `PUT /api/v1/companies/{tenantId}/users/{userId}/role` - Change role (OWNER)
- `PATCH /api/v1/companies/{tenantId}/users/{userId}/status` - Change status (OWNER, MANAGER)
- `DELETE /api/v1/companies/{tenantId}/users/{userId}` - Remove user (OWNER)

### Internal Endpoints (Service-to-Service)

- `GET /internal/v1/companies/{tenantId}` - Get company (requires `X-Internal-Api-Key` header)

## Running the Service

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Or run the JAR
java -jar target/company-service-1.0.0-SNAPSHOT.jar
```

## Keycloak Setup

The service expects Keycloak to have:

1. A realm named `platform`
2. Client `admin-cli` with service account enabled
3. Custom JWT claims: `tenantId`, `companyId`
4. Realm roles extracted from `realm_access.roles`

## Registration Flow

1. Save company (status=PENDING)
2. Create user in Keycloak
3. Create tenant group in Keycloak with role subgroups
4. Assign user to OWNER group
5. Save company user record
6. Update company status to ACTIVE
7. Return JWT tokens

**Rollback**: If any step fails, the service performs saga-style rollback.

## Tenant Security

- JWT contains `tenantId` claim
- `TenantContextFilter` extracts and stores `tenantId` in ThreadLocal
- `TenantSecurityService` validates tenant access on every protected operation
- Unauthorized access throws `403 Forbidden`

## Error Handling

Global exception handler returns standardized error responses:

- `404` - Resource not found
- `409` - Conflict (user exists, invitation already accepted)
- `410` - Gone (invitation expired)
- `403` - Forbidden (unauthorized tenant access)
- `502` - Bad Gateway (Keycloak integration error)
- `400` - Bad Request (validation errors)

## Development

### MapStruct

Mappers are auto-generated during compilation. To regenerate:

```bash
mvn clean compile
```

### Adding New Features

Follow hexagonal architecture principles:

1. Define domain model (no framework dependencies)
2. Create port interfaces in `application.port.in` or `application.port.out`
3. Implement use case in `application.service`
4. Create adapters in `adapters.in` (controllers) or `adapters.out` (persistence/feign)
5. Add mappers in `infrastructure.mapper`

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## License

Proprietary - All rights reserved
