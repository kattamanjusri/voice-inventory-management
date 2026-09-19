import { useState } from "react";

function VoiceEntry() {

  const [text, setText] = useState("");
  const [message, setMessage] = useState("");
  const [language, setLanguage] = useState("en-IN");
  const [listening, setListening] = useState(false);

  const startListening = () => {

    const SpeechRecognition =
      window.SpeechRecognition ||
      window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      setMessage("Voice recognition is not supported in this browser.");
      return;
    }

    const recognition = new SpeechRecognition();

    recognition.lang = language;
    recognition.continuous = false;
    recognition.interimResults = false;

    recognition.onstart = () => {
      setListening(true);
      setMessage("");
    };

    recognition.onresult = async (event) => {

      const spokenText =
        event.results[0][0].transcript;

      setText(spokenText);

      try {

        const response = await fetch(
          "http://localhost:8080/api/voice/process",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              text: spokenText
            })
          }
        );

        const data = await response.json();

        if (!response.ok) {
          throw new Error(data.message || "Voice command failed");
        }

        setMessage(
          data.message || "Stock updated successfully."
        );

      } catch (error) {

        setMessage(
          error.message || "Could not process the voice command."
        );
      }
    };

    recognition.onerror = (event) => {

      setListening(false);

      setMessage(
        "Voice recognition error: " + event.error
      );
    };

    recognition.onend = () => {
      setListening(false);
    };

    recognition.start();
  };

  return (
    <div className="voice-page">

      <div className="voice-container">

        <h1>🎤 Voice Stock Entry</h1>

        <p>
          Speak your stock command
        </p>

        <h2>🌐 Select Language</h2>

        <select
          value={language}
          onChange={(e) => setLanguage(e.target.value)}
          className="language-select"
        >
          <option value="en-IN">
            English
          </option>

          <option value="te-IN">
            తెలుగు - Telugu
          </option>

          <option value="hi-IN">
            हिन्दी - Hindi
          </option>
        </select>

        <h2>Try saying:</h2>

        <div className="example">
          "Add 10 bags rice"
        </div>

        <div className="example">
          "Remove 5 kg sugar"
        </div>

        <div className="example">
          "Add one bag rice"
        </div>

        <button
          onClick={startListening}
          disabled={listening}
          className="voice-button"
        >
          {listening
            ? "🎙️ Listening..."
            : "🎤 Start Speaking"}
        </button>

        <div className="result-box">

          <h3>Recognized Command</h3>

          <p>
            {text ||
              "Your spoken command will appear here..."}
          </p>

        </div>

        {message && (
          <div className="message">
            {message}
          </div>
        )}

      </div>

    </div>
  );
}

export default VoiceEntry;
