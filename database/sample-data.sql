USE voice_inventory;

-- Sugar (4 < 10) and Eggs (2 < 5) are deliberately low, to demo alerts.
INSERT INTO products (id, name, unit, quantity, minimum_stock, price) VALUES
  (1, 'Rice',        'bags',     25, 10, 1200.00),
  (2, 'Sugar',       'kg',        4, 10,   45.00),
  (3, 'Cooking Oil', 'litres',   20,  8,  140.00),
  (4, 'Biscuits',    'boxes',    12,  5,  240.00),
  (5, 'Wheat Flour', 'quintals',  3,  2, 2800.00),
  (6, 'Eggs',        'dozens',    2,  5,   84.00),
  (7, 'Soap',        'pieces',   60, 20,   35.00);

-- History that matches the quantities above.
INSERT INTO stock_transactions
  (product_id, product_name, action, quantity, unit, price, quantity_before, quantity_after, source, created_at)
VALUES
  (1, 'Rice',        'ADD',    10, 'bags',     1200.00, 15,  25, 'VOICE',  NOW() - INTERVAL 3 HOUR),
  (2, 'Sugar',       'REMOVE',  6, 'kg',       NULL,    10,   4, 'MANUAL', NOW() - INTERVAL 2 HOUR),
  (3, 'Cooking Oil', 'ADD',    20, 'litres',   140.00,   0,  20, 'MANUAL', NOW() - INTERVAL 1 DAY),
  (4, 'Biscuits',    'ADD',    12, 'boxes',    240.00,   0,  12, 'MANUAL', NOW() - INTERVAL 1 DAY),
  (5, 'Wheat Flour', 'ADD',     3, 'quintals', 2800.00,  0,   3, 'MANUAL', NOW() - INTERVAL 2 DAY),
  (6, 'Eggs',        'REMOVE',  3, 'dozens',   NULL,     5,   2, 'MANUAL', NOW() - INTERVAL 1 HOUR),
  (7, 'Soap',        'ADD',    60, 'pieces',   35.00,    0,  60, 'MANUAL', NOW() - INTERVAL 2 DAY);