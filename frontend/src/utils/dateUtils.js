export function getDaysBetween(startDate, endDate = new Date()) {
    const start = new Date(startDate);
    const end = new Date(endDate);

    const startUTC = Date.UTC(
        start.getUTCFullYear(),
        start.getUTCMonth(),
        start.getUTCDate()
    );

    const endUTC = Date.UTC(
        end.getFullYear(),
        end.getMonth(),
        end.getDate()
    );

    return Math.floor((endUTC - startUTC) / 86400000);
}