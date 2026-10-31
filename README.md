# Transaction Ledger API

## 1. Overview

The Transaction Ledger API is a RESTful backend application built using Spring Boot. It manages bank accounts, records credit and debit transactions, retrieves transaction history, and supports transfers between accounts.

The application uses MySQL for persistent storage, Spring Data JPA for database operations, and Flyway for database schema migrations.

## 2. Technologies Used

- **Java:** 21
- **Spring Boot:** 4.1.2
- **Build Tool:** Maven
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Database Migrations:** Flyway
- **Validation:** Jakarta Bean Validation
- **API Style:** REST

## 3. Prerequisites

Install the following software before running the application:

- Java Development Kit (JDK) 21
- MySQL Server
- Maven, or use the Maven Wrapper included in the project
- Git, if cloning the repository

Verify your Java installation:

```bash
java -version
```

Verify your Maven installation, if Maven is installed separately:

```bash
mvn -version
```

## 4. Clone the Repository

Clone the project repository and navigate to the project directory.

```bash
git clone <repository-url>
cd transaction-ledger
```

Replace `<repository-url>` with the actual repository URL.

## 5. Database Setup

### Step 1: Start MySQL

Ensure that your local MySQL server is running.

### Step 2: Create the Database

Log in to MySQL using your local MySQL credentials and execute:

```sql
CREATE DATABASE transactiondb;
```

Use the database name configured in `src/main/resources/application.yml`. If your configuration uses a different name, create that database instead.

### Step 3: Configure Database Credentials

Open the following file:

`src/main/resources/application.yml`

Configure the datasource with your local MySQL username, password, and database name.

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/transactiondb
    username: your_mysql_username
    password: your_mysql_password
```

Replace the example credentials with your own local MySQL credentials.

Keep the existing JPA and Flyway configuration in the file.

**Note:** Do not commit real database passwords or other sensitive credentials to a public repository.

## 6. Run the Application

Make sure MySQL is running and the database configuration is correct.

### Option 1: Using Maven

```bash
mvn spring-boot:run
```

### Option 2: Using Maven Wrapper

On Windows:

```bat
mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

Flyway applies the configured database migrations when the application starts, provided Flyway is configured and the database connection succeeds.

Once the application starts successfully, the API is available at:

`http://localhost:8080`

## 7. API Endpoints

The application exposes the following six endpoints.

| #   | Method | Endpoint                             | Description                            |
| --- | ------ | ------------------------------------ | -------------------------------------- |
| 1   | POST   | `/api/v1/accounts`                   | Create an account                      |
| 2   | GET    | `/api/v1/accounts/{id}`              | Retrieve account details               |
| 3   | GET    | `/api/v1/accounts/{id}/balance`      | Retrieve the current account balance   |
| 4   | POST   | `/api/v1/accounts/{id}/transactions` | Create a credit or debit transaction   |
| 5   | GET    | `/api/v1/accounts/{id}/transactions` | Retrieve paginated transaction history |
| 6   | POST   | `/api/v1/transfer`                   | Transfer money between two accounts    |

For complete request examples, response formats, query parameters, and error responses, refer to [API Contracts](docs/API_CONTRACTS.md).

## 8. API Documentation (Swagger UI)

The application integrates Swagger UI using Springdoc OpenAPI to provide interactive API documentation.

Swagger UI allows users to explore the available API endpoints, view request and response structures, and execute API requests directly from the browser.

Once the application is running, access Swagger UI using:

**Swagger UI:** http://localhost:8080/swagger-ui.html

**OpenAPI Specification:** http://localhost:8080/v3/api-docs



## 9. Key Features

### Account Management

- Create accounts with automatically generated account IDs.
- Retrieve account details.
- Retrieve the current balance of an account.
- Initialize new accounts with a zero balance.

### Transaction Management

- Create credit and debit transactions.
- Validate transaction amounts.
- Prevent debit transactions when the available balance is insufficient.
- Record the balance after each transaction.
- Prevent duplicate transaction references according to the implemented reference validation rules.

### Transaction History

- Retrieve transaction history for a specific account.
- Filter transactions by type: `CREDIT` or `DEBIT`.
- Support zero-based pagination.
- Configure page size, with a maximum of 100 transactions per page.

### Money Transfers

- Transfer funds between two existing accounts.
- Create a debit entry for the sender and a credit entry for the receiver.
- Update both account balances within a single database transaction.
- Lock accounts in ascending ID order to reduce the risk of deadlocks.
- Roll back the transfer if an operation fails within the transaction.

## 10. Database Migrations

Flyway manages database schema changes using migration scripts stored in:

`src/main/resources/db/migration/`

Ensure that the configured MySQL user has sufficient permissions to create or modify the required tables.

## 11. Error Handling

The application provides structured error responses for situations such as:

- Invalid request data
- Account not found
- Insufficient funds
- Duplicate transaction references
- Invalid pagination parameters

HTTP status codes depend on the application's configured exception handling. Refer to the API Contracts document for the status codes implemented by the application.

## 12. Assumptions

The application follows defined business rules for account creation, transaction processing, reference validation, pagination, and money transfers.

See [Assumptions](docs/ASSUMPTIONS.md) for the detailed business rules and implementation assumptions.

## 13. Stopping the Application

Stop the Spring Boot application by pressing `Ctrl+C` in the terminal where it is running.

Stop MySQL separately if you no longer need it.

## 14. Submission Contents

The project submission contains:

1. **Source Code** — Complete Spring Boot application.
2. **README** — Project overview, prerequisites, and local setup instructions.
3. **API Contracts** — Endpoint definitions, request and response examples, and error handling.
4. **Assumptions** — Business rules and implementation assumptions.
