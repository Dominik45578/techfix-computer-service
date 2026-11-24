import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { UserRound, Pencil, LockKeyhole, LogOut } from "lucide-react";
import { useAuth } from "../../context/AuthContext.jsx";
import Input from "../common/Input.jsx";
import Button from "../common/Button.jsx";

import "../../styles/components/profile/ProfileForm.css";

function getUserData(user) {
    return {
        firstName: user?.firstName ?? "",
        lastName: user?.lastName ?? "",
        email: user?.email ?? "",
        phone: user?.phone ?? ""
    };
}

export default function ProfileForm() {
    const { user, login, logout } = useAuth();
    const navigate = useNavigate();

    const [formData, setFormData] = useState(() => getUserData(user));
    const [isEditing, setIsEditing] = useState(false);
    const [message, setMessage] = useState("");

    const [isChangingPassword, setIsChangingPassword] = useState(false);

    const [passwordData, setPasswordData] = useState({
        currentPassword: "",
        newPassword: "",
        confirmPassword: ""
    });

    const [passwordError, setPasswordError] = useState("");

    function handleChange(event) {
        const { name, value } = event.target;

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    function handleEdit() {
        setFormData(getUserData(user));
        setMessage("");
        setIsEditing(true);
    }

    function handleCancel() {
        setFormData(getUserData(user));
        setIsEditing(false);
        setMessage("");
    }

    function handleSubmit(event) {
        event.preventDefault();

        login({
            ...user,
            ...formData
        });

        setIsEditing(false);
        setMessage("Dane zostały zaktualizowane testowo.");
    }

    function handleLogout() {
        logout();
        navigate("/login", { replace: true });
    }

    function handlePasswordChange(event) {
        const { name, value } = event.target;

        setPasswordData(prev => ({
            ...prev,
            [name]: value
        }));

        setPasswordError("");
    }

    function handleCancelPassword() {
        setIsChangingPassword(false);
        setPasswordData({
            currentPassword: "",
            newPassword: "",
            confirmPassword: ""
        });
        setPasswordError("");
    }

    function handlePasswordSubmit(event) {
        event.preventDefault();
        setPasswordError("Zmiana hasła będzie dostępna po podłączeniu backendu.");
    }

    return (
        <div className="profile-content">
            <section className="account-card profile-details">
                <div className="profile-section-header">
                    <div className="profile-section-title">
                        <div className="profile-section-icon">
                            <UserRound size={26} />
                        </div>
                        <h2>Dane kontaktowe</h2>
                    </div>

                    {!isEditing && (
                        <Button
                            text="Edytuj dane"
                            variant="outline"
                            icon={Pencil}
                            onClick={handleEdit}
                        />
                    )}
                </div>

                <form onSubmit={handleSubmit} className="profile-form">
                    <div className="profile-fields">
                        <Input
                            label="Imię"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />

                        <Input
                            label="Nazwisko"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />

                        <Input
                            label="Adres e-mail"
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            disabled={!isEditing}
                            required
                        />

                        <Input
                            label="Numer telefonu"
                            type="tel"
                            name="phone"
                            value={formData.phone}
                            onChange={handleChange}
                            disabled={!isEditing}
                        />
                    </div>

                    {message && (
                        <p className="profile-message" role="status">
                            {message}
                        </p>
                    )}

                    {isEditing && (
                        <div className="profile-actions">
                            <Button
                                text="Anuluj"
                                variant="outline"
                                onClick={handleCancel}
                            />

                            <Button
                                text="Zapisz zmiany"
                                type="submit"
                                variant="primary"
                            />
                        </div>
                    )}
                </form>
            </section>


            <section className="account-card profile-security">
                <div className="profile-security-header">
                    <div className="profile-section-title">
                        <div className="profile-section-icon">
                            <LockKeyhole size={26} />
                        </div>

                        <div>
                            <h2>Bezpieczeństwo</h2>
                            <p>Zarządzaj hasłem do swojego konta.</p>
                        </div>
                    </div>

                    {!isChangingPassword && (
                        <Button
                            text="Zmień hasło"
                            variant="outline"
                            icon={LockKeyhole}
                            onClick={() => setIsChangingPassword(true)}
                        />
                    )}
                </div>

                {isChangingPassword && (
                    <form
                        className="profile-password-form"
                        onSubmit={handlePasswordSubmit}
                    >
                        <div className="profile-password-fields">
                            <Input
                                label="Aktualne hasło"
                                type="password"
                                name="currentPassword"
                                value={passwordData.currentPassword}
                                onChange={handlePasswordChange}
                                required
                            />

                            <Input
                                label="Nowe hasło"
                                type="password"
                                name="newPassword"
                                value={passwordData.newPassword}
                                onChange={handlePasswordChange}
                                required
                            />

                            <Input
                                label="Potwierdź nowe hasło"
                                type="password"
                                name="confirmPassword"
                                value={passwordData.confirmPassword}
                                onChange={handlePasswordChange}
                                required
                            />
                        </div>

                        {passwordError && (
                            <p className="profile-password-error" role="alert">
                                {passwordError}
                            </p>
                        )}

                        <div className="profile-actions">
                            <Button
                                text="Anuluj"
                                variant="outline"
                                onClick={handleCancelPassword}
                            />

                            <Button
                                text="Zapisz nowe hasło"
                                type="submit"
                                variant="primary"
                            />
                        </div>
                    </form>
                )}
            </section>

            <div className="profile-logout">
                <Button
                    text="Wyloguj się"
                    variant="alert"
                    icon={LogOut}
                    onClick={handleLogout}
                />
            </div>
        </div>
    );
}
