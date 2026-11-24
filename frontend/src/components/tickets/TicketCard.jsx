import { CalendarDays, Clock3} from "lucide-react";
import Button from "../common/Button.jsx";
import StatusBadge from "../common/StatusBadge.jsx";
import { getDaysBetween } from "../../utils/dateUtils.js";
import "../../styles/components/serviceTickets/TicketCard.css";

export default function TicketCard({ticket, showClaimButton = false, editableStatus = false, onStatusChange , onClaim, onDetails}) {
    const waitingDays = Math.max(0, getDaysBetween(ticket.createdAt));

    const urgency =
        waitingDays >= 7 ? "urgent" :
            waitingDays >= 3 ? "warning" :
                "normal";

    return (
        <article className={`ticket-card ${showClaimButton ? `ticket-${urgency}` : ""}`}>
            <div className="ticket-card-main">
                <div className="ticket-card-image">
                    <img
                        src={ticket.image || "/images/tickets/default.jpg"}
                        alt={ticket.service}
                        loading="lazy"
                    />
                </div>

                <div className="ticket-card-info">
                    <div className="ticket-card-heading">
                        <h3>{ticket.service}</h3>

                        <span className="ticket-card-number">
                            #{ticket.id}
                        </span>
                    </div>

                    <p className="ticket-card-scope">
                        <strong>Zakres:</strong> {ticket.scope}
                    </p>

                    {ticket.device?.trim() && (
                        <p className="ticket-card-device">
                            <strong>Urządzenie:</strong> {ticket.device}
                        </p>
                    )}

                    <div className="ticket-card-meta">
                        <span className="ticket-card-date">
                            <CalendarDays size={15} />
                            {ticket.createdAt}
                        </span>

                        {showClaimButton && (
                            <span className={`ticket-waiting ${urgency}`}>
                                <Clock3 size={15} />
                                Oczekuje {waitingDays} {waitingDays === 1 ? "dzień" : "dni"}
                            </span>
                        )}

                        {!showClaimButton && (
                            <StatusBadge
                                status={ticket.status}
                                editable={editableStatus}
                                onStatusChange={status =>
                                    onStatusChange?.(ticket.id, status)
                                }
                            />
                        )}
                    </div>
                </div>
            </div>

            <div className="ticket-card-actions">
                <Button
                    text="Szczegóły"
                    variant="outline"
                    onClick={() => onDetails?.(ticket.id)}
                />

                {showClaimButton && (
                    <Button
                        text="Przejmij zgłoszenie"
                        variant="primary"
                        onClick={() => onClaim?.(ticket.id)}
                    />
                )}
            </div>
        </article>
    );
}