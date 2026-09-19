import { useState } from "react";

function StockAssistant() {
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  const askQuestion = async (q = question) => {
    if (!q.trim()) return;

    setLoading(true);
    setAnswer("");
    setItems([]);

    try {
      const response = await fetch(
        `http://localhost:8080/api/inventory/query?question=${encodeURIComponent(q)}`
      );

      const data = await response.json();

      setAnswer(data.answer || "No answer found.");
      setItems(data.items || []);
    } catch (error) {
      setAnswer("Could not connect to the inventory server.");
    }

    setLoading(false);
  };

  return (
    <div className="assistant-page">
      <h1>🤖 Stock Assistant</h1>
      <p>Ask questions about your inventory.</p>

      <div className="assistant-input">
        <input
          type="text"
          placeholder="Ask about your stock..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              askQuestion();
            }
          }}
        />

        <button onClick={() => askQuestion()}>
          {loading ? "Thinking..." : "Ask"}
        </button>
      </div>

      <div className="quick-questions">
        <button onClick={() => askQuestion("How much rice is available?")}>
          Rice stock
        </button>

        <button onClick={() => askQuestion("Which items are low in stock?")}>
          Low stock
        </button>

        <button onClick={() => askQuestion("What should I reorder?")}>
          Reorder
        </button>

        <button onClick={() => askQuestion("How many products do I have?")}>
          Total products
        </button>
      </div>

      {answer && (
        <div className="assistant-answer">
          <h3>Answer</h3>
          <p>{answer}</p>

          {items.length > 0 && (
            <div>
              <h4>Items</h4>
              <ul>
                {items.map((item, index) => (
                  <li key={index}>{item}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default StockAssistant;
