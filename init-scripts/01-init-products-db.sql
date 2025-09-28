-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create product status enum type
CREATE TYPE product_status AS ENUM ('ACTIVE', 'INACTIVE', 'DISCONTINUED');

-- Create product category enum type (we'll also have a categories table)
CREATE TYPE product_category_enum AS ENUM ('BREAD', 'CAKES', 'PASTRIES', 'COOKIES', 'BEVERAGES', 'SPECIALTY');

-- Create inventory status enum type
CREATE TYPE inventory_status AS ENUM ('IN_STOCK', 'LOW_STOCK', 'OUT_OF_STOCK');

-- Ensure proper permissions
GRANT ALL PRIVILEGES ON DATABASE bakery_products TO product_user;

-- Insert initial categories
INSERT INTO categories (id, name, description, display_order, active) VALUES
                                                                          (gen_random_uuid(), 'Bread', 'Fresh baked breads and loaves', 1, true),
                                                                          (gen_random_uuid(), 'Cakes', 'Delicious cakes for all occasions', 2, true),
                                                                          (gen_random_uuid(), 'Pastries', 'Flaky and sweet pastries', 3, true),
                                                                          (gen_random_uuid(), 'Cookies', 'Homemade cookies and biscuits', 4, true),
                                                                          (gen_random_uuid(), 'Beverages', 'Coffee, tea, and other drinks', 5, true),
                                                                          (gen_random_uuid(), 'Specialty', 'Seasonal and custom items', 6, true)
    ON CONFLICT DO NOTHING;
