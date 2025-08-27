# Transaction & Import Service

## 1. Overview

The **Transaction & Import Service** is a core component of the **Smart Expense Tracker** backend.  
Its primary responsibility is to manage the ingestion and storage of user transaction data.  
It acts as the foundational data provider for other services within the system that require raw financial transaction information.

- **Technology Stack:** Spring Boot
- **Database:** PostgreSQL

---

## 2. Core Responsibilities

- **Data Ingestion**: Provides endpoints for creating single transactions and for bulk-importing transaction history via a CSV file.
- **Data Storage**: Manages the `transactions` table in PostgreSQL, which is the single source of truth for all user transactions.
- **Internal Data Provisioning**: Exposes a secure, internal-only API endpoint for other microservices to fetch raw transaction data.

---

## 3. CSV Import Guide

This section details the process for populating the database with user-specific transactions using the CSV import endpoint.

### File Format

The service requires a CSV file with the following header and column order:


Header: Date,Merchant,Amount,Currency,Category

#### Data Types

- **Date**: ISO-8601 timestamp string (e.g., `2025-09-05T10:15:00Z`)
- **Amount**: A number representing the expense (negative) or income (positive)
- **Merchant, Currency, Category**: Strings

#### Example: `my_transactions.csv`

```csv
Date,Merchant,Amount,Currency,Category
2025-09-05T10:15:00Z,Grocery Mart,-75.50,EUR,Groceries
2025-09-08T08:30:00Z,Metro Ticket,-2.90,EUR,Transport
2025-09-12T19:00:00Z,Netflix,-12.99,EUR,Entertainment

### API Request Workflow
**Authentication Note:** All requests to public endpoints must include a valid JWT from the Auth Service in the  
`Authorization: Bearer <token>` header. The `userId` is extracted directly from this token.

1. **Obtain a JWT**: Authenticate with the Auth Service to get a token for a specific user.  
2. **Upload the File**: Construct a `multipart/form-data` POST request to the `/transactions/import/csv` endpoint, including the JWT.  

#### Example cURL Command
```bash
# Store your token in a variable
TOKEN="<paste_your_copied_jwt_token_here>"

# Make the authenticated upload request
# (Run this command from the same directory as your CSV file)
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@my_transactions.csv" \
  http://localhost:8083/transactions/import/csv


## 4. API Endpoints

### Public API
These endpoints are intended for use by authenticated clients via the API Gateway.

| Method | Endpoint                   | Description                                           | Authentication |
|--------|-----------------------------|-------------------------------------------------------|----------------|
| POST   | `/transactions/import/csv` | Accepts a `multipart/form-data` request with a CSV file | JWT Required   |
| POST   | `/transactions`            | Accepts a JSON object to create a single transaction   | JWT Required   |

---

### Internal API
These endpoints are intended for secure, internal microservice communication only.

| Method | Endpoint                                 | Description                                             | Authentication |
|--------|-------------------------------------------|---------------------------------------------------------|----------------|
| GET    | `/internal/transactions-by-user/{userId}` | Provides a list of transactions for a specified user/month | JWT Required   |

---

## 5. Communication

- **Inbound:** Receives requests from the API Gateway.  
- **Outbound:** This service does not initiate communication with other services.  
- **Database:** Connects to **PostgreSQL** to read and write transaction data.  
