import { ChevronDown } from "lucide-react";
import "../../styles/components/home/Card.css"

export default function Card({item, isActive, onClick}) {
    const Icon = item.icon;

    return (
        <button
            type="button"
            className={`specialty-card ${isActive ? "active" : ""}`}
            onClick={onClick}
            aria-expanded={isActive}
        >
            <div className="specialty-top">
                <div className="specialty-icon">
                    <Icon size={26} />
                </div>

                <ChevronDown
                    size={20}
                    className="specialty-arrow"
                />
            </div>

            <h3>{item.title}</h3>

            <div className="specialty-description">
                <p>{item.description}</p>
            </div>
        </button>
    );
}
