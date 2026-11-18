import { useState } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import Button from "./Button.jsx";

import "../styles/components/Navbar.css";
import logoImage from "../assets/images/logo.png";

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const [menuOpen, setMenuOpen] = useState(false);

    function handleNavigate(path) {
        navigate(path);
        setMenuOpen(false);
    }

    return (
        <header className="navbar">
            <Link
                to="/"
                className="navbar-brand"
                onClick={() => setMenuOpen(false)}
            >
                <img
                    src={logoImage}
                    alt="TechFix Logo"
                    className="brand-logo-img"
                />
            </Link>

            <button
                type="button"
                className="navbar-toggle"
                aria-label={menuOpen ? "Zamknij menu" : "Otwórz menu"}
                aria-expanded={menuOpen}
                aria-controls="navbar-menu"
                onClick={() => setMenuOpen(prev => !prev)}
            >
                <span />
                <span />
                <span />
            </button>

            <nav
                id="navbar-menu"
                className={`navbar-nav ${menuOpen ? "navbar-nav-open" : ""}`}
            >
                <Link
                    to="/"
                    className="nav-link"
                    onClick={() => setMenuOpen(false)}
                >
                    Strona główna
                </Link>

                <Link
                    to="/services"
                    className="nav-link"
                    onClick={() => setMenuOpen(false)}
                >
                    Usługi
                </Link>

                <Link
                    to="/contact"
                    className="nav-link"
                    onClick={() => setMenuOpen(false)}
                >
                    Kontakt
                </Link>

                <div className="navbar-actions">
                    <Button
                        text="Zaloguj się"
                        variant="outline"
                        onClick={() => handleNavigate("/login")}
                    />

                    <Button
                        text="Zarejestruj się"
                        variant="primary"
                        onClick={() => handleNavigate("/register")}
                    />
                </div>
            </nav>
        </header>
    );
}
