import { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import Button from "./Button.jsx";


import "../styles/components/Navbar.css";
import logoImage from "../assets/images/logo.png";

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();


    function scrollToSection(sectionId) {
        setMenuOpen(false);

        if (location.pathname !== "/") {
            navigate(`/#${sectionId}`);
        } else {
            document.getElementById(sectionId)?.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });
        }
    }

    const [menuOpen, setMenuOpen] = useState(false);

    function handleNavigate(path) {
        navigate(path);
        setMenuOpen(false);
    }

    useEffect(() => {
        if (location.pathname !== "/" || !location.hash) return;

        const sectionId = location.hash.slice(1);

        document.getElementById(sectionId)?.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    }, [location.pathname, location.hash]);

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
                    to="/#home"
                    onClick={(event) => {
                        if (location.pathname === "/") {
                            event.preventDefault();
                            scrollToSection("home");
                        } else {
                            setMenuOpen(false);
                        }
                    }}
                    className="nav-link"
                >
                    Strona główna
                </Link>


                <Link
                    to="/#services"
                    onClick={(event) => {
                        if (location.pathname === "/") {
                            event.preventDefault();
                            scrollToSection("services");
                        } else {
                            setMenuOpen(false);
                        }
                    }}
                    className="nav-link"
                >
                    Usługi
                </Link>

                <Link
                    to="/#contact"
                    onClick={(event) => {
                        if (location.pathname === "/") {
                            event.preventDefault();
                            scrollToSection("contact");
                        } else {
                            setMenuOpen(false);
                        }
                    }}
                    className="nav-link"
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
