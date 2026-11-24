import "../../styles/components/common/Input.css";

export default function Input({label, type = "text", name, value, onChange, placeholder, disabled = false, required = false, min, max, step}) {
    return (
        <div className="input-group">
            {label && <label htmlFor={name}>{label}</label>}

            <input
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                disabled={disabled}
                placeholder={placeholder}
                required={required}
                min={min}
                max={max}
                step={step}
            />
        </div>
    );
}