import { useState } from "react";
import AllTickets from "../components/tickets/AllTickets.jsx";
import "../styles/pages/ServiceTickets.css";
import MyTickets from "../components/tickets/MyTickets.jsx";
import RepairHistory from "../components/tickets/RepairHistory.jsx";

export function ServiceTickets() {
    const [activeTab, setActiveTab] = useState("all");

    return (
        <main className="tickets-page">
            <div className="tickets-container">

                <div className="tickets-tabs">
                    <button
                        className={activeTab === "all" ? "active" : ""}
                        onClick={() => setActiveTab("all")}
                    >
                        Zgłoszenia
                    </button>

                    <button
                        className={activeTab === "mine" ? "active" : ""}
                        onClick={() => setActiveTab("mine")}
                    >
                        Moje zgłoszenia
                    </button>

                    <button
                        className={activeTab === "history" ? "active" : ""}
                        onClick={() => setActiveTab("history")}
                    >
                        Historia napraw
                    </button>
                </div>

                <div className="tickets-tab-content">
                    {activeTab === "all" && <AllTickets />}
                    {activeTab === "mine" && <MyTickets />}
                    {activeTab === "history" && <RepairHistory />}
                </div>

            </div>
        </main>
    );
}
