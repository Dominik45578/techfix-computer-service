import { useState } from "react";
import { Link } from "react-router-dom";
import Input from "../components/common/Input.jsx";
import Button from "../components/common/Button.jsx";
import AuthLayout from "../layouts/AuthLayout.jsx";

export function ForgotPassword() {
    const [email, setEmail] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setSuccess("");

        if (!email.trim()) {
            setError("Wprowadź adres e-mail.");
            return;
        }

        setLoading(true);

        try {
            console.log("Przypomnienie hasła:", email);
        } catch {
            setError("Wystąpił błąd. Spróbuj ponownie.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Nie pamiętasz hasła?"
            description="Podaj adres e-mail przypisany do Twojego konta. Wyślemy Ci instrukcję resetowania hasła."
            switchText="Pamiętasz hasło?"
            switchLink="/login"
            switchLabel="Wróć do logowania"
        >
            <form className="auth-form" onSubmit={handleSubmit}>
                <Input
                    label="Adres e-mail"
                    type="email"
                    name="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Wprowadź adres e-mail"
                    required
                />

                {error && (
                    <p className="auth-error" role="alert">
                        {error}
                    </p>
                )}

                {success && (
                    <p className="auth-success" role="status">
                        {success}
                    </p>
                )}

                <Button
                    text={loading ? "Wysyłanie..." : "Wyślij instrukcję"}
                    variant="primary"
                    type="submit"
                    disabled={loading}
                />
            </form>
        </AuthLayout>
    );
}
