# Keycloak Integration Workflow - Company Service

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          COMPANY REGISTRATION FLOW                               │
└─────────────────────────────────────────────────────────────────────────────────┘

┌──────────────────┐
│   Client/API     │
│    Request       │
└────────┬─────────┘
         │
         │ POST /register
         │ {company, owner}
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                      RegisterCompanyService                                      │
│  ┌───────────────────────────────────────────────────────────────────────────┐ │
│  │ 1. Generate tenantId & companyId (UUID)                                   │ │
│  └───────────────────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────┬──────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         KeycloakAdapter (Port)                                   │
│  ┌───────────────────────────────────────────────────────────────────────────┐ │
│  │ 2. createUser(tenantId, companyId, email, password, firstName, lastName)  │ │
│  └───────────────────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────┬──────────────────────────────────────────────┘
                                   │
         ┌─────────────────────────┼─────────────────────────┐
         │                         │                         │
         ▼                         ▼                         ▼
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│ KeycloakUser     │    │ KeycloakGroup    │    │ KeycloakToken    │
│    Client        │    │    Client        │    │    Client        │
│  (Feign)         │    │  (Feign)         │    │  (Feign)         │
└────────┬─────────┘    └────────┬─────────┘    └────────┬─────────┘
         │                       │                       │
         │ Uses Admin Token      │ Uses Admin Token      │ Gets Tokens
         │ (via Interceptor)     │ (via Interceptor)     │ (Direct)
         │                       │                       │
         └───────────┬───────────┴───────────┬───────────┘
                     │                       │
                     ▼                       ▼
         ┌────────────────────┐  ┌────────────────────┐
         │ KeycloakAdmin      │  │ KeycloakToken      │
         │ FeignConfig        │  │ ClientConfig       │
         │                    │  │                    │
         │ +Interceptor       │  │ (Form Encoder)     │
         │  (adds Bearer)     │  │                    │
         └─────────┬──────────┘  └────────────────────┘
                   │
                   ▼
         ┌────────────────────┐
         │ KeycloakToken      │
         │   Provider         │
         │                    │
         │ • Caches Token     │
         │ • Auto-refresh     │
         │ • Client Creds     │
         └────────────────────┘
                   │
                   ▼
         ┌────────────────────────────────────┐
         │         KEYCLOAK SERVER            │
         │                                    │
         │  /realms/{realm}/protocol/         │
         │    openid-connect/token            │
         │                                    │
         │  /admin/realms/{realm}/users       │
         │  /admin/realms/{realm}/groups      │
         └────────────────────────────────────┘
```

## Detailed Flow Sequence

```
┌────────┐         ┌──────────────┐         ┌──────────────┐         ┌──────────┐
│ Client │         │   Service    │         │   Keycloak   │         │ Database │
│        │         │   Layer      │         │   Adapter    │         │          │
└───┬────┘         └──────┬───────┘         └──────┬───────┘         └────┬─────┘
    │                     │                        │                      │
    │ 1. Register Company │                        │                      │
    ├────────────────────>│                        │                      │
    │                     │                        │                      │
    │                     │ 2. Create User         │                      │
    │                     ├───────────────────────>│                      │
    │                     │                        │                      │
    │                     │                        │ 3. Get Admin Token   │
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │                        │<─────────┘           │
    │                     │                        │                      │
    │                     │                        │ 4. POST /users       │
    │                     │                        │   (with attributes:  │
    │                     │                        │    tenantId,         │
    │                     │                        │    companyId)        │
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │   5. Return userId     │<─────────┘           │
    │                     │<───────────────────────┤                      │
    │                     │                        │                      │
    │                     │ 6. Save Company        │                      │
    │                     ├───────────────────────────────────────────────>│
    │                     │                        │                      │
    │                     │                        │                      │
    │                     │ 7. Create Group        │                      │
    │                     │    Structure           │                      │
    │                     ├───────────────────────>│                      │
    │                     │                        │                      │
    │                     │                        │ 8. POST /groups      │
    │                     │                        │   (tenant-{uuid})    │
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │                        │<─────────┘           │
    │                     │                        │                      │
    │                     │                        │ 9. Create Subgroups  │
    │                     │                        │   (OWNER, ADMIN,     │
    │                     │                        │    MANAGER, USER)    │
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │                        │<─────────┘           │
    │                     │                        │                      │
    │                     │ 10. Assign User        │                      │
    │                     │     to OWNER Group     │                      │
    │                     ├───────────────────────>│                      │
    │                     │                        │                      │
    │                     │                        │ 11. PUT /users/{id}  │
    │                     │                        │     /groups/{groupId}│
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │                        │<─────────┘           │
    │                     │                        │                      │
    │                     │ 12. Save CompanyUser   │                      │
    │                     ├───────────────────────────────────────────────>│
    │                     │                        │                      │
    │                     │ 13. Get User Token     │                      │
    │                     ├───────────────────────>│                      │
    │                     │                        │                      │
    │                     │                        │ 14. POST /token      │
    │                     │                        │   (grant_type:       │
    │                     │                        │    password)         │
    │                     │                        ├──────────┐           │
    │                     │                        │          │           │
    │                     │   15. Return Token     │<─────────┘           │
    │                     │<───────────────────────┤                      │
    │                     │                        │                      │
    │  16. Response       │                        │                      │
    │  {tenantId,         │                        │                      │
    │   companyId,        │                        │                      │
    │   userId,           │                        │                      │
    │   accessToken}      │                        │                      │
    │<────────────────────┤                        │                      │
    │                     │                        │                      │
```

## Component Breakdown

### 1. **Feign Clients**
```
KeycloakUserClient
├── createUser()          → POST /admin/realms/{realm}/users
├── getUser()             → GET /admin/realms/{realm}/users/{userId}
├── deleteUser()          → DELETE /admin/realms/{realm}/users/{userId}
├── addUserToGroup()      → PUT /admin/realms/{realm}/users/{userId}/groups/{groupId}
└── removeUserFromGroup() → DELETE /admin/realms/{realm}/users/{userId}/groups/{groupId}

KeycloakGroupClient
├── createGroup()         → POST /admin/realms/{realm}/groups
├── searchGroups()        → GET /admin/realms/{realm}/groups?search={name}
├── createSubgroup()      → POST /admin/realms/{realm}/groups/{groupId}/children
├── getGroup()            → GET /admin/realms/{realm}/groups/{groupId}
├── getGroupChildren()    → GET /admin/realms/{realm}/groups/{groupId}/children
└── getGroupMembers()     → GET /admin/realms/{realm}/groups/{groupId}/members

KeycloakTokenClient
├── getAdminToken()       → POST /realms/{realm}/protocol/openid-connect/token
│                            (grant_type: client_credentials)
└── getUserToken()        → POST /realms/{realm}/protocol/openid-connect/token
                             (grant_type: password)
```

### 2. **Authentication Flow**
```
┌─────────────────────────────────────────────────────────────┐
│              Admin Token Management                          │
│                                                              │
│  KeycloakTokenProvider                                       │
│  ├── Maintains cached admin token                           │
│  ├── Auto-refreshes 60s before expiry                       │
│  ├── Uses client_credentials grant                          │
│  └── Synchronized token access                              │
│                                                              │
│  KeycloakAdminFeignConfig                                    │
│  └── RequestInterceptor adds "Bearer {token}" to all        │
│      KeycloakUserClient & KeycloakGroupClient requests       │
└─────────────────────────────────────────────────────────────┘
```

### 3. **Group Hierarchy Structure**
```
Keycloak Groups
│
└── tenant-{tenantId}                    (Parent Group)
    ├── OWNER                            (Role Subgroup)
    ├── ADMIN                            (Role Subgroup)
    ├── MANAGER                          (Role Subgroup)
    └── USER                             (Role Subgroup)
```

### 4. **User Attributes**
```
Keycloak User Attributes:
├── tenantId: {UUID}      → Multi-tenancy identifier
├── companyId: {UUID}     → Company association
├── username: {email}     → User login
├── email: {email}        → Email address
├── firstName: {string}   → First name
├── lastName: {string}    → Last name
└── enabled: true         → Account status
```

## Key Operations

### **Create User**
1. Build `KeycloakUserRepresentation` with attributes
2. Set credentials (password)
3. POST to Keycloak Admin API
4. Extract userId from Location header
5. Return userId

### **Create Company Group Structure**
1. Create parent group: `tenant-{tenantId}`
2. Extract groupId from response
3. For each UserRole (OWNER, ADMIN, MANAGER, USER):
   - Create subgroup under parent
4. Return success

### **Assign User to Role**
1. Search for tenant group by name
2. Get group children (role subgroups)
3. Find matching role subgroup
4. Add user to role group via PUT request

### **Change User Role**
1. Find old role group ID
2. Find new role group ID
3. Remove user from old group
4. Add user to new group

### **Get User Access Token**
1. Call token endpoint with password grant
2. Provide username, password, client_id
3. Return access_token from response

## Configuration Requirements

```yaml
keycloak:
  base-url: http://localhost:8080
  realm: invoSmart
  admin:
    client-id: admin-cli
    client-secret: {secret}
```

## Error Handling

- All operations wrapped in try-catch
- Throws `KeycloakIntegrationException` on failure
- Rollback mechanism in `RegisterCompanyService`:
  - Deletes Keycloak user if company save fails
  - Deletes company if any step fails
- Comprehensive logging at each step

## Security Features

1. **Admin Token Caching**: Reduces token requests
2. **Token Auto-Refresh**: Prevents expiry mid-operation
3. **Request Interceptor**: Automatic Bearer token injection
4. **Multi-Tenancy**: Isolated groups per tenant
5. **Role-Based Access**: Hierarchical group structure
