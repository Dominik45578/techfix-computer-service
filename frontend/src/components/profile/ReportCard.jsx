import { ChevronRight, CalendarDays } from "lucide-react";
import StatusBadge from "../common/StatusBadge.jsx";

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

                <StatusBadge status={report.status} />
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
