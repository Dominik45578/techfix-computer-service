import { ChevronLeft, ChevronRight } from "lucide-react";
import "../../styles/components/common/Pagination.css";

export default function Pagination({currentPage, totalPages, onPageChange}) {
    if (totalPages <= 1) return null;

    return (
        <nav className="pagination" aria-label="Paginacja">
            <button
                type="button"
                className="pagination-button"
                disabled={currentPage === 1}
                onClick={() => onPageChange(currentPage - 1)}
                aria-label="Poprzednia strona"
            >
                <ChevronLeft size={18} />
            </button>

            {Array.from({ length: totalPages }, (_, index) => (
                <button
                    type="button"
                    key={index + 1}
                    className={`pagination-button ${
                        currentPage === index + 1 ? "active" : ""
                    }`}
                    onClick={() => onPageChange(index + 1)}
                    aria-current={
                        currentPage === index + 1 ? "page" : undefined
                    }
                >
                    {index + 1}
                </button>
            ))}

            <button
                type="button"
                className="pagination-button"
                disabled={currentPage === totalPages}
                onClick={() => onPageChange(currentPage + 1)}
                aria-label="Następna strona"
            >
                <ChevronRight size={18} />
            </button>
        </nav>
    );
}
