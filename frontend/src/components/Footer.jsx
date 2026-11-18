import { Mail, Phone, Clock } from "lucide-react";
import { Link } from "react-router-dom";

import "../styles/components/Footer.css";

const contact = {
    email: "kontakt@techfix.pl",
    phone: "+48 000 000 000",
    workingHours: "Pon.–Pt.: 9:00–17:00"
};

export default function Footer() {
    return (
        <footer id="contact" className="footer">
            <div className="footer-content">
                <div className="footer-brand">
                    <Link to="/" className="footer-logo">
                        TechFix
                    </Link>
                    <p>
                        Profesjonalna naprawa i serwis
                        sprzętu komputerowego.
                    </p>
                </div>

                <div className="footer-column">
                    <h3>Przydatne linki</h3>

                    <Link to="/">Strona główna</Link>
                    <Link to="/services">Nasze usługi</Link>
                    <Link to="/report">Zgłoś usterkę</Link>
                </div>

                <div className="footer-column">
                    <h3>Kontakt</h3>

                    <a href={`mailto:${contact.email}`} className="footer-contact-item">
                        <Mail size={18} />
                        <span>{contact.email}</span>
                    </a>

                    <a
                        href={`tel:${contact.phone.replace(/\s/g, "")}`}
                        className="footer-contact-item"
                    >
                        <Phone size={18} />
                        <span>{contact.phone}</span>
                    </a>

                    <div className="footer-contact-item">
                        <Clock size={18} />
                        <span>{contact.workingHours}</span>
                    </div>
                </div>
            </div>

            <div className="footer-bottom">
                <p>
                    © {new Date().getFullYear()} TechFix.
                    Wszelkie prawa zastrzeżone.
                </p>

                <div className="footer-legal">
                    <Link to="/privacy-policy">
                        Polityka prywatności
                    </Link>
                    <Link to="/terms">
                        Regulamin
                    </Link>
                </div>
            </div>
        </footer>
    );
}
