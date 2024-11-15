


-- Table for storing Kategori
CREATE TABLE IF NOT EXISTS Category (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL UNIQUE,
       symbol VARCHAR(50),
       description TEXT
);

-- Table for storing Plats
CREATE TABLE IF NOT EXISTS Place (
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

       FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE SET NULL
);