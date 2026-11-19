import {
    ClipboardList,
    SearchCheck,
    Wrench,
    PackageCheck
} from "lucide-react";

import "../../styles/components/services/ServiceProcess.css";

const steps = [
    { title: "Zgłoszenie", icon: ClipboardList },
    { title: "Diagnostyka", icon: SearchCheck },
    { title: "Naprawa", icon: Wrench },
    { title: "Odbiór", icon: PackageCheck }
];

export default function ServiceProcess() {
    return (
        <div className="service-process">
            {steps.map((step, index) => {
                const Icon = step.icon;

                return (
                    <div className="process-item" key={step.title}>
                        <div className="process-step">
                            <div className="process-step-icon">
                                <Icon size={32} strokeWidth={1.8} />
                            </div>

                            <span className="process-step-number">
                                KROK {String(index + 1).padStart(2, "0")}
                            </span>

                            <span className="process-step-title">
                                {step.title}
                            </span>
                        </div>
                    </div>
                );
            })}
        </div>
    );
}
