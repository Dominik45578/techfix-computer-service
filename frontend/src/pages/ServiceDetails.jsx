import { useLocation , useParams } from "react-router-dom";
import { useState } from "react";
import ServiceDetailsHero from "../components/services/ServiceDetailsHero.jsx"
import ServiceDetailsInfo from "../components/services/ServiceDetailsInfo.jsx"
import ServiceOrderForm from "../components/services/ServiceOrderForm.jsx"

import "../styles/pages/ServiceDetails.css";

export function ServiceDetails() {
    const [selectedScope, setSelectedScope] = useState("");
    const { id } = useParams();
    const { state } = useLocation();

    const service = state?.service;

    if(!service){
        return <p>Nie znaleziono usługi</p>
    }

    return (
        <div className="service-details-page">
            <div className="service-details-container">
                <ServiceDetailsHero service={service}/>
                <div className="service-details-grid">
                    <ServiceDetailsInfo
                        service={service}
                        selectedScope={selectedScope}
                        onScopeSelect={setSelectedScope}
                    />

                    <ServiceOrderForm
                        service={service}
                        selectedScope={selectedScope}
                        setSelectedScope={setSelectedScope}
                    />
                </div>

            </div>
        </div>
    );
}
