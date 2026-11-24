import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { getRepairHistory } from "../../services/ticketService.js";
import TicketsLayout from "../../layouts/TicketLayout.jsx";
import TicketCard from "./TicketCard.jsx";
import Pagination from "../common/Pagination.jsx";

export default function RepairHistory() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        getRepairHistory()
            .then(data => setTickets(data))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, []);

    const currentPage = Math.max(
        1,
        parseInt(searchParams.get("page"), 10) || 1
    );

    const pageSize = 7;

    const completedTickets = [...tickets].sort((a, b) =>
        (b.completedAt ?? b.createdAt).localeCompare(
            a.completedAt ?? a.createdAt
        )
    );

    const totalPages = Math.ceil(
        completedTickets.length / pageSize
    );

    const activePage = Math.min(
        currentPage,
        Math.max(1, totalPages)
    );

    const pageTickets = completedTickets.slice(
        (activePage - 1) * pageSize,
        activePage * pageSize
    );

    const handleDetails = (id) => {
        navigate(`/tickets/${id}`);
    };

    const handlePageChange = (page) => {
        setSearchParams(prev => {
            const params = new URLSearchParams(prev);
            params.set("page", page.toString());
            return params;
        });
    };

    return (
        <TicketsLayout
            title="Historia napraw"
            description="Przeglądaj zakończone przez Ciebie naprawy."
            tickets={completedTickets}
        >
            {loading && <p>Ładowanie zgłoszeń...</p>}

            {error && <p>{error}</p>}

            {pageTickets.map(ticket => (
                <TicketCard
                    key={ticket.id}
                    ticket={ticket}
                    onDetails={handleDetails}
                />
            ))}

            {!loading && !error && completedTickets.length === 0 && (
                <p>Nie masz jeszcze zakończonych napraw.</p>
            )}

            <Pagination
                currentPage={activePage}
                totalPages={totalPages}
                onPageChange={handlePageChange}
            />
        </TicketsLayout>
    );
}