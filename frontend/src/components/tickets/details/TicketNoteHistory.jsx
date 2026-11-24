import { History, UserRound, ChevronDown } from "lucide-react";
import "../../../styles/components/serviceTickets/details/TicketNoteHistory.css";

export default function TicketNoteHistory({ notes = [] }) {

    const formatDate = (date) => {
        return new Date(date).toLocaleString("pl-PL", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        });
    };

    return (
        <div className="ticket-details-panel full-width">

            <div className="ticket-panel-title">
                <History size={20} />
                <h2>Historia notatek serwisowych</h2>

                <span className="ticket-note-count">
                    Liczba notatek: {notes.length}
                </span>
            </div>

            {notes.length === 0 ? (
                <p className="ticket-note-empty">
                    Nie dodano jeszcze żadnych notatek serwisowych.
                </p>
            ) : (
                <div className="ticket-note-history">

                    {notes.map(note => (
                        <details
                            key={note.id}
                            className="ticket-history-note"
                        >
                            <summary className="ticket-history-note-header">

                                <div className="ticket-history-author">
                                    <UserRound size={16} />
                                    <span>{note.technician}</span>
                                </div>

                                <div className="ticket-history-note-meta">
                                    <time dateTime={note.createdAt}>
                                        {formatDate(note.createdAt)}
                                    </time>

                                    <ChevronDown
                                        size={18}
                                        className="ticket-note-arrow"
                                    />
                                </div>

                            </summary>

                            <div className="ticket-history-note-body">
                                <p className="ticket-history-note-content">
                                    {note.content}
                                </p>
                            </div>

                        </details>
                    ))}

                </div>
            )}

        </div>
    );
}