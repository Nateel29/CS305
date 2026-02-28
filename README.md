# CS305 – Software Security (SNHU)

## Artemis Financial Secure Spring Boot Application

This project demonstrates practical secure software development skills built for **Artemis Financial**, a financial-services company that required stronger application security. The two primary goals were:

1. **Encrypted transport** – all communication protected with TLS (HTTPS).
2. **Integrity verification** – a `/hash` endpoint that returns a SHA-256 checksum so consumers can verify data has not been tampered with in transit.

---

## Features

| Feature | Implementation |
|---------|---------------|
| HTTPS / TLS | Spring Boot `server.ssl.*` properties + PKCS12 keystore (keytool) |
| Integrity hashing | SHA-256 via `java.security.MessageDigest` at `GET /hash` |
| Authentication | Spring Security HTTP Basic (replace with OAuth2/JWT in production) |
| Channel enforcement | `requiresChannel().requiresSecure()` – rejects plain-HTTP requests |
| Dependency scanning | OWASP Dependency-Check Maven plugin (fails build on CVSS ≥ 7) |
| XSS prevention | User-supplied input is HTML-escaped before rendering |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/snhu/sslserver/
│   │   ├── SslServerApplication.java       # Entry point
│   │   ├── controller/HashController.java  # /hash endpoint
│   │   └── config/SecurityConfig.java      # HTTPS + auth config
│   └── resources/
│       ├── application.properties          # TLS & security settings
│       └── keystore.p12                    # Self-signed certificate (dev only)
└── test/
    ├── java/com/snhu/sslserver/
    │   └── SslServerApplicationTests.java  # Unit/integration tests
    └── resources/
        └── application.properties          # Test overrides (SSL disabled)
dependency-check-suppressions.xml           # OWASP false-positive suppressions
```

---

## Prerequisites

- Java 17+
- Maven 3.8+

---

## Running the Application

```bash
# Build (skips OWASP scan for speed; remove the flag to include the scan)
mvn package -DskipDependencyCheck=true

# Start the server
java -jar target/ssl-server-0.0.1-SNAPSHOT.jar
```

The server starts on **`https://localhost:8443`**.

> **Note:** The included `keystore.p12` is self-signed for local development. Your browser will show a certificate warning; accept it or import the certificate into your trust store.

### Environment variables (production)

| Variable | Purpose | Default |
|----------|---------|---------|
| `KEYSTORE_PASSWORD` | PKCS12 keystore password | `changeit` |
| `APP_USER` | Application username | `user` |
| `APP_PASSWORD` | Application password | `changeit` |

**Always override the defaults in any non-development environment.**

---

## API Usage

### `GET /hash`

Returns a SHA-256 checksum for the supplied string.

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `data` | No | `Hello World!` | String to hash |

**Example (curl – accepts self-signed cert with `-k`):**

```bash
curl -k -u user:changeit "https://localhost:8443/hash?data=Hello+World%21"
```

**Example response:**

```html
<p>Data: Hello World!</p>
<p>SHA-256 Checksum: 7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069</p>
```

---

## Running Tests

```bash
mvn test -DskipDependencyCheck=true
```

---

## OWASP Dependency-Check

```bash
# Run the full dependency vulnerability scan (downloads NVD data on first run)
mvn dependency-check:check
```

The report is written to `target/dependency-check-report.html`. The build fails if any dependency has a CVSS score ≥ 7. Add false-positive suppression entries to `dependency-check-suppressions.xml`.

---

## Security Notes

- The self-signed certificate in `keystore.p12` is **for development only**. Replace it with a CA-signed certificate before deploying to a public environment.
- The in-memory user credentials are **for development only**. Integrate an external identity provider (OAuth2, LDAP/AD) for production.
- All passwords are read from environment variables; the placeholder values in `application.properties` must never be used in production.

