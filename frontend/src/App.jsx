import { BrowserRouter, Routes, Route } from "react-router-dom";
import ScrollToTop from "./components/ScrollToTop.jsx"
import Navbar from "./components/Navbar.jsx";
import Footer from "./components/Footer.jsx";
import AppRoutes from "./routes/AppRoutes.jsx";

import './App.css'

function App() {
  return (
      <BrowserRouter>
          <ScrollToTop />
          <div className="app-container">
              <Navbar />
              <main className="app-main">
                <AppRoutes />
              </main>
              <Footer />
          </div>
      </BrowserRouter>
  );
}

export default App
