import { useState } from "react";
import {
    Laptop,
    HardDrive,
    ShieldCheck,
    Cpu,
    ChevronDown
} from "lucide-react";
import Card from "./Card.jsx";
import "../../styles/components/home/Specialties.css";

const specialties = [
    {
        title: "Naprawa laptopów",
        description: "Diagnostyka i naprawa usterek laptopów.",
        icon: Laptop
    },
    {
        title: "Dyski i dane",
        description: "Optymalizacja dysków i odzyskiwanie danych.",
        icon: HardDrive
    },
    {
        title: "Oprogramowanie",
        description: "Instalacja systemów i usuwanie wirusów.",
        icon: ShieldCheck
    },
    {
        title: "Modernizacja sprzętu",
        description: "Wymiana i rozbudowa podzespołów.",
        icon: Cpu
    }
];

export default function Specialties() {
    const [active, setActive] = useState([]);

    function toggle(index) {
        setActive((prev) =>
            prev.includes(index)
                ? prev.filter((item) => item !== index)
                : [...prev, index]
        );
    }

    return (
        <section className="specialties">
            <div className="specialties-header">
                <span>NASZE SPECJALIZACJE</span>
                <h2>W czym możemy Ci pomóc?</h2>
                <p>
                    Sprawdź, w jakich obszarach możemy
                    pomóc Twojemu sprzętowi.
                </p>
            </div>

            <div className="specialties-grid">
                {specialties.map((item, index) => (
                    <Card
                        key={item.title}
                        item={item}
                        isActive={active.includes(index)}
                        onClick={() => toggle(index)}
                    />
                ))}
            </div>
        </section>
    );
}
