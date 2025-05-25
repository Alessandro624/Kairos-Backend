# Kairos Backend

Backend del progetto **Kairos** – Piattaforma di gestione e prenotazione eventi.

---

## Configurazione

Prima di eseguire il progetto è necessario configurare alcune variabili d’ambiente. Queste vengono utilizzate dal backend per connettersi a servizi esterni (DB, OAuth2, Keycloak, Email, ecc.).

### File `kairos-start.sh`

Apri il file `kairos-start.sh` e rimuovi i commenti dalle seguenti righe, inserendo i tuoi valori reali:

```bash
# Set required environment variables (remove comment and configure)
export POSTGRES_USER="your-postgres-user"
export POSTGRES_PASSWORD="your-postgres-password"
export JWT_SECRET_KAIROS="your-jwt-secret"
export GOOGLE_CLIENT_ID_KAIROS="your-google-client-id"
export GOOGLE_CLIENT_SECRET_KAIROS="your-google-client-secret"
export KEYCLOAK_CLIENT_ID_KAIROS="your-keycloak-client-id"
export KEYCLOAK_CLIENT_SECRET_KAIROS="your-keycloak-client-secret"
export SPRING_MAIL_USERNAME_KAIROS="your-spring-mail-username"
export SPRING_MAIL_PASSWORD_KAIROS="your-spring-mail-password"
```

---

## Esecuzione del progetto

### Linux/macOS

Nel terminale, esegui il backend con il comando:

```bash
sudo sh kairos-start.sh
```

Questo comando imposta le variabili d’ambiente e avvia l'applicazione con Maven.

---

### Windows

Su Windows è necessario eseguire uno script `.bat` che richiama lo script `.sh` (per settare correttamente le variabili anche in ambienti tipo Git Bash o WSL).

1. Fai doppio clic sul file o esegui sul terminale:
   **`kairos-start.bat`**

2. Nel terminale aperto dallo script `.bat`, esegui il comando:

   ```bat
    kairos-start.sh
   ```

Assicurati che Git Bash o WSL sia installato.

---
