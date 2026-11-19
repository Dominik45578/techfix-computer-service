export default function ReportInfo({ icon: Icon, label, value }) {
    return (
        <div className="report-info-row">
            <Icon size={19} />
            <div>
                <span>{label}</span>
                <strong>{value}</strong>
            </div>
        </div>
    );
}