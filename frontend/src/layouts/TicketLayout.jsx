import "../styles/pages/ServiceTickets.css";

export default function TicketsLayout({title, description, tickets = [], children}) {
    return (
        <section className="tickets-layout">

            <div className="tickets-layout-header">
                <div>
                    <h2>{title}</h2>
                    {description && <p>{description}</p>}
                </div>

                <span className="tickets-count">
                    {tickets.length} zgłoszeń
                </span>
            </div>

            <div className="tickets-layout-content">
                {children}
            </div>

        </section>
    );
}
