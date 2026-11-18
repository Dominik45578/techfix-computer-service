import "../styles/components/Input.css";

export function Input({label, type = "text", name, value, onChange, placeholder, required = false}) {
    return (
        <div className="input-group">
            <label htmlFor={name}>{label}</label>

            <input
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
            />
        </div>
    );
}
