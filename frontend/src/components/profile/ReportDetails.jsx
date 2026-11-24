import {X, CalendarDays, UserRound, Laptop, ClipboardList} from "lucide-react";
import Button from "../common/Button.jsx"
import ReportInfo from "./ReportInfo.jsx";
import ReportTimeline from "./ReportTimeline.jsx";

export default function ReportDetails({ report, onClose }) {
    return (
        <aside className="report-details">
            <div className="report-details-header">
                <div>
                    <span className="report-number">
                        Zgłoszenie #{report.id}
                    </span>
                    <h2>{report.title}</h2>
                </div>

                <Button
                    icon={X}
                    variant="alert"
                    ariaLabel="Zamknij szczegóły"
                    onClick={onClose}
                    className="report-close"
                />
            </div>

            <ReportTimeline status={report.status} />

            <div className="report-details-section">
                <h3>Informacje o zgłoszeniu</h3>

                <ReportInfo
                    icon={CalendarDays}
                    label="Data zgłoszenia"
                    value={report.createdAt}
                />

                <ReportInfo
                    icon={Laptop}
                    label="Urządzenie"
                    value={report.device}
                />

                <ReportInfo
                    icon={UserRound}
                    label="Przypisany technik"
                    value={report.technician ?? "Nie przypisano"}
                />
            </div>

            <div className="report-details-section">
                <h3>
                    <ClipboardList size={19} />
                    Opis usterki
                </h3>
                <p className="report-description">
                    {report.description}
                </p>
            </div>
        </aside>
    );
}
