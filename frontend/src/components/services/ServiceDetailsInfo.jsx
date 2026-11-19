import { Check } from "lucide-react";
import ServiceProcess from "./ServiceProcces.jsx";

import "../../styles/components/services/ServiceDetailsInfo.css";

export default function ServiceDetailsInfo({ service, selectedScope , onScopeSelect }) {
    return (
        <section className="details-info">
            <h2>Zakres usługi</h2>

            <ul className="details-scope">
                {service.scope.map((item) => (
                    <li key={item}>
                        <button
                            type="button"
                            className={`details-scope-button ${
                                selectedScope === item ? "selected" : ""
                            }`}
                            onClick={() => {
                                onScopeSelect(item);

                                document
                                    .getElementById("service-order")
                                    ?.scrollIntoView({
                                        behavior: "smooth",
                                        block: "center"
                                    });
                            }}
                        >
                <span className="details-scope-icon">
                    <Check size={18} />
                </span>

                            <span>{item}</span>
                        </button>
                    </li>
                ))}
            </ul>

            <div className="details-info-divider" />

            <h2>Przebieg realizacji</h2>
            <p className="details-process-description">
                Zobacz, jak krok po kroku przebiega realizacja usługi.
            </p>

            <ServiceProcess />
        </section>
    );
}
