import { Link } from "react-router-dom";
import banner from "../assets/images/banner.png";

import "../styles/pages/Auth.css";

export default function AuthLayout({title, description, switchText, switchLink, switchLabel, children}) {
    return (
        <main className="auth-page">
            <div className="auth-container">

                <section className="auth-form-section">
                    <h1 className="auth-title">{title}</h1>

                    <p className="auth-description">
                        {description}
                    </p>

                    {children}

                    <div className="auth-switch">
                        <div className="auth-divider">
                            <span>lub</span>
                        </div>

                        <p>
                            {switchText}{" "}
                            <Link
                                to={switchLink}
                                className="auth-switch-link"
                            >
                                {switchLabel}
                            </Link>
                        </p>
                    </div>
                </section>

                <section className="auth-logo-section">
                    <img
                        src={banner}
                        alt="TechFix"
                        className="auth-logo"
                    />
                </section>

            </div>
        </main>
    );
}