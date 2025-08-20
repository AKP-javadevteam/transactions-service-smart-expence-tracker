# Transactions Service — Smart Expense Project

This is the **Transactions Service** for the Smart Expense project.  
It manages **financial transactions**, supports **CSV ingestion**, and provides APIs for listing, categorizing, and validating transactions.

---

## 🚀 Tech Stack

- **Project**: Maven, Java 17
- **Spring Boot**: 3.5.4

### Dependencies
- **Spring Web** — REST controllers for transaction ingestion and retrieval.
- **Spring Security** — JWT-based resource server for secure endpoints.
- **Spring Data JPA** — persistence layer for transaction entities.
- **PostgreSQL Driver** — relational database for storing transactions.
- **Flyway** — database schema migrations.
- **Validation** — ensure input correctness (amounts, dates, categories).
- **Spring Boot Actuator** — monitoring and health checks.

---

## 📂 Project Purpose
- Provide endpoints for **transaction ingestion** (e.g., CSV upload).
- Persist and manage **user financial transactions**.
- Support **categorization** of transactions.
- Validate transaction data (amount, date, category).
- Secure all APIs with **JWT authentication**.
- Maintain database schema with **Flyway migrations**.
- Expose **monitoring and health endpoints** via Actuator.

---
