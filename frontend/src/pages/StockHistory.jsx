import { useEffect, useState } from "react";

function StockHistory() {

  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    loadTransactions();
  }, []);

  async function loadTransactions() {

    try {

      const response = await fetch(
        "http://localhost:8080/api/transactions"
      );

      if (!response.ok) {
        throw new Error("Failed to load transactions");
      }

      const data = await response.json();

      setTransactions(data);

    } catch (err) {
      setError("Could not load stock history.");
    }
  }

  return (
    <div className="page">

      <h1>📋 Stock History</h1>

      <p>
        View all stock additions and removals.
      </p>

      {error && (
        <div className="error-message">
          ❌ {error}
        </div>
      )}

      {!error && transactions.length === 0 && (
        <p>No stock transactions found.</p>
      )}

      {transactions.length > 0 && (
        <div className="history-container">

          <table className="history-table">

            <thead>
              <tr>
                <th>Product</th>
                <th>Action</th>
                <th>Quantity</th>
                <th>Unit</th>
                <th>Date</th>
              </tr>
            </thead>

            <tbody>

              {transactions.map((transaction) => (

                <tr key={transaction.id}>

                  <td>
                    Product #{transaction.product?.id}
                  </td>

                  <td>
                    <span
                      className={
                        transaction.action === "ADD"
                          ? "add-action"
                          : "remove-action"
                      }
                    >
                      {transaction.action === "ADD"
                        ? "➕ ADD"
                        : "➖ REMOVE"}
                    </span>
                  </td>

                  <td>
                    {transaction.quantity}
                  </td>

                  <td>
                    {transaction.unit}
                  </td>

                  <td>
                    {new Date(
                      transaction.createdAt
                    ).toLocaleString()}
                  </td>

                </tr>

              ))}

            </tbody>

          </table>

        </div>
      )}

    </div>
  );
}

export default StockHistory;
