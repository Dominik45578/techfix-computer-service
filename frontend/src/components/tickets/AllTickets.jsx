import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAvailableTickets } from "../../services/ticketService.js";
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

export default function AllTickets() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filters, setFilters] = useState(initialFilters);

    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        getAvailableTickets()
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

    const availableTickets = filterTickets(tickets, filters);

    const totalPages = Math.ceil(
        availableTickets.length / pageSize
    );

    const activePage = Math.min(
        currentPage,
        Math.max(1, totalPages)
    );

    const pageTickets = availableTickets.slice(
        (activePage - 1) * pageSize,
        activePage * pageSize
    );

    const handleClaim = (id) => {
        setTickets(prev =>
            prev.filter(ticket => ticket.id !== id)
        );
    };

    const handleDetails = (id) => {
        console.log("Szczegóły zgłoszenia:", id);
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
            title="Zgłoszenia do przejęcia"
            description="Przeglądaj dostępne zgłoszenia i wybierz kolejne zlecenie."
            tickets={availableTickets}
        >
            <TicketFilters
                filters={filters}
                services={services}
                onChange={handleFilterChange}
                onReset={handleResetFilters}
            />

            {loading && <p>Ładowanie zgłoszeń...</p>}

            {error && <p>{error}</p>}

            {pageTickets.map(ticket => (
                <TicketCard
                    key={ticket.id}
                    ticket={ticket}
                    showClaimButton={true}
                    onClaim={handleClaim}
                    onDetails={handleDetails}
                />
            ))}

            {!loading && !error && availableTickets.length === 0 && (
                <p>
                    {tickets.length === 0
                        ? "Brak zgłoszeń oczekujących na przejęcie."
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