import {
    ArrowDown,
    CircleDollarSign,
    Clock3,
    ShieldCheck,
    Wrench
} from "lucide-react";

import "../../styles/components/services/ServiceDetailsHero.css";

export default function ServiceDetailsHero({ service }) {
    return (
        <section className="details-hero">
            <div className="details-hero-content">

                <div className="details-hero-badge">
                    <Wrench size={18} />
                    <span>Usługa serwisowa</span>
                </div>

                <h1>{service.name}</h1>
                <p>{service.description}</p>

                <div className="details-hero-meta">
                    <div className="details-meta-item">
                        <CircleDollarSign size={32} />
                        <div>
                            <span>Cena</span>
                            <strong>{service.price}</strong>
                        </div>
                    </div>

                    <div className="details-meta-item">
                        <Clock3 size={32} />
                        <div>
                            <span>Czas realizacji</span>
                            <strong>{service.duration}</strong>
                        </div>
                    </div>

                    <div className="details-meta-item">
                        <ShieldCheck size={32} />
                        <div>
                            <span>Gwarancja</span>
                            <strong>{service.warranty}</strong>
                        </div>
                    </div>
                </div>

            </div>

            <div className="details-hero-image">
                <img
                    src={service.image}
                    alt={service.name}
                />
            </div>
        </section>
    );
}
