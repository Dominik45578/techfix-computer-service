import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Input from "../components/Input.jsx";
import Button from "../components/Button.jsx";
import AuthLayout from "../layouts/AuthLayout.jsx";
import { useAuth } from "../context/AuthContext.jsx";

export function Login() {
    const [formData, setFormData] = useState({
        email: "",
        password: ""
    });

    const { login } = useAuth();
    const navigate = useNavigate();

    const [error, setError] = useState("");

    function handleChange(e) {
        const { name, value } = e.target;

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    function handleSubmit(event) {
        event.preventDefault();
        setError("");

        if (
            formData.email !== "jan@example.com" ||
            formData.password !== "123456"
        ) {
            setError("Nieprawidłowy adres e-mail lub hasło.");
            return;
        }

        login({
            id: 1,
            firstName: "Jan",
            lastName: "Kowalski",
            email: formData.email,
            role: "user"
        });

        navigate("/");

        console.log("Próba logowania:", formData.email);
    }

    return (
        <AuthLayout
            title="Zaloguj się"
            description="Zaloguj się do swojego konta na platformie TechFix"
            switchText="Nie masz konta?"
            switchLink="/register"
            switchLabel="Zarejestruj się"
        >
            <form className="auth-form" onSubmit={handleSubmit}>
                <Input
                    label="Adres e-mail"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Wprowadź adres e-mail"
                    required
                />

                <Input
                    label="Hasło"
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Wprowadź hasło"
                    required
                />

                <div className="auth-options">
                    <Link to="/forgot-password" className="auth-link">
                        Nie pamiętasz hasła?
                    </Link>
                </div>

                {error && (
                    <p className="auth-error" role="alert">
                        {error}
                    </p>
                )}

                <Button
                    text="Zaloguj się"
                    variant="primary"
                    type="submit"
                />
            </form>
        </AuthLayout>
    );
}
