import {Info, Hash, Package, Layers, Laptop, CalendarDays, ChartNoAxesColumn, CalendarClock, UserRound} from "lucide-react";

import StatusBadge from "../../common/StatusBadge.jsx";
import "../../../styles/components/serviceTickets/details/TicketInformation.css"

export default function TicketInformation({ ticket }) {
    return (
        <div className="ticket-details-panel">

            <div className="ticket-panel-title">
                <Info size={20} />
                <h2>Informacje o zgłoszeniu</h2>
            </div>

            <div className="ticket-information-list">

                <div className="ticket-information-row">
                    <Hash size={18} />
                    <span>Numer zgłoszenia:</span>
                    <p>{ticket.id}</p>
                </div>

                <div className="ticket-information-row">
                    <Package size={18} />
                    <span>Usługa:</span>
                    <p>{ticket.service}</p>
                </div>

                <div className="ticket-information-row">
                    <Layers size={18} />
                    <span>Zakres:</span>
                    <p>{ticket.scope}</p>
                </div>

                {ticket.device?.trim() && (
                    <div className="ticket-information-row">
                        <Laptop size={18} />
                        <span>Urządzenie:</span>
                        <p>{ticket.device}</p>
                    </div>
                )}

                <div className="ticket-information-row">
                    <CalendarDays size={18} />
                    <span>Data zgłoszenia:</span>
                    <p>{ticket.createdAt}</p>
                </div>

                <div className="ticket-information-row">
                    <ChartNoAxesColumn size={18} />
                    <span>Status:</span>
                    <div>
                        <StatusBadge status={ticket.status} />
                    </div>
                </div>

                <div className="ticket-information-row">
                    <CalendarClock size={18} />
                    <span>Ostatnia aktualizacja:</span>
                    <p>{ticket.updatedAt || "—"}</p>
                </div>

                {ticket.technicianId != null && (
                    <div className="ticket-information-row">
                        <UserRound size={18} />
                        <span>Przypisany serwisant:</span>
                        <p>{ticket.technician}</p>
                    </div>
                )}

            </div>
        </div>
    );
}