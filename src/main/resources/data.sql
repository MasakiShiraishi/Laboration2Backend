-- Insert initial data into the Kategori table
INSERT INTO category (name, symbol, description) VALUES
     ('Parks', '🌳', 'Parks and natural areas with greenery'),
     ('Restaurants', '🍴', 'Places to enjoy food and beverages'),
     ('Shopping', '🛍️', 'Malls and shopping stores'),
     ('Cultural Sites', '🏛️', 'Museums, galleries, and historical sites');

-- Insert initial data into the Plats table
INSERT INTO place (name, category_id, user_id, public_status, description, latitude, longitude) VALUES
     ('Central Park', 1, 101, TRUE, 'A large public park located in the city center', 40.785091, -73.968285),
     ('Sagrada Familia', 4, 102, TRUE, 'A world heritage site and an iconic church', 41.403629, 2.174356),
     ('Shibuya Mall', 3, 103, TRUE, 'A popular mall for shopping and dining', 35.659466, 139.700551),
     ('Cafe Delicious', 2, 104, TRUE, 'A well-known cafe with amazing desserts', 34.052235, -118.243683),
     ('Ueno Zoo', 1, 101, FALSE, 'A zoo with various animal exhibits', 35.716852, 139.773063),
     ('Louvre Museum', 4, 105, TRUE, 'The historic museum housing the Mona Lisa', 48.860611, 2.337644),
     ('Italian Pizzeria', 2, 102, TRUE, 'An authentic Italian restaurant', 41.902782, 12.496366),
     ('Outlet Mall', 3, 103, FALSE, 'A mall offering discounted brand items', 34.693738, 135.502165);
