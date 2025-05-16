#!/bin/sh

set -e

KEYCLOAK_URL="http://keycloak:8081"
REALM="kairos-realm"
ADMIN_USER="admin" 
ADMIN_PASS="admin"

echo "Waiting for Keycloak to be ready..."
until curl -s "${KEYCLOAK_URL}/realms/${REALM}" > /dev/null; do
  sleep 5
done

echo "Keycloak is ready. Getting admin token..."

ACCESS_TOKEN=$(curl -s \
  -d "client_id=admin-cli" \
  -d "username=${ADMIN_USER}" \
  -d "password=${ADMIN_PASS}" \
  -d "grant_type=password" \
  "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" | jq -r .access_token)

# Function to create user + role
create_user() {
  USERNAME=$1
  PASSWORD=$2
  ROLE=$3
  FIRSTNAME=$4
  LASTNAME=$5
  EMAIL=$6

  echo "Creating user ${USERNAME} with role ${ROLE}..."

  curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM}/users" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    -H "Content-Type: application/json" \
    -d @- <<EOF
{
  "username": "${USERNAME}",
  "enabled": true,
  "firstName": "${FIRSTNAME}",
  "lastName": "${LASTNAME}", 
  "email": "${EMAIL}",
  "emailVerified": true,
  "credentials": [{
    "type": "password",
    "value": "${PASSWORD}",
    "temporary": false
  }]
}
EOF

  # Get userId
  USER_ID=$(curl -s "${KEYCLOAK_URL}/admin/realms/${REALM}/users?username=${USERNAME}" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" | jq -r '.[0].id')

  # Get role ID
  ROLE_ID=$(curl -s "${KEYCLOAK_URL}/admin/realms/${REALM}/roles/${ROLE}" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" | jq -r '{id: .id, name: .name}' | jq -s '.')

  # Assign role to user
  curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM}/users/${USER_ID}/role-mappings/realm" \
    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
    -H "Content-Type: application/json" \
    -d "${ROLE_ID}"

  echo "User ${USERNAME} created successfully."
}

# Create users
create_user "admin"     "admin"     "ADMIN"     "Admin"     "Admin"     "admin@admin.com"
create_user "organizer" "organizer" "ORGANIZER" "Org"       "Organizer" "organizer@event.com"
create_user "user"      "user"      "USER"      "User"      "User"      "user@kairos.com"
