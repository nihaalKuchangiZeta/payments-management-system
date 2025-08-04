
-- 1. Users Table
CREATE TABLE Users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL, -- store hashed passwords
    role VARCHAR(20) CHECK (role IN ('Admin', 'FinancialManager', 'Viewer')) NOT NULL
);

-- 2. Employee Table
CREATE TABLE Employee (
    eid VARCHAR(10) PRIMARY KEY, -- Format: Exxxx
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    salary BIGINT NOT NULL
);

-- 3. Vendor Table
CREATE TABLE Vendor (
    vendor_id VARCHAR(10) PRIMARY KEY, -- Format: Vxxxx
    vendor_name VARCHAR(100) NOT NULL,
    status VARCHAR(10) CHECK (status IN ('Active', 'Inactive')) NOT NULL
);

-- 4. Client Table
CREATE TABLE Client (
    client_id VARCHAR(10) PRIMARY KEY, -- Format: Cxxxx
    client_name VARCHAR(100) NOT NULL,
    status VARCHAR(10) CHECK (status IN ('Active', 'Inactive')) NOT NULL
);

-- 5. Payments Table
CREATE TABLE Payments (
    pid VARCHAR(20) PRIMARY KEY, -- Format: PAYMMYYTCXXXX
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type VARCHAR(10) CHECK (type IN ('Incoming', 'Outgoing')) NOT NULL,
    category VARCHAR(20) CHECK (category IN ('Salary', 'Vendor Payment', 'Client Invoice')) NOT NULL,
    counter_party_id VARCHAR(10) NOT NULL,
    amount BIGINT NOT NULL,
    payment_status VARCHAR(15) CHECK (payment_status IN ('Pending', 'Processing', 'Completed')) NOT NULL
);

-- 6. AuditTrails Table
CREATE TABLE AuditTrails (
    id SERIAL PRIMARY KEY,
    jsondb JSONB NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);