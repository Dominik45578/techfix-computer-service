import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getMyTickets } from "../../services/ticketService.js";
import { filterTickets } from "../../utils/ticketUtils.js";
import TicketsLayout from "../../layouts/TicketLayout.jsx";
import TicketCard from "./TicketCard.jsx";
import TicketFilters from "./TicketsFilters.jsx";
import Pagination from "../common/Pagination.jsx";

const initialFilters = {
    service: "all",
    status: "all",
    dateFrom: "",
    dateTo: "",
    sort: "oldest"
};

export default function MyTickets() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filters, setFilters] = useState(initialFilters);

    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        getMyTickets()
            .then(data => setTickets(data))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, []);

    const currentPage = Math.max(
        1,
        parseInt(searchParams.get("page"), 10) || 1
    );

    const pageSize = 7;

    const services = [...new Set(
        tickets.map(ticket => ticket.service)
    )];

    const myTickets = filterTickets(tickets, filters);

    const totalPages = Math.ceil(
        myTickets.length / pageSize
    );

    const activePage = Math.min(
        currentPage,
        Math.max(1, totalPages)
    );

    const pageTickets = myTickets.slice(
        (activePage - 1) * pageSize,
        activePage * pageSize
    );

    const handleDetails = (id) => {
        console.log("Szczegóły zgłoszenia:", id);
    };

    const handleStatusChange = (id, status) => {
        setTickets(prev =>
            prev
                .map(ticket =>
                    ticket.id === id
                        ? { ...ticket, status }
                        : ticket
                )
                .filter(ticket =>
                    ticket.status !== "completed"
                )
        );
    };

    const handlePageChange = (page) => {
        setSearchParams(prev => {
            const params = new URLSearchParams(prev);
            params.set("page", page.toString());
            return params;
        });
    };

    const handleFilterChange = (name, value) => {
        setFilters(prev => ({
            ...prev,
            [name]: value
        }));

        handlePageChange(1);
    };

    const handleResetFilters = () => {
        setFilters(initialFilters);
        handlePageChange(1);
    };

    return (
        <TicketsLayout
            title="Moje zgłoszenia"
            description="Przeglądaj i zarządzaj przypisanymi do Ciebie zgłoszeniami."
            tickets={myTickets}
        >
            <TicketFilters
                filters={filters}
                services={services}
                showStatus={true}
                onChange={handleFilterChange}
                onReset={handleResetFilters}
            />

            {loading && <p>Ładowanie zgłoszeń...</p>}

            {error && <p>{error}</p>}

            {pageTickets.map(ticket => (
                <TicketCard
                    key={ticket.id}
                    ticket={ticket}
                    editableStatus={true}
                    onStatusChange={handleStatusChange}
                    onDetails={handleDetails}
                />
            ))}

            {!loading && !error && myTickets.length === 0 && (
                <p>
                    {tickets.length === 0
                        ? "Nie masz aktualnie przypisanych zgłoszeń."
                        : "Nie znaleziono zgłoszeń spełniających kryteria filtrowania."
                    }
                </p>
            )}

            <Pagination
                currentPage={activePage}
                totalPages={totalPages}
                onPageChange={handlePageChange}
            />
        </TicketsLayout>
    );
}