const API_URL = "http://localhost:8080/api";

export async function getProducts() {
  const response = await fetch(`${API_URL}/products`);

  if (!response.ok) {
    throw new Error("Failed to load products");
  }

  return response.json();
}

export async function addStock(id, quantity) {
  const response = await fetch(`${API_URL}/products/${id}/add`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ quantity: Number(quantity) })
  });

  if (!response.ok) {
    throw new Error("Failed to add stock");
  }

  return response.json();
}

export async function removeStock(id, quantity) {
  const response = await fetch(`${API_URL}/products/${id}/remove`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ quantity: Number(quantity) })
  });

  if (!response.ok) {
    throw new Error("Failed to remove stock");
  }

  return response.json();
}
