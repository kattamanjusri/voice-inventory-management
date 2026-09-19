import "./App.css";

import VoiceEntry from "./pages/VoiceEntry";
import VoiceInventory from "./VoiceInventory";

import Inventory from "./pages/Inventory";
import AddStock from "./pages/AddStock";
import RemoveStock from "./pages/RemoveStock";
import StockHistory from "./pages/StockHistory";
import StockAssistant from "./pages/StockAssistant";
import Login from "./pages/Login";
import Signup from "./pages/Signup";

import {
  BrowserRouter,
  Routes,
  Route,
  Link,
  Navigate,
  useNavigate
} from "react-router-dom";


function ProtectedRoute({ children }) {

  const isLoggedIn =
    localStorage.getItem("isLoggedIn") === "true";

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  return children;
}


function Dashboard() {

  const navigate = useNavigate();

  const username =
    localStorage.getItem("username") || "User";

  const handleLogout = () => {

    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("username");

    navigate("/login");
  };


  return (

    <div className="dashboard">

      <header className="header">

        <div className="header-top">

          <div>
            <h1>📦 Voice Inventory Management</h1>

            <p>
              Simple inventory management for small businesses
            </p>
          </div>

          <div className="user-section">

            <span>
              👤 {username}
            </span>

            <button
              className="logout-button"
              onClick={handleLogout}
            >
              Logout
            </button>

          </div>

        </div>

      </header>


      <main>

        <div className="cards">

          <div className="card">
            <h3>📦 Total Products</h3>
            <p className="number">7</p>
          </div>

          <div className="card">
            <h3>⚠️ Low Stock</h3>
            <p className="number">2</p>
          </div>

          <div className="card">
            <h3>📈 Stock Updates</h3>
            <p className="number">9</p>
          </div>

        </div>


        <div className="actions">

          <Link to="/inventory">
            <button>📦 Inventory</button>
          </Link>

          <Link to="/add-stock">
            <button>➕ Add Stock</button>
          </Link>

          <Link to="/remove-stock">
            <button>➖ Remove Stock</button>
          </Link>

          <Link to="/voice-entry">
            <button>🎤 Voice Entry</button>
          </Link>

          <Link to="/stock-history">
            <button>📋 Stock History</button>
          </Link>

          <Link to="/stock-assistant">
            <button>🤖 Stock Assistant</button>
          </Link>

        </div>

      </main>

    </div>
  );
}


function ProtectedPage({ children }) {

  return (
    <ProtectedRoute>
      {children}
    </ProtectedRoute>
  );
}


function App() {

  return (

    <BrowserRouter>

      <Routes>

        {/* LOGIN */}

        <Route
          path="/signup"
          element={<Signup />}
        />

        <Route
          path="/login"
          element={<Login />}
        />


        {/* DASHBOARD */}

        <Route
          path="/"
          element={
            <ProtectedPage>
              <Dashboard />
            </ProtectedPage>
          }
        />


        {/* INVENTORY */}

        <Route
          path="/inventory"
          element={
            <ProtectedPage>
              <Inventory />
            </ProtectedPage>
          }
        />


        {/* ADD STOCK */}

        <Route
          path="/add-stock"
          element={
            <ProtectedPage>
              <AddStock />
            </ProtectedPage>
          }
        />


        {/* REMOVE STOCK */}

        <Route
          path="/remove-stock"
          element={
            <ProtectedPage>
              <RemoveStock />
            </ProtectedPage>
          }
        />


        {/* VOICE ENTRY */}

        <Route
          path="/voice-entry"
          element={
            <ProtectedPage>
              <VoiceEntry />
            </ProtectedPage>
          }
        />


        {/* STOCK ASSISTANT */}

        <Route
          path="/stock-assistant"
          element={
            <ProtectedPage>
              <StockAssistant />
            </ProtectedPage>
          }
        />


        {/* STOCK HISTORY */}

        <Route
          path="/stock-history"
          element={
            <ProtectedPage>
              <StockHistory />
            </ProtectedPage>
          }
        />


        {/* OLD VOICE INVENTORY */}

        <Route
          path="/voice-inventory"
          element={
            <ProtectedPage>
              <VoiceInventory />
            </ProtectedPage>
          }
        />


        {/* UNKNOWN URL */}

        <Route
          path="*"
          element={<Navigate to="/" replace />}
        />

      </Routes>

    </BrowserRouter>

  );
}

export default App;
