import TicketStatusChange from "./TicketStatusChange.jsx";
import TechnicianNote from "./TechnicalNote.jsx";
import RepairCost from "./RepairCost.jsx";
import TicketNoteHistory from "./TicketNoteHistory.jsx";
import "../../../styles/components/serviceTickets/details/TechnicalSection.css";

export default function TechnicianSection({ ticket, onTicketChange }) {

    const handleStatusChange = (status) => {
        const today = new Date().toISOString().slice(0, 10);

        onTicketChange(prev => ({
            ...prev,
            status: status,
            updatedAt: today,
            completedAt: status === "completed"
                ? today
                : prev.completedAt
        }));
    };

    const handleAddNote = (content) => {
        const newNote = {
            id: Date.now(),
            type: "note",
            content: content,
            technicianId: ticket.technicianId,
            technician: ticket.technician,
            createdAt: new Date().toISOString()
        };

        onTicketChange(prev => ({
            ...prev,
            updatedAt: new Date().toISOString().slice(0, 10),
            technicianNotes: [
                newNote,
                ...(prev.technicianNotes ?? [])
            ]
        }));
    };

    const handleSaveCosts = (items, total) => {
        const costDetails = items.map(item => {
            const itemTotal = Number(item.quantity)
                * Math.round(Number(item.unitPrice) * 100) / 100;

            return `- ${item.name}: ${item.quantity} szt. × ${Number(item.unitPrice).toFixed(2)} zł = ${itemTotal.toFixed(2)} zł`;
        }).join("\n");

        const content = `Końcowe rozliczenie naprawy:\n\n${costDetails}\n\nCałkowity koszt: ${total.toFixed(2)} zł`;

        const newNote = {
            id: Date.now(),
            type: "settlement",
            content: content,
            technicianId: ticket.technicianId,
            technician: ticket.technician,
            createdAt: new Date().toISOString()
        };

        onTicketChange(prev => ({
            ...prev,
            totalCost: total,
            updatedAt: new Date().toISOString().slice(0, 10),
            technicianNotes: [
                newNote,
                ...(prev.technicianNotes ?? [])
            ]
        }));
    };

    return (
        <section className="technician-section">
            <h2>Sekcja serwisanta</h2>

            <div className="technician-grid">

                <TicketStatusChange
                    status={ticket.status}
                    onStatusChange={handleStatusChange}
                />

                <TechnicianNote
                    onAddNote={handleAddNote}
                />

                <RepairCost
                    totalCost={ticket.totalCost}
                    onSave={handleSaveCosts}
                />

                <TicketNoteHistory notes={ticket.technicianNotes ?? []} />

            </div>
        </section>
    );
}