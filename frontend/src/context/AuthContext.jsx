import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    const isLoggedIn = user !== null;
    const role = user?.role ?? null;
    const isAdmin = role === "admin";
    const isUser = role === "user";

    function login(userData) {
        setUser(userData);
    }

    function logout() {
        setUser(null);
    }

    return (
        <AuthContext.Provider
            value={{
                user,
                isLoggedIn,
                role,
                isAdmin,
                isUser,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error("useAuth musi być używany wewnątrz AuthProvider");
    }

    return context;
}
