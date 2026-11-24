import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { getTicketById } from "../services/ticketService.js";
import TicketInformation from "../components/tickets/details/TicketInformation.jsx";
import TicketCustomer from "../components/tickets/details/TicketCustomer.jsx";
import TicketNote from "../components/tickets/details/TicketNote.jsx";
import TechnicianSection from "../components/tickets/details/TechnicalSection.jsx";
import TicketNoteHistory from "../components/tickets/details/TicketNoteHistory.jsx";
import Button from "../components/common/Button.jsx";
import "../styles/pages/TicketDetails.css";

export function TicketDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [ticket, setTicket] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true);
        setError(null);

        getTicketById(id)
            .then(data => setTicket(data))
            .catch(error => setError(error.message))
            .finally(() => setLoading(false));
    }, [id]);

    const currentTechnicianId = 1;

    const isAssigned = ticket?.technicianId != null;

    const isMyTicket =
        ticket?.technicianId === currentTechnicianId;

    const canEdit = isMyTicket
        && ["accepted", "in_progress"].includes(ticket.status);

    const canClaim = !isAssigned
        && ticket?.status === "pending";

    const canViewHistory = isMyTicket
        && ticket.status === "completed";

    const handleClaim = () => {
        if (!canClaim) return;

        setTicket(prev => ({
            ...prev,
            technicianId: currentTechnicianId,
            technician: "Jan Kowalski",
            status: "accepted",
            updatedAt: new Date().toISOString().slice(0, 10)
        }));
    };

    return (
        <main className="ticket-details-page">
            <div className="ticket-details-container">

                <button
                    type="button"
                    className="ticket-details-back"
                    onClick={() => navigate(-1)}
                >
                    <ArrowLeft size={18} />
                    Wróć do zgłoszeń
                </button>

                <header className="ticket-details-header">
                    <h1>Szczegóły zgłoszenia #{id}</h1>

                    {!loading && !error && canClaim && (
                        <Button
                            text="Przyjmij zgłoszenie"
                            variant="primary"
                            onClick={handleClaim}
                        />
                    )}
                </header>

                {loading && <p>Ładowanie zgłoszenia...</p>}

                {error && <p>{error}</p>}

                {!loading && !error && ticket && (
                    <>
                        <section className="ticket-details-grid">

                            <TicketInformation ticket={ticket} />

                            <TicketCustomer customer={ticket.customer} />

                            <TicketNote note={ticket.userNote} />

                        </section>

                        {canEdit && (
                            <TechnicianSection
                                ticket={ticket}
                                onTicketChange={setTicket}
                            />
                        )}

                        {canViewHistory && (
                            <section className="technician-section">
                                <TicketNoteHistory
                                    notes={ticket.technicianNotes ?? []}
                                />
                            </section>
                        )}
                    </>
                )}

            </div>
        </main>
    );
}