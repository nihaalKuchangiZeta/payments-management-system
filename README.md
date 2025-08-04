# 💸 Payments Management System

A command-line based fintech application designed for internal payment operations in a startup environment. The system handles **incoming client payments**, **outgoing vendor settlements**, and **salary disbursements**, while ensuring **data integrity**, **audit logging**, and **role-based access control**.

## 📁 Project Structure

```plaintext
Payments Management System/
├── pom.xml                    # Maven build file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/         # Domain models (User, Employee, Payment, etc.)
│   │   │   ├── dao/           # DAO classes for database operations
│   │   │   ├── service/       # Business logic and service layer
│   │   │   ├── utils/         # Common utilities (DBUtil, Validators, etc.)
│   │   │   ├── menu/          # CLI Menus (AdminMenu, ViewerMenu, etc.)
│   │   │   └── Main.java      # Entry point
│   │   └── resources/
│   │       ├── database_schema.sql # PostgreSQL schema
│   │       └── db.config      # DB connection config
│   └── test/
│       └── java/              # Unit tests (to be implemented)
└── target/                    # Compiled classes and build artifactsn


