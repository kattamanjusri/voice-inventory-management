import { useEffect, useState } from "react";
import { getProducts, addStock } from "../services/api";

function AddStock() {

  const [products, setProducts] = useState([]);
  const [productId, setProductId] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    try {
      const data = await getProducts();
      setProducts(data);
    } catch (err) {
      setError("Could not load products.");
    }
  }

  async function handleAddStock(e) {
    e.preventDefault();

    setMessage("");
    setError("");

    if (!productId) {
      setError("Please select a product.");
      return;
    }

    if (!quantity || Number(quantity) <= 0) {
      setError("Please enter a valid quantity.");
      return;
    }

    try {
      const updatedProduct = await addStock(
        productId,
        quantity
      );

      setMessage(
        `${updatedProduct.name} stock updated successfully. New quantity: ${updatedProduct.quantity} ${updatedProduct.unit}`
      );

      setQuantity("");

      await loadProducts();

    } catch (err) {
      setError("Could not add stock.");
    }
  }

  return (
    <div className="page">

      <h1>➕ Add Stock</h1>

      <p>Add stock to an existing product.</p>

      <form className="stock-form" onSubmit={handleAddStock}>

        <label>
          Select Product
        </label>

        <select
          value={productId}
          onChange={(e) => setProductId(e.target.value)}
        >
          <option value="">-- Select Product --</option>

          {products.map((product) => (
            <option
              key={product.id}
              value={product.id}
            >
              {product.name} ({product.quantity} {product.unit})
            </option>
          ))}

        </select>

        <label>
          Quantity to Add
        </label>

        <input
          type="number"
          min="0.01"
          step="0.01"
          value={quantity}
          onChange={(e) => setQuantity(e.target.value)}
          placeholder="Enter quantity"
        />

        <button type="submit">
          ➕ Add Stock
        </button>

      </form>

      {message && (
        <div className="success-message">
          ✅ {message}
        </div>
      )}

      {error && (
        <div className="error-message">
          ❌ {error}
        </div>
      )}

    </div>
  );
}

export default AddStock;
