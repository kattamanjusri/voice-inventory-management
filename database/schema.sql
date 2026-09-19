USE voice_inventory;

-- ---------- PRODUCTS ----------
CREATE TABLE IF NOT EXISTS products (
  id             BIGINT        NOT NULL AUTO_INCREMENT,
  name           VARCHAR(100)  NOT NULL,
  unit           VARCHAR(20)   NOT NULL,
  quantity       DECIMAL(12,2) NOT NULL DEFAULT 0,
  minimum_stock  DECIMAL(12,2) NOT NULL DEFAULT 0,
  price          DECIMAL(12,2) NULL,
  created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  -- No two products may share a name. The collation is case-insensitive,
  -- so "Rice" and "rice" count as duplicates.
  UNIQUE KEY uq_products_name (name),
  CONSTRAINT chk_products_quantity CHECK (quantity >= 0),
  CONSTRAINT chk_products_minimum  CHECK (minimum_stock >= 0)
) ENGINE=InnoDB;

-- ---------- STOCK TRANSACTIONS (history, append-only) ----------
CREATE TABLE IF NOT EXISTS stock_transactions (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  product_id       BIGINT        NULL,          -- becomes NULL if the product is deleted
  product_name     VARCHAR(100)  NOT NULL,      -- copy of the name, so history stays readable
  action           VARCHAR(10)   NOT NULL,      -- 'ADD' or 'REMOVE'
  quantity         DECIMAL(12,2) NOT NULL,
  unit             VARCHAR(20)   NOT NULL,
  price            DECIMAL(12,2) NULL,
  quantity_before  DECIMAL(12,2) NOT NULL,
  quantity_after   DECIMAL(12,2) NOT NULL,
  source           VARCHAR(10)   NOT NULL DEFAULT 'MANUAL',  -- 'MANUAL' or 'VOICE'
  created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_transactions_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL,
  CONSTRAINT chk_transactions_quantity CHECK (quantity > 0),
  CONSTRAINT chk_transactions_action CHECK (action IN ('ADD', 'REMOVE')),
  -- Indexes speed up the history page and per-product lookups.
  INDEX idx_transactions_product (product_id),
  INDEX idx_transactions_created (created_at)
) ENGINE=InnoDB;