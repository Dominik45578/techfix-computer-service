
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ArrowRight } from "lucide-react";

import ServiceCard from "./ServiceCard.jsx";
import ServiceFilters from "./ServiceFilters.jsx";
import Button from "../Button.jsx"

import "../../styles/components/home/Services.css";

const categories = [
    "Wszystkie",
    "Naprawa",
    "Wymiana",
    "Oprogramowanie",
    "Modernizacja"
];

const services = [
    {
        id: 1,
        name: "Naprawa komputerów stacjonarnych",
        category: "Naprawa",
        description: "Diagnostyka i naprawa komputerów PC.",
        image: "/images/services/img1.png"
    },
    {
        id: 2,
        name: "Naprawa laptopów i notebooków",
        category: "Naprawa",
        description: "Kompleksowy serwis laptopów.",
        image: "/images/services/img2.png"
    },
    {
        id: 3,
        name: "Usuwanie wirusów i malware",
        category: "Oprogramowanie",
        description: "Ochrona i oczyszczanie systemu.",
        image: "/images/services/img3.png"
    },
    {
        id: 4,
        name: "Modernizacja sprzętu komputerowego",
        category: "Modernizacja",
        description: "Zwiększ wydajność swojego komputera.",
        image: "/images/services/img4.png"
    },
    {
        id: 5,
        name: "Naprawa komputerów stacjonarnych",
        category: "Naprawa",
        description: "Diagnostyka i naprawa komputerów PC.",
        image: "/images/services/img1.png"
    },
    {
        id: 6,
        name: "Naprawa laptopów i notebooków",
        category: "Naprawa",
        description: "Kompleksowy serwis laptopów.",
        image: "/images/services/img2.png"
    },
    {
        id: 7,
        name: "Usuwanie wirusów i malware",
        category: "Oprogramowanie",
        description: "Ochrona i oczyszczanie systemu.",
        image: "/images/services/img3.png"
    },
    {
        id: 8,
        name: "Modernizacja sprzętu komputerowego",
        category: "Modernizacja",
        description: "Zwiększ wydajność swojego komputera.",
        image: "/images/services/img4.png"
    }
];

export default function Services() {
    const navigate = useNavigate();
    const [activeCategory, setActiveCategory] = useState("Wszystkie");
    const [showAll, setShowAll] = useState(false);

    const filteredServices = services.filter((service) =>
        activeCategory === "Wszystkie" ||
        service.category === activeCategory
    );

    const visibleServices = showAll
        ? filteredServices
        : filteredServices.slice(0, 4);

    return (
        <section id="services" className="services-section">
            <div className="services-container">

                <div className="services-header">
                    <span className="services-label">
                        NASZA OFERTA
                    </span>

                    <h2>Dostępne usługi serwisowe</h2>

                    <p>
                        Sprawdź, w czym możemy Ci pomóc.
                        Wybierz kategorię i poznaj nasze usługi.
                    </p>
                </div>

                <ServiceFilters
                    categories={categories}
                    activeCategory={activeCategory}
                    onCategoryChange={(category) => {
                        setActiveCategory(category);
                        setShowAll(false);
                    }}
                />

                <div className="services-grid">
                    {visibleServices.map((service) => (
                        <ServiceCard
                            key={service.id}
                            service={service}
                        />
                    ))}
                </div>

                {visibleServices.length === 0 && (
                    <p className="services-empty">
                        Brak usług w wybranej kategorii.
                    </p>
                )}

                {filteredServices.length > 4 && (
                    <div className="services-footer">
                        <Button
                            text={showAll ? "Pokaż mniej" : "Zobacz wszystkie usługi"}
                            variant="primary"
                            onClick={() => setShowAll((prev) => !prev)}
                        />
                    </div>
                )}

            </div>
        </section>
    );
}
