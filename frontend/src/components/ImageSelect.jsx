
import { useState } from "react";
import { ChevronDown, Check } from "lucide-react";

import "../styles/components/ImageSelect.css";

export default function ImageSelect({label, options = [], value, onChange, placeholder = "Wybierz opcję" }) {
    const [isOpen, setIsOpen] = useState(false);

    const selectedOption = options.find(
        (option) => option.id === value
    );

    function handleSelect(option) {
        onChange(option.id);
        setIsOpen(false);
    }

    return (
        <div className="image-select">
            {label && <label>{label}</label>}

            <button
                type="button"
                className="image-select-trigger"
                onClick={() => setIsOpen(!isOpen)}
                aria-expanded={isOpen}
            >
                <span>
                    {selectedOption
                        ? selectedOption.name
                        : placeholder}
                </span>

                <ChevronDown
                    size={20}
                    className={isOpen ? "image-select-arrow open" : "image-select-arrow"}
                />
            </button>

            {isOpen && (
                <div className="image-select-options">
                    {options.map((option) => (
                        <button
                            type="button"
                            key={option.id}
                            className="image-select-option"
                            onClick={() => handleSelect(option)}
                        >
                            <div className="image-select-image">
                                <img src={option.image} alt="" />
                            </div>

                            <div className="image-select-option-content">
                                <strong>{option.name}</strong>
                                <span>{option.description}</span>
                            </div>

                            {value === option.id && (
                                <Check size={20} />
                            )}
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
}
