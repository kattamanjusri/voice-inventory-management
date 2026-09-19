import { useEffect, useState } from "react";
import { getProducts } from "../services/api";

function Inventory() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    try {
      const data = await getProducts();
      setProducts(data);
    } catch (error) {
      setError("Could not load inventory.");
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <div className="page"><h2>Loading inventory...</h2></div>;
  }

  if (error) {
    return <div className="page"><h2>{error}</h2></div>;
  }

  return (
    <div className="page">
      <h2>📦 Inventory</h2>

      <p>Manage all your products and stock.</p>

      <div className="inventory-grid">
        {products.map((product) => {
          const lowStock =
            Number(product.quantity) < Number(product.minimumStock);

          return (
            <div className="product-card" key={product.id}>

              <h3>{product.name}</h3>

              <p>
                Quantity:
                <strong> {product.quantity}</strong> {product.unit}
              </p>

              <p>
                Minimum Stock:
                <strong> {product.minimumStock}</strong> {product.unit}
              </p>

              <p>
                Price: ₹{product.price}
              </p>

              <span className={lowStock ? "low" : "available"}>
                {lowStock ? "⚠️ Low Stock" : "✅ In Stock"}
              </span>

            </div>
          );
        })}
      </div>
    </div>
  );
}

export default Inventory;
