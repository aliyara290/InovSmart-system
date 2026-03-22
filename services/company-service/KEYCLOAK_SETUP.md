# Keycloak Setup Guide - Company Service

## Required Configuration

> **Note**: This guide is for Keycloak v24+ which uses the new User Profile feature.
> For older versions (v23 and below), user attributes were managed directly on users.

### 1. Define User Attributes in User Profile (Keycloak v24+)

**IMPORTANT**: In Keycloak v24+, you must first define custom attributes in the User Profile schema before they can be used.

#### Step 1: Create Custom Attributes

1. Go to **Keycloak Admin Console**
2. Select Realm: `InovSmart`
3. Navigate to **Realm Settings** → **User Profile** tab
4. Click **Create attribute**
5. Add `tenantId` attribute:
   ```
   Attribute name: tenantId
   Display name: Tenant ID
   Required for: (leave empty - set programmatically)
   Permissions: 
     - Admin: Read/Write
     - User: Read only
   Validation: (optional - can add pattern for UUID)
   ```
6. Click **Create**
7. Click **Create attribute** again for `companyId`:
   ```
   Attribute name: companyId
   Display name: Company ID
   Required for: (leave empty)
   Permissions:
     - Admin: Read/Write
     - User: Read only
   ```
8. Click **Create**

#### Step 2: Verify Attributes Are Available

1. Go to **Users** → Select any user
2. In the **Details** tab, scroll down to see the custom attributes section
3. You should see `tenantId` and `companyId` fields available

### 2. Client Mappers for User Attributes

The JWT tokens need to include custom user attributes (`tenantId` and `companyId`). Configure protocol mappers for the `frontend-client`.

#### Add tenantId Mapper

1. Go to **Keycloak Admin Console**
2. Select Realm: `InovSmart`
3. Navigate to **Clients** → `frontend-client`
4. Go to **Client scopes** tab
5. Click on `frontend-client-dedicated` (or your default scope)
6. Click **Add mapper** → **By configuration** → **User Attribute**
7. Configure:
   ```
   Name: tenantId-mapper
   User Attribute: tenantId
   Token Claim Name: tenantId
   Claim JSON Type: String
   Add to ID token: ON
   Add to access token: ON
   Add to userinfo: ON
   Multivalued: OFF
   ```
8. Click **Save**

#### Add companyId Mapper

1. Click **Add mapper** → **By configuration** → **User Attribute**
2. Configure:
   ```
   Name: companyId-mapper
   User Attribute: companyId
   Token Claim Name: companyId
   Claim JSON Type: String
   Add to ID token: ON
   Add to access token: ON
   Add to userinfo: ON
   Multivalued: OFF
   ```
3. Click **Save**

### 3. Verify User Attributes After Registration

After a user is registered via the API, verify that users have the correct attributes:

1. Go to **Users** → Select user
2. In the **Details** tab, scroll down to the attributes section
3. Verify these attributes exist:
   - `tenantId`: `{UUID}`
   - `companyId`: `{UUID}`

> **Note**: In Keycloak v24+, attributes appear in the Details tab, not a separate Attributes tab

### 3. Test JWT Token

After configuration, request a new token and decode it. It should include:

```json
{
  "sub": "user-id",
  "email": "user@example.com",
  "tenantId": "429269ba-e118-4bc7-95dd-77fec27c7fc3",
  "companyId": "company-uuid",
  "realm_access": {
    "roles": ["OWNER", ...]
  },
  ...
}
```

### 4. Group Membership Mapper (Optional)

To include group information in the token:

1. Click **Add mapper** → **By configuration** → **Group Membership**
2. Configure:
   ```
   Name: groups-mapper
   Token Claim Name: groups
   Full group path: OFF
   Add to ID token: ON
   Add to access token: ON
   Add to userinfo: ON
   ```
3. Click **Save**

## Troubleshooting

### Issue: tenantId is null in JWT

**Cause**: User attributes not mapped to token claims

**Solution**: 
- Verify protocol mappers are configured correctly
- Ensure user has the attributes set (check Users → Attributes tab)
- Request a new token (old tokens won't have the new claims)

### Issue: User attributes not saved during registration

**Cause**: Attributes might be stored as single values instead of lists

**Fix in KeycloakAdapter.java**: Ensure attributes are set as lists:
```java
user.setAttributes(Map.of(
    "tenantId", List.of(tenantId.toString()),
    "companyId", List.of(companyId.toString())
));
```

### Issue: 403 Forbidden - Unauthorized access to tenantId

**Cause**: JWT doesn't contain tenantId claim

**Solution**:
1. Configure mappers as described above
2. Get a new access token
3. Verify token contains tenantId claim

## Required Keycloak Configuration Summary

```yaml
Realm: InovSmart

Clients:
  - frontend-client:
      Access Type: public
      Valid Redirect URIs: [http://localhost:3000/*, ...]
      Web Origins: [*]
      Client Scopes:
        - frontend-client-dedicated:
            Mappers:
              - tenantId-mapper (User Attribute)
              - companyId-mapper (User Attribute)
              - groups-mapper (Group Membership) [Optional]
  
  - admin-cli:
      Access Type: confidential
      Service Accounts Enabled: ON
      Client Secret: {your-secret}
      Service Account Roles:
        - realm-management: manage-users, manage-groups, view-users, view-groups

Groups Structure:
  - tenant-{UUID}
    ├── OWNER
    ├── ADMIN
    ├── MANAGER
    └── USER
```

## Verification Checklist

- [ ] Protocol mappers configured for frontend-client
- [ ] User attributes (tenantId, companyId) are set during registration
- [ ] JWT token contains tenantId and companyId claims
- [ ] Group structure created for each tenant
- [ ] Users assigned to appropriate role groups
- [ ] Admin client has necessary service account roles
