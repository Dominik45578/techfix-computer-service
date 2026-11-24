import { ListFilter } from "lucide-react";
import Select from "../common/Select.jsx";
import "../../styles/components/serviceTickets/TicketFillters.css"

export default function TicketFilters({filters, onChange, onReset, services = [], showStatus = false, dateLabel = "Data zgłoszenia"}) {
    const serviceOptions = [
        { value: "all", label: "Wszystkie usługi" },
        ...services.map(service => ({
            value: service,
            label: service
        }))
    ];

    const statusOptions = [
        { value: "all", label: "Wszystkie statusy" },
        { value: "accepted", label: "Przyjęte" },
        { value: "in_progress", label: "W trakcie realizacji" }
    ];

    const sortOptions = [
        { value: "oldest", label: "Od najstarszych" },
        { value: "newest", label: "Od najnowszych" }
    ];

    return (
        <div className="ticket-filters">

            <div className="ticket-filters-header">
                <div className="ticket-filters-title">
                    <ListFilter size={18} />
                    <h3>Filtry</h3>
                </div>

                <button
                    type="button"
                    className="ticket-filters-reset"
                    onClick={onReset}
                >
                    Wyczyść filtry
                </button>
            </div>

            <div className="ticket-filters-fields">

                <Select
                    id="filter-service"
                    label="Usługa"
                    value={filters.service}
                    options={serviceOptions}
                    onChange={event =>
                        onChange("service", event.target.value)
                    }
                />

                {showStatus && (
                    <Select
                        id="filter-status"
                        label="Status"
                        value={filters.status}
                        options={statusOptions}
                        onChange={event =>
                            onChange("status", event.target.value)
                        }
                    />
                )}

                <div className="ticket-filter-date">
                    <label htmlFor="filter-date-from">
                        {dateLabel} od
                    </label>

                    <input
                        id="filter-date-from"
                        type="date"
                        value={filters.dateFrom}
                        max={filters.dateTo || undefined}
                        onChange={event =>
                            onChange("dateFrom", event.target.value)
                        }
                    />
                </div>

                <div className="ticket-filter-date">
                    <label htmlFor="filter-date-to">
                        {dateLabel} do
                    </label>

                    <input
                        id="filter-date-to"
                        type="date"
                        value={filters.dateTo}
                        min={filters.dateFrom || undefined}
                        onChange={event =>
                            onChange("dateTo", event.target.value)
                        }
                    />
                </div>

                <Select
                    id="filter-sort"
                    label="Sortowanie"
                    value={filters.sort}
                    options={sortOptions}
                    onChange={event =>
                        onChange("sort", event.target.value)
                    }
                />

            </div>
        </div>
    );
}