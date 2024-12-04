


-- Table for storing Kategori
CREATE TABLE IF NOT EXISTS category (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL UNIQUE,
       symbol VARCHAR(50),
       description TEXT
);

-- Table for storing Plats
CREATE TABLE IF NOT EXISTS place (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT,
    app_user_id BIGINT NOT NULL,
    public_status BOOLEAN DEFAULT TRUE,
    last_change DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    description TEXT,
    playground_id BIGINT,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    FOREIGN KEY (playground_id) REFERENCES playground(id) ON DELETE SET NULL,
    FOREIGN KEY (app_user_id) REFERENCES app_user(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS playground (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coordinate GEOMETRY NOT NULL SRID 4326
);

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);