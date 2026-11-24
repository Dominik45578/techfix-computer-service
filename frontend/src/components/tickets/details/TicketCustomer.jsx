import { UserRound, Hash, Mail, Phone } from "lucide-react";
import "../../../styles/components/serviceTickets/details/TicketCustomer.css"

export default function TicketCustomer({ customer }) {
    return (
        <div className="ticket-details-panel">

            <div className="ticket-panel-title">
                <UserRound size={20} />
                <h2>Dane użytkownika</h2>
            </div>

            <div className="ticket-customer-list">

                <div className="ticket-customer-row">
                    <Hash size={18} />
                    <span>ID użytkownika:</span>
                    <p>#{customer.id}</p>
                </div>

                <div className="ticket-customer-row">
                    <UserRound size={18} />
                    <span>Imię i nazwisko:</span>
                    <p>
                        {customer.firstName} {customer.lastName}
                    </p>
                </div>

                <div className="ticket-customer-row">
                    <Mail size={18} />
                    <span>Adres e-mail:</span>
                    <p>{customer.email}</p>
                </div>

                <div className="ticket-customer-row">
                    <Phone size={18} />
                    <span>Numer telefonu:</span>
                    <p>{customer.phone || "Nie podano"}</p>
                </div>

            </div>
        </div>
    );
}