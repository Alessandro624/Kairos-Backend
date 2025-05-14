#!/usr/bin/env bash
# kairos-start.sh - Universal script for Kairos environment

#============================================
# SETTING ENVIRONMENT VARIABLES
#============================================
# Set required environment variables (remove comment and configure)
# export POSTGRES_USER="your-postgres-user"
# export POSTGRES_PASSWORD="your-postgres-password"
# export JWT_SECRET_KAIROS="your-jwt-secret"
# export GOOGLE_CLIENT_ID_KAIROS="your-google-client-id"
# export GOOGLE_CLIENT_SECRET_KAIROS="your-google-client-secret"
# export KEYCLOAK_CLIENT_ID_KAIROS="your-keycloak-client-id"
# export KEYCLOAK_CLIENT_SECRET_KAIROS="your-keycloak-client-secret"

#============================================
# HELPER FUNCTIONS
#============================================

# Function to check administrator privileges
check_admin() {
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        # Windows with Git Bash/Cygwin
        if ! net session &>/dev/null; then
            echo "ERROR: This script requires administrative privileges on Windows!"
            echo "Run as administrator"
            exit 1
        fi
    else
        # Unix-like (Linux/macOS)
        if [ "$(id -u)" -ne 0 ]; then
            echo "ERROR: This script requires root privileges!"
            echo "Run with sudo"
            exit 1
        fi
    fi
}

# Function to determine hosts file path
get_hosts_path() {
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        # Windows with Git Bash/Cygwin
        echo "/c/Windows/System32/drivers/etc/hosts"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        echo "/private/etc/hosts"
    else
        # Linux and other Unix
        echo "/etc/hosts"
    fi
}

# Function to backup and modify hosts file
modify_hosts() {
    local hosts_path=$1
    echo "Configuring DNS resolver (adding 'keycloak' entry to hosts file)..."
    
    # Backup hosts file
    cp "$hosts_path" "${hosts_path}.bak"
    
    # Check if entry already exists
    if ! grep -q "127\.0\.0\.1\s\+keycloak" "$hosts_path"; then
        echo "127.0.0.1 keycloak" >> "$hosts_path"
    fi
}

# Function to restore original hosts file
restore_hosts() {
    local hosts_path=$1
    echo "Restoring hosts file..."
    
    if [ -f "${hosts_path}.bak" ]; then
        mv "${hosts_path}.bak" "$hosts_path"
        echo "Hosts file restored"
    else
        echo "WARNING: Hosts file backup not found!"
    fi
}

# Cleanup function for errors or termination
cleanup() {
    restore_hosts "$HOSTS_PATH"
    echo "Operation completed"
    exit 0
}

#============================================
# START MAIN SCRIPT
#============================================
# Check admin privileges
check_admin

# Set hosts file path
HOSTS_PATH=$(get_hosts_path)

# Configure trap for interruptions
trap cleanup INT TERM

# Modify hosts file
modify_hosts "$HOSTS_PATH"

#============================================
# START DOCKER COMPOSE
#============================================
echo "Starting containers with Docker Compose..."
if docker compose up --build -d; then
    echo "Kairos environment started successfully!"
    echo "Logs available: docker compose logs -f"
else
    echo "ERROR: Unable to start Docker containers!"
    cleanup
    # shellcheck disable=SC2317
    exit 1
fi

#============================================
# WAIT AND CLEANUP
#============================================
echo ""
echo "Press Enter to terminate and restore hosts file..."
read -r

# Terminate and cleanup
cleanup
