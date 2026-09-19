import { useEffect, useState } from "react";
import { getProducts, removeStock } from "../services/api";

function RemoveStock() {

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

  async function handleRemoveStock(e) {
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

    const selectedProduct = products.find(
      (product) => product.id === Number(productId)
    );

    if (selectedProduct && Number(quantity) > selectedProduct.quantity) {
      setError(
        `Only ${selectedProduct.quantity} ${selectedProduct.unit} available.`
      );
      return;
    }

    try {
      const updatedProduct = await removeStock(
        productId,
        quantity
      );

      setMessage(
        `${updatedProduct.name} stock updated successfully. New quantity: ${updatedProduct.quantity} ${updatedProduct.unit}`
      );

      setQuantity("");

      await loadProducts();

    } catch (err) {
      setError("Could not remove stock.");
    }
  }

  return (
    <div className="page">

      <h1>➖ Remove Stock</h1>

      <p>Remove stock from an existing product.</p>

      <form className="stock-form" onSubmit={handleRemoveStock}>

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
          Quantity to Remove
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
          ➖ Remove Stock
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

export default RemoveStock;
