import "../styles/components/Input.css";

export default function Input({label, type = "text", name, value, onChange, placeholder,disabled = false, required = false}) {
    return (
        <div className="input-group">
            <label htmlFor={name}>{label}</label>

            <input
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                disabled={disabled}
                placeholder={placeholder}
                required={required}
            />
        </div>
    );
}
