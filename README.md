# 💸 Payments Management System

A command-line based fintech application designed for internal payment operations in a startup environment. The system handles **incoming client payments**, **outgoing vendor settlements**, and **salary disbursements**, while ensuring **data integrity**, **audit logging**, and **role-based access control**.

## 📁 Project Structure

```plaintext
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── menu/                # CLI Menus (Presentation Layer)
│   │   │   ├── service/             # Business Logic (Service Layer)
│   │   │   ├── dao/                 # Database Access (Data Access Layer)
│   │   │   ├── model/               # Entity Classes
│   │   │   └── util/                # Configs, Common Utilities
│   └── resources/
│       └── application.properties   # DB Connection Properties
├── scripts/
│   └── schema.sql                   # PostgreSQL Schema
├── pom.xml                          # Maven Config
└── README.md                        # Project Documentation


