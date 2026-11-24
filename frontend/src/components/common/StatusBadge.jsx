import Select from "./Select.jsx";
import "../../styles/components/common/StatusBadge.css";

const statusLabels = {
    pending: "Oczekujące",
    in_progress: "W trakcie realizacji",
    completed: "Zakończone",
    cancelled: "Anulowane",
    accepted: "Przyjęte",
    rejected: "Odrzucone"
};

const availableTransitions = {
    accepted: ["in_progress"],
    in_progress: ["completed"]
};

export default function StatusBadge({status, editable = false, onStatusChange}) {
    const availableStatuses = availableTransitions[status] ?? [];

    const options = [status, ...availableStatuses].map(value => ({
        value,
        label: statusLabels[value] ?? value
    }));

    if (editable && availableStatuses.length > 0) {
        return (
            <Select
                variant="badge"
                className={`status-badge status-${status}`}
                value={status}
                options={options}
                onChange={event => {
                    if (event.target.value !== status) {
                        onStatusChange?.(event.target.value);
                    }
                }}
            />
        );
    }

    return (
        <span className={`status-badge status-${status}`}>
            {statusLabels[status] ?? status}
        </span>
    );
}