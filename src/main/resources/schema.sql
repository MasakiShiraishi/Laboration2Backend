


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
       user_id BIGINT NOT NULL,
       public_status BOOLEAN DEFAULT TRUE,
       last_change DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       description TEXT,
       latitude DECIMAL(10, 8) NOT NULL,
       longitude DECIMAL(11, 8) NOT NULL,
       created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
       deleted BOOLEAN DEFAULT FALSE,

       FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS api_key
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    api_key     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    valid_until DATETIME     NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );