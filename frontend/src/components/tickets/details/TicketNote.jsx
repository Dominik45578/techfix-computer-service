import { MessageSquare } from "lucide-react";
import "../../../styles/components/serviceTickets/details/TicketNote.css";

export default function TicketNote({ note }) {
    return (
        <div className="ticket-details-panel full-width">
            <div className="ticket-panel-title">
                <MessageSquare size={20} />
                <h2>Notatka od użytkownika</h2>
            </div>

            <p className="ticket-user-note">
                {note?.trim() || "Użytkownik nie dodał notatki."}
            </p>
        </div>
    );
}