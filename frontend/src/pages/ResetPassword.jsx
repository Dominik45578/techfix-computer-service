import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import Input from "../components/common/Input.jsx";
import Button from "../components/common/Button.jsx";
import AuthLayout from "../layouts/AuthLayout.jsx";

export function ResetPassword() {
    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");
    const email = searchParams.get("email");

    const [formData, setFormData] = useState({
        password: "",
        confirmPassword: ""
    });

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    function handleChange(event) {
        const { name, value } = event.target;

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setSuccess("");

        setLoading(true);

        try {
        } catch {
            setError("Wystąpił błąd. Spróbuj ponownie.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Resetowanie hasła"
            description="Wprowadź nowe hasło do swojego konta TechFix."
            switchText="Pamiętasz hasło?"
            switchLink="/login"
            switchLabel="Wróć do logowania"
        >
            <form className="auth-form" onSubmit={handleSubmit}>
                <Input
                    label="Nowe hasło"
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Wprowadź nowe hasło"
                    required
                />

                <Input
                    label="Powtórz nowe hasło"
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="Powtórz nowe hasło"
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
                    text={loading ? "Zapisywanie..." : "Zresetuj hasło"}
                    variant="primary"
                    type="submit"
                    disabled={loading}
                />
            </form>
        </AuthLayout>
    );
}
