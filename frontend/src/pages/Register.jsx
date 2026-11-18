import { useState } from "react";
import { Input } from "../components/Input.jsx";
import Button from "../components/Button.jsx";
import AuthLayout from "../layouts/AuthLayout.jsx";

export function Register() {
    const [formData, setFormData] = useState({
        email: "",
        phone: "",
        firstName: "",
        lastName: "",
        password: "",
        confirmPassword: ""
    });

    const [error, setError] = useState("");

    function handleChange(event) {
        const { name, value } = event.target;

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    function handleSubmit(event) {
        event.preventDefault();
        setError("");


        console.log("Dane rejestracji:", {
            email: formData.email,
            phone: formData.phone,
            firstName: formData.firstName,
            lastName: formData.lastName
        });
    }

    return (
        <AuthLayout
            title="Zarejestruj się"
            description="Utwórz konto na platformie TechFix"
            switchText="Masz już konto?"
            switchLink="/login"
            switchLabel="Zaloguj się"
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
                    label="Numer telefonu"
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    placeholder="Wprowadź numer telefonu"
                />

                <Input
                    label="Imię"
                    type="text"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    placeholder="Wprowadź imię"
                    required
                />

                <Input
                    label="Nazwisko"
                    type="text"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    placeholder="Wprowadź nazwisko"
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

                <Input
                    label="Powtórz hasło"
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="Powtórz hasło"
                    required
                />

                {error && (
                    <p className="auth-error" role="alert">
                        {error}
                    </p>
                )}

                <Button
                    text="Zarejestruj się"
                    variant="primary"
                    type="submit"
                />
            </form>
        </AuthLayout>
    );
}
