# Wallet Application

## Features
- Credit Wallet
- Debit Wallet
- Get Wallet Balance
- PostgreSQL Integration
- Docker Support
- Liquibase Migration
- Unit Testing

## Tech Stack
- Java
- Spring Boot
- PostgreSQL
- Docker
- Liquibase
- JUnit
- Mockito

## Run Using Docker

```bash
docker-compose up --build

## API Endpoints

### Update Wallet Balance
POST /api/v1/wallet

Sample Request:
```json
{
  "walletId": "11111111-1111-1111-1111-111111111111",
  "operationType": "CREDIT",
  "amount": 500
}
```

### Get Wallet Balance
GET /api/v1/wallets/{walletId}

Example:
```text
GET /api/v1/wallets/11111111-1111-1111-1111-111111111111
```
