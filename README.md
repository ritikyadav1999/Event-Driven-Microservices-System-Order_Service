# Order Service 🚀

A production-style Order microservice built using:

- Spring Boot
- JPA (Hibernate)
- Redis Streams
- Outbox Pattern
- Idempotency Handling
- At-Least-Once Event Publishing

---

## 📌 Overview

This service:

1. Creates Orders
2. Enforces API-level idempotency
3. Uses Outbox Pattern for reliable event publishing
4. Publishes events to Redis Streams (`order-events`)
5. Guarantees no event loss

---

## 🧠 Architecture

Client → Order API  
↓  
OrderService (Transactional)  
↓  
Order Table  
Idempotency Table  
Outbox Table  
↓  
Outbox Publisher (Scheduler)  
↓  
Redis Stream (`order-events`)

---

## 🔥 Key Features

### ✅ API Idempotency
- Idempotency key required in request header
- Prevents duplicate order creation
- Enforced using DB-level unique constraint

### ✅ Outbox Pattern
- Order + Outbox event stored in same transaction
- Background scheduler publishes events to Redis
- Guarantees no message loss

### ✅ At-Least-Once Delivery
- Events may be published more than once
- Downstream services must be idempotent
---

## 🗄 Database Tables

### `orders`
- order_id (UUID)
- user_id
- amount
- status

### `idempotency_records`
- id
- user_id
- idempotency_key (UNIQUE per user)
- request_hash
- order_id

### `outbox`
- id
- event_id (UNIQUE)
- aggregate_type
- aggregate_id
- event_type
- payload (JSON)
- created_at
- published_at (nullable)

---

## 📨 Event Structure

Stream: `order-events`

Each entry contains:

- eventId
- eventType
- aggregateType
- aggregateId
- payload

---

## ▶️ How To Run

### 1️⃣ Start Redis

```bash
docker run -d -p 6379:6379 redis:7

mvn spring-boot:run

POST /api/order/create
Headers:
Idempotency-Key: unique-key-123

```

