import { useState } from "react";
import ReportCard from "./ReportCard.jsx";
import ReportDetails from "./ReportDetails.jsx";

import "../../styles/components/profile/Report.css";

const exampleReports = [
    {
        id: 1042,
        title: "Laptop nie uruchamia się",
        device: "Dell Inspiron 15",
        description: "Laptop nie reaguje na przycisk zasilania. Po podłączeniu ładowarki nie świeci się żadna kontrolka.",
        createdAt: "2026-11-15",
        status: "in_progress",
        technician: "Jan Kowalski"
    },
    {
        id: 1038,
        title: "Wymiana dysku SSD",
        device: "Lenovo ThinkPad T480",
        description: "Chciałbym wymienić obecny dysk na SSD o większej pojemności.",
        createdAt: "2026-11-10",
        status: "pending",
        technician: null
    },
    {
        id: 1025,
        title: "Czyszczenie układu chłodzenia",
        device: "ASUS TUF Gaming",
        description: "Laptop mocno się nagrzewa i głośno pracuje podczas użytkowania.",
        createdAt: "2026-10-28",
        status: "completed",
        technician: "Anna Nowak"
    }
];

export default function Reports() {
    const [reports] = useState(exampleReports);
    const [selectedReportId, setSelectedReportId] = useState(null);

    const selectedReport = reports.find(
        report => report.id === selectedReportId
    );

    return (
        <div className="reports-content">
            <div className="reports-header">
                <div>
                    <h2>Moje zgłoszenia</h2>
                    <p>Sprawdzaj status swoich zgłoszeń serwisowych.</p>
                </div>

                <span className="reports-count">
                    {reports.length} zgłoszenia
                </span>
            </div>

            <div className={`reports-layout ${selectedReport ? "has-selection" : ""}`}>
                <div className="reports-list">
                    {reports.map(report => (
                        <ReportCard
                            key={report.id}
                            report={report}
                            selected={selectedReportId === report.id}
                            onClick={() => setSelectedReportId(report.id)}
                        />
                    ))}
                </div>

                {selectedReport && (
                    <ReportDetails
                        report={selectedReport}
                        onClose={() => setSelectedReportId(null)}
                    />
                )}
            </div>
        </div>
    );
}
