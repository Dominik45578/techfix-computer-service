import { useState } from "react";
import {ClipboardPlus, ArrowRight, ShieldCheck} from "lucide-react";

import Button from "../common/Button.jsx";
import TextArea from "../common/TextArea.jsx";
import Select from "../common/Select.jsx";
import Input from "../common/Input.jsx";

import "../../styles/components/services/ServiceOrderForm.css";

export default function ServiceOrderForm({service, selectedScope,setSelectedScope}) {
    const [description, setDescription] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const [device, setDevice] = useState("");

    function handleSubmit(event) {
        event.preventDefault();

        const request = {
            serviceId: service.id,
            issueType: selectedScope,
            device: device.trim(),
            description: description.trim()
        };

        if (
            !request.issueType ||
            !request.device ||
            request.description.length < 10
        ) {
            return;
        }

        console.log("Zgłoszenie:", request);
        setSubmitted(true);
    }

    return (
        <section id="service-order" className="service-order-form">

            <div className="order-form-heading">
                <div className="order-form-heading-icon">
                    <ClipboardPlus size={28} />
                </div>

                <div>
                    <h2>Zamów usługę</h2>
                    <span className="order-form-subtitle">
                        Szybkie zgłoszenie online
                    </span>
                </div>
            </div>

            <p className="order-form-description">
                Opisz problem ze swoim sprzętem, a skontaktujemy się
                z Tobą w celu ustalenia szczegółów naprawy.
            </p>

            <form onSubmit={handleSubmit}>
                <Select
                    id="services"
                    label="Zakres zgłoszenia"
                    value={selectedScope}
                    onChange={(event) => {
                        setSelectedScope(event.target.value);
                        setSubmitted(false);
                    }}
                    options={service.scope}
                    placeholder="Wybierz zakres zgłoszenia"
                    required
                />

                <Input
                    label="Urządzenie"
                    type="text"
                    name="device"
                    value={device}
                    onChange={(event) => {
                        setDevice(event.target.value);
                        setSubmitted(false);
                    }}
                    placeholder="Np. Dell Inspiron 15"
                    required
                />

                <TextArea
                    id="description"
                    label="Opis problemu"
                    value={description}
                    onChange={(event) => {
                        setDescription(event.target.value);
                        setSubmitted(false);
                    }}
                    placeholder="Opisz, co dzieje się z Twoim sprzętem..."
                    rows={5}
                    minLength={10}
                    required
                />

                <div className="order-form-submit">
                    <Button
                        text="Zgłoś usterkę"
                        variant="primary"
                        type="submit"
                    />
                </div>

                <div className="order-form-note">
                    <ShieldCheck size={18} />
                    <span>
                        Wysłanie zgłoszenia nie wiąże się
                        z obowiązkiem zapłaty.
                    </span>
                </div>

                {submitted && (
                    <p className="order-form-message" role="status">
                        Formularz został poprawnie wypełniony.
                        Zgłoszenie nie zostało jeszcze wysłane.
                    </p>
                )}
            </form>
        </section>
    );
}
