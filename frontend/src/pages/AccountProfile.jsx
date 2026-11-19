import { useState } from "react";
import AccountTabs from "../components/profile/AccountTabs.jsx";
import ProfileForm from "../components/profile/ProfileForm.jsx";
import Reports from "../components/profile/Reports.jsx";

import "../styles/pages/AccountProfile.css";

export function AccountProfile() {
    const [activeTab, setActiveTab] = useState("profile");

    return (
        <main className="account-page">
            <div className="account-container">
                <AccountTabs
                    activeTab={activeTab}
                    setActiveTab={setActiveTab}
                />

                {activeTab === "profile" ? <ProfileForm /> : <Reports />}
            </div>
        </main>
    );
}