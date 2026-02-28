# CS305
Software Security (SNHU)

Artemis Financial is a financial‑services company that needed stronger application security. The key issue was unsecured communication and weak integrity verification, so the goal was to protect data in transit and validate data integrity.

The most helpful part was using dependency analysis to see risks in third‑party libraries. The most challenging part was getting the dependency checks to run in Eclipse due to incompatibility issues, and not being able to set up VS Code until the very last assignment.

I added layered security through TLS certificates, encrypted transport, and integrity hashing. In future work, I would continue using dependency scanners, static analysis, code review checklists, and threat‑based mitigation selection.

I verified functionality by running the app on HTTPS, testing the /hash endpoint, and executing tests/build checks. After refactoring, I re‑ran security scans (OWASP Dependency‑Check) to detect newly introduced vulnerabilities.

Some useful tools and practices that aided me throughout this program include Keytool, Spring Boot TLS configuration, SHA‑256 via Java security APIs, Maven test/build flow, OWASP Dependency‑Check, and secure coding practices (least exposure, modern crypto, explicit security configuration).

In this repository, I am adding the refactored secure Spring Boot project, HTTPS + checksum implementation, configuration updates, and vulnerability assessment evidence demonstrating practical secure software development skills.
