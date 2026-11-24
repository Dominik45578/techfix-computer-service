import { useState, useRef, useEffect } from "react";
import { ChevronDown, Check } from "lucide-react";
import "../../styles/components/common/Select.css";

export default function Select({label, id, value, onChange, options = [], placeholder = "Wybierz opcję",
                                   required = false, disabled = false, variant = "default", className = ""
                               }) {
    const [isOpen, setIsOpen] = useState(false);
    const selectRef = useRef(null);

    const normalizedOptions = options.map((option) =>
        typeof option === "string"
            ? { value: option, label: option }
            : option
    );

    const selectedOption = normalizedOptions.find(
        (option) => String(option.value) === String(value)
    );

    useEffect(() => {
        function handleClickOutside(event) {
            if (!selectRef.current?.contains(event.target)) {
                setIsOpen(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    function handleSelect(optionValue) {
        onChange?.({
            target: {
                value: optionValue
            }
        });

        setIsOpen(false);
    }

    function handleKeyDown(event) {
        if (event.key === "Escape") {
            setIsOpen(false);
        }

        if (event.key === "ArrowDown" && !isOpen) {
            event.preventDefault();
            setIsOpen(true);
        }
    }

    return (
        <div
            className={`select-field select-${variant}`}
            ref={selectRef}
        >
            {label && <label id={`${id}-label`}>{label}</label>}

            <div className={`select-wrapper ${isOpen ? "open" : ""}`}>
                <button
                    id={id}
                    type="button"
                    className={`select-trigger ${
                        !selectedOption ? "placeholder" : ""
                    } ${className}`}
                    onClick={() => setIsOpen(prev => !prev)}
                    onKeyDown={handleKeyDown}
                    disabled={disabled}
                    aria-haspopup="listbox"
                    aria-expanded={isOpen}
                    aria-labelledby={label ? `${id}-label ${id}` : undefined}
                >
                    <span>
                        {selectedOption?.label || placeholder}
                    </span>

                    <ChevronDown
                        size={variant === "badge" ? 14 : 20}
                        className="select-arrow"
                    />
                </button>

                {isOpen && (
                    <div
                        className="select-dropdown"
                        role="listbox"
                        aria-labelledby={label ? `${id}-label` : undefined}
                    >
                        {normalizedOptions.map((option) => (
                            <button
                                key={option.value}
                                type="button"
                                role="option"
                                aria-selected={
                                    String(value) === String(option.value)
                                }
                                className={`select-option ${
                                    String(value) === String(option.value)
                                        ? "selected"
                                        : ""
                                }`}
                                onClick={() => handleSelect(option.value)}
                            >
                                <span>{option.label}</span>

                                {String(value) === String(option.value) && (
                                    <Check size={variant === "badge" ? 14 : 18} />
                                )}
                            </button>
                        ))}
                    </div>
                )}
            </div>

            <input
                type="hidden"
                name={id}
                value={value ?? ""}
                required={required}
                disabled={disabled}
            />
        </div>
    );
}