import { ArrowUpRight } from "lucide-react";
import { useNavigate } from "react-router-dom";
import "../../styles/components/home/ServiceCard.css";

export default function ServiceCard({ service }) {
    const navigate = useNavigate();

    return (
        <article
            className="service-card"
            onClick={() => navigate(`/services/${service.id}`, {
                state: { service }
            })}
        >
            <div className="service-card-image">
                <img
                    src={service.image}
                    alt={service.name}
                />
            </div>

            <div className="service-card-content">
                <span className="service-card-category">
                    {service.category}
                </span>

                <h3>{service.name}</h3>

                <p>{service.description}</p>

                <div className="service-card-bottom">
                    <span>Szczegóły usługi</span>
                    <ArrowUpRight size={19} />
                </div>
            </div>
        </article>
    );
}
