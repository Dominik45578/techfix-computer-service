import { useState } from "react";
import { RefreshCw } from "lucide-react";
import Select from "../../common/Select.jsx";
import Button from "../../common/Button.jsx";
import StatusBadge from "../../common/StatusBadge.jsx";
import "../../../styles/components/serviceTickets/details/TicketChangeStatus.css";

const availableTransitions = {
    accepted: [
        { value: "in_progress", label: "W trakcie realizacji" }
    ],
    in_progress: [
        { value: "completed", label: "Zakończone" }
    ]
};

export default function TicketStatusChange({ status, onStatusChange }) {
    const [selectedStatus, setSelectedStatus] = useState("");

    const options = availableTransitions[status] ?? [];

    const handleSave = () => {
        if (!options.some(option => option.value === selectedStatus)) {
            return;
        }

        onStatusChange(selectedStatus);
        setSelectedStatus("");
    };

    return (
        <div className="ticket-details-panel full-width">

            <div className="ticket-panel-title">
                <RefreshCw size={20} />
                <h2>Zmiana statusu zgłoszenia</h2>
            </div>

            <div className="ticket-status-form">

                <div className="ticket-status-current">
                    <span>Aktualny status</span>

                    <div className="ticket-status-value">
                        <StatusBadge status={status} />
                    </div>
                </div>

                <Select
                    id="ticket-status"
                    label="Nowy status"
                    placeholder="Wybierz status"
                    value={selectedStatus}
                    options={options}
                    onChange={event =>
                        setSelectedStatus(event.target.value)
                    }
                />

                <Button
                    text="Zapisz status"
                    variant="primary"
                    onClick={handleSave}
                    disabled={!selectedStatus}
                />

            </div>
        </div>
    );
}