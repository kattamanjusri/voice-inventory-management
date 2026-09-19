import { useState } from "react";
import "./VoiceInventory.css";

function VoiceInventory() {
  const [listening, setListening] = useState(false);
  const [text, setText] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const startListening = () => {
    setText("");
    setResult(null);
    setError("");

    const SpeechRecognition =
      window.SpeechRecognition ||
      window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      setError("Speech recognition is not supported in this browser.");
      return;
    }

    const recognition = new SpeechRecognition();

    recognition.lang = "en-IN";
    recognition.continuous = false;
    recognition.interimResults = false;

    recognition.onstart = () => {
      setListening(true);
    };

    recognition.onresult = async (event) => {
      const recognizedText =
        event.results[0][0].transcript;

      setText(recognizedText);

      try {
        const response = await fetch(
          "http://localhost:8080/api/voice/process",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              text: recognizedText
            })
          }
        );

        const data = await response.json();

        if (!response.ok) {
          throw new Error(
            data.message || "Could not process command."
          );
        }

        setResult(data);

      } catch (err) {
        setError(err.message);
      }
    };

    recognition.onerror = (event) => {
      setError("Voice recognition error: " + event.error);
      setListening(false);
    };

    recognition.onend = () => {
      setListening(false);
    };

    recognition.start();
  };

  return (
    <div className="voice-page">

      <div className="voice-card">

        <h1>🎤 Voice Inventory</h1>

        <p className="subtitle">
          Control your inventory using voice commands
        </p>

        <div className="examples">
          <p>Try saying:</p>

          <span>"Add 1 bag rice"</span>
          <span>"Remove 1 kg sugar"</span>
          <span>"Add 2 boxes biscuits"</span>
        </div>

        <button
          className={`voice-button ${
            listening ? "listening" : ""
          }`}
          onClick={startListening}
          disabled={listening}
        >
          {listening
            ? "🎙️ Listening..."
            : "🎤 Start Voice Command"}
        </button>

        {text && (
          <div className="recognized-box">
            <h3>Recognized Command</h3>
            <p>“{text}”</p>
          </div>
        )}

        {result && (
          <div className="result-box">

            <div className="result-action">
              {result.action === "ADD" ? "➕" : "➖"}
              {" "}
              {result.action}
            </div>

            <div className="result-details">

              <div>
                <small>Product</small>
                <strong>{result.productName}</strong>
              </div>

              <div>
                <small>Quantity</small>
                <strong>
                  {result.quantity} {result.unit}
                </strong>
              </div>

            </div>

            <div className="success">
              ✅ {result.message}
            </div>

          </div>
        )}

        {error && (
          <div className="error">
            ❌ {error}
          </div>
        )}

      </div>

    </div>
  );
}

export default VoiceInventory;
