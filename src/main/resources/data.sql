-- Insert initial data into the Kategori table
INSERT INTO category (name, symbol, description) VALUES
     ('Parks', '🌳', 'Parks and natural areas with greenery'),
     ('Restaurants', '🍴', 'Places to enjoy food and beverages'),
     ('Shopping', '🛍️', 'Malls and shopping stores'),
     ('Cultural Sites', '🏛️', 'Museums, galleries, and historical sites');

-- Insert initial data into the Plats table
INSERT INTO place (name, category_id, user_id, public_status, description, playground_id) VALUES
     ('Central Park', 1, 101, TRUE, 'A large public park located in the city center', 1),
     ('Sagrada Familia', 4, 102, TRUE, 'A world heritage site and an iconic church', 2),
     ('Shibuya Mall', 3, 103, TRUE, 'A popular mall for shopping and dining', 3),
     ('Cafe Delicious', 2, 104, TRUE, 'A well-known cafe with amazing desserts', 4),
     ('Ueno Zoo', 1, 101, FALSE, 'A zoo with various animal exhibits', 5),
     ('Louvre Museum', 4, 105, TRUE, 'The historic museum housing the Mona Lisa', 6),
     ('Italian Pizzeria', 2, 102, TRUE, 'An authentic Italian restaurant', 7),
     ('Outlet Mall', 3, 103, FALSE, 'A mall offering discounted brand items', 8);


-- Insert sample data into api_key table
INSERT INTO api_key (api_key, name, valid_until)
VALUES ('wEWM967DqqC9cBMGpxvr99GM', 'Service A', '2024-12-31 23:59:59'),
       ('xYz12345AbCdEfGhIjKlMnOp', 'Service B', '2025-06-30 23:59:59'),
       ('aBcDeFgHiJkLmNoPqRsTuVwX', 'Service A', '2024-12-31 23:59:59'),
       ('LmNoPqRsTuVwXyZaBcDeFgHi', 'Service C', '2025-01-15 23:59:59'),
       ('QrStUvWxYzAbCdEfGhIjKlMn', 'Service B', '2025-06-30 23:59:59');

ALTER TABLE place MODIFY COLUMN deleted BOOLEAN DEFAULT FALSE;

INSERT INTO playground (coordinate) VALUES
       (ST_GeomFromText('POINT(40.785091 -73.968285)', 4326)), -- Central Park
       (ST_GeomFromText('POINT(41.403629 2.174356)', 4326)),   -- Sagrada Familia
       (ST_GeomFromText('POINT(35.659466 139.700551)', 4326)), -- Shibuya Mall
       (ST_GeomFromText('POINT(34.052235 -118.243683)', 4326)),-- Cafe Delicious
       (ST_GeomFromText('POINT(35.716852 139.773063)', 4326)), -- Ueno Zoo
       (ST_GeomFromText('POINT(48.860611 2.337644)', 4326)),   -- Louvre Museum
       (ST_GeomFromText('POINT(41.902782 12.496366)', 4326)),  -- Italian Pizzeria
       (ST_GeomFromText('POINT(34.693738 135.502165)', 4326)); -- Outlet Mall

INSERT INTO AppUser (username, password, role) VALUES
       ('user101', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER101'),
       ('user102', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER102'),
       ('user103', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER103'),
       ('user104', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER104'),
       ('user105', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER105'),
       ('admin', '$2a$10$D9XxFgR5G.AMwe8LfRXFbO3uiCynZP0kH8MhHydbYfiO6TZ7IUIRe', 'USER101,USER102,USER103,USER104,USER105,ADMIN');
