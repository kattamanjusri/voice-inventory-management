-- Creates the database and a dedicated application user.
-- Run once as root.

CREATE DATABASE IF NOT EXISTS voice_inventory
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- CHANGE this password if you like. If you do, use the same one in
-- backend/src/main/resources/application-local.properties (Step 5).
CREATE USER IF NOT EXISTS 'inventory_user'@'localhost' IDENTIFIED BY 'Inventory@123';

GRANT ALL PRIVILEGES ON voice_inventory.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
