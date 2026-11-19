import "../../styles/components/profile/ReportTimeline.css";

const statuses = [
    { key: "pending", label: "Oczekujące" },
    { key: "in_progress", label: "W trakcie" },
    { key: "completed", label: "Zakończone" }
];

export default function ReportTimeline({ status }) {
    const currentIndex = statuses.findIndex(
        item => item.key === status
    );

    if (status === "cancelled") {
        return (
            <div className="report-timeline">
                <h3>Postęp zgłoszenia</h3>
                <p className="report-timeline-cancelled">
                    Zgłoszenie zostało anulowane.
                </p>
            </div>
        );
    }

    return (
        <div className="report-timeline">
            <h3>Postęp zgłoszenia</h3>

            <div className="timeline-steps">
                {statuses.map((item, index) => (
                    <div
                        key={item.key}
                        className={`timeline-step timeline-status-${item.key} ${
                            index < currentIndex
                                ? "done"
                                : index === currentIndex
                                    ? "current"
                                    : ""
                        }`}
                    >
                        <div className="timeline-dot" />
                        <span>{item.label}</span>
                    </div>
                ))}
            </div>
        </div>
    );
}
