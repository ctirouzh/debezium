# 🧬 Debezium CDC with Spring Boot, Kafka, MySQL & Elasticsearch

This project demonstrates a practical **Change Data Capture (CDC)** pipeline using **Debezium**, **Kafka**, **MySQL**, and **Spring Boot**. It captures changes in a `Product` domain model (with `Price` and `Media`) and syncs them to **Elasticsearch**.

---

## 📦 Technologies Used

- **Spring Boot**
- **MySQL**
- **Apache Kafka**
- **Debezium (MySQL connector)**
- **Elasticsearch**
- **Docker Compose**

---

## 📁 Entities Overview

- **Product**
    - Has many `Price`
    - Has many `Media` (via generic `entity/entity_id`)
- **Price**
    - Linked to `Product` via `product_id`
- **Media**
    - Generic table with `entity` and `entity_id`

All changes to `products`, `prices`, and `media` are captured via Debezium and processed into a flat **Elasticsearch document**.

---

## ⚙️ Setup Instructions

### 1. Clone the repository

```bash
git clone git@github.com:ctirouzh/debezium.git
cd debezium
```
### 2. Configure environment variables
Copy the .env.example to .env:

```bash
cp .env.example .env
```
You can customize ports, credentials, and service names in this file.

### 3. Start infrastructure (MySQL, Kafka, Zookeeper, Debezium, Elasticsearch)
Make sure Docker is running, then:

```bash
docker-compose up -d
```
This will spin up MySQL, Zookeeper, Kafka, Kafka Connect (with Debezium connector), and
Elasticsearch. 

### 🐛 MySQL Privilege Requirements (Binlog Access)
In order for Debezium to read the MySQL binary log, the MySQL user needs the following privileges:

```sql
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'your_user'@'%';
FLUSH PRIVILEGES;
```

Make sure my.cnf (or Docker MySQL config) includes:

```ini
[mysqld]
server-id         = 1
log_bin           = mysql-bin
binlog_format     = ROW
binlog_row_image  = FULL
expire_logs_days  = 10
```
Your MySQL Docker setup should already handle this, but if using your own MySQL, make sure it's configured.

### 🚀 Run the Spring Boot Application
Make sure the infrastructure is up, then:

```bash
./gradlew bootRun
```
Or using IntelliJ / any IDE with Spring Boot support.

🔍 Query Elasticsearch
Once data flows through Debezium and the Spring consumer processes it, you can verify documents:

```bash
curl http://localhost:9200/products/_search
```

### 📌 Features
- Live sync from MySQL to Elasticsearch

- Full document denormalization (Product + Prices + Media)

- Robust error handling in Kafka consumer

- Works with Debezium MySQL CDC connector

- Clean architecture with Kotlin + Spring Data JPA

### 🧪 Sample Debezium Event (from Kafka)
```json
{
  "before": null,
  "after": {
    "id": 3,
    "amount": 9.0,
    "unit": "$",
    "product_id": 8
  },
  "source": {
    "connector": "mysql",
    "table": "prices"
  },
  "op": "c"
}
```

### 📚 TODO (optional ideas)
-[ ] Integrate with Kibana for search
-[ ] Implement some search APIs

### 💬 License
This project is licensed under the Apache-2.0 license.