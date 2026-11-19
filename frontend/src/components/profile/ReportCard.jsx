import { ChevronRight, CalendarDays } from "lucide-react";

const statusLabels = {
    pending: "Oczekujące",
    in_progress: "W trakcie",
    completed: "Zakończone",
    cancelled: "Anulowane"
};

export default function ReportCard({ report, selected, onClick }) {
    return (
        <button
            type="button"
            className={`report-card ${selected ? "selected" : ""}`}
            onClick={onClick}
        >
            <div className="report-card-top">
                <span className="report-number">
                    Zgłoszenie #{report.id}
                </span>

                <span className={`report-status status-${report.status}`}>
                    {statusLabels[report.status]}
                </span>
            </div>

            <h3>{report.title}</h3>
            <p className="report-device">{report.device}</p>

            <div className="report-card-bottom">
                <span>
                    <CalendarDays size={16} />
                    {report.createdAt}
                </span>

                <ChevronRight size={19} />
            </div>
        </button>
    );
}
