import { Routes, Route, Navigate } from "react-router-dom";

import { Login } from "../pages/Login.jsx";
import { Register } from "../pages/Register.jsx";
import { ForgotPassword } from "../pages/ForgotPassword.jsx";
import { ResetPassword } from "../pages/ResetPassword.jsx";

export default function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/reset-password" element={<ResetPassword />} />
        </Routes>
    );
}