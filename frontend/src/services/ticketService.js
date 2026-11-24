async function getTickets() {
    const response = await fetch("/data/ticket.json");

    if (!response.ok) {
        throw new Error("Nie udało się pobrać zgłoszeń.");
    }

    return response.json();
}

export async function getAvailableTickets() {
    const tickets = await getTickets();

    return tickets.filter(ticket =>
        ticket.status === "pending" &&
        ticket.technicianId === null
    );
}

export async function getMyTickets() {
    const tickets = await getTickets();

    return tickets.filter(ticket =>
        ticket.technicianId === 1 &&
        ["accepted", "in_progress"].includes(ticket.status)
    );
}

export async function getRepairHistory() {
    const tickets = await getTickets();

    return tickets.filter(ticket =>
        ticket.technicianId === 1 &&
        ticket.status === "completed"
    );
}

export async function getTicketById(id) {
    const tickets = await getTickets();

    const ticket = tickets.find(ticket =>
        ticket.id === Number(id)
    );

    if (!ticket) {
        throw new Error("Nie znaleziono zgłoszenia.");
    }

    return ticket;
}