import { UserRound, ClipboardList } from "lucide-react";
import { useAuth } from "../../context/AuthContext.jsx";

export default function AccountTabs({ activeTab, setActiveTab }) {
    const { isUser } = useAuth();
    return (
        <div className="account-tabs">
            <button
                type="button"
                className={`account-tab ${activeTab === "profile" ? "active" : ""}`}
                onClick={() => setActiveTab("profile")}
            >
                <UserRound size={20} />
                Mój profil
            </button>

            {isUser && (
                <button
                    type="button"
                    className={`account-tab ${activeTab === "reports" ? "active" : ""}`}
                    onClick={() => setActiveTab("reports")}
                >
                    <ClipboardList size={20} />
                    Moje zgłoszenia
                </button>
            )}
        </div>
    );
}