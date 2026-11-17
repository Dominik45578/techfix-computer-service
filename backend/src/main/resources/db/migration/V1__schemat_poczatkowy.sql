-- V1: schemat początkowy (tabele z kroku 03/04 skryptu TI)

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    phone_number TEXT,
    date_of_birth TEXT,
    role TEXT NOT NULL DEFAULT 'CLIENT' CHECK(role IN ('CLIENT', 'ADMIN')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS device_categories(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    device_name TEXT NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS services(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    device_categorie_id INTEGER NOT NULL,
    service_name TEXT NOT NULL,
    service_kind_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    base_cost REAL NOT NULL,
    estimated_duration_minutes INTEGER NOT NULL,
    is_active INTEGER DEFAULT 1 check(is_active IN (0, 1)),
    FOREIGN KEY (device_categorie_id) REFERENCES device_categories(id) ON DELETE CASCADE,
    FOREIGN KEY (service_kind_id) REFERENCES service_kind(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS service_kind(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    kind_name TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS purchases(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    service_id INTEGER NOT NULL,
    total_value REAL NOT NULL,
    payment_status TEXT DEFAULT 'PENDING' CHECK(payment_status IN ('PENDING', 'PAID', 'REFUNDED', 'CANCELLED')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS reservations(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    purchase_id INTEGER NOT NULL UNIQUE,
    assigned_staff_id INTEGER,
    status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    planned_at DATETIME NOT NULL,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_staff_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_reservation_purchase ON reservations(purchase_id);
CREATE INDEX idx_reservation_users ON reservations(assigned_staff_id);
CREATE INDEX idx_purchases_users ON purchases(user_id);
CREATE INDEX idx_purchases_services ON purchases(service_id);
CREATE INDEX idx_services_device_categories ON services(device_categorie_id);
CREATE INDEX idx_services_service_kind ON services(service_kind_id);
