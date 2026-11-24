export function filterTickets(tickets, filters, dateField = "createdAt") {
    return tickets
        .filter(ticket => {
            const date = ticket[dateField]?.slice(0, 10);

            const matchesService =
                filters.service === "all" ||
                ticket.service === filters.service;

            const matchesStatus =
                filters.status === "all" ||
                ticket.status === filters.status;

            const matchesDateFrom =
                !filters.dateFrom ||
                (date && date >= filters.dateFrom);

            const matchesDateTo =
                !filters.dateTo ||
                (date && date <= filters.dateTo);

            return matchesService &&
                matchesStatus &&
                matchesDateFrom &&
                matchesDateTo;
        })
        .sort((a, b) => {
            const dateA = a[dateField] ?? "";
            const dateB = b[dateField] ?? "";

            return filters.sort === "oldest"
                ? dateA.localeCompare(dateB)
                : dateB.localeCompare(dateA);
        });
}