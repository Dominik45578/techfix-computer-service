import "../../styles/components/common/TextArea.css";

export default function TextArea({label, id, value, onChange, placeholder, rows = 4, required = false, minLength, disabled = false}) {
    return (
        <div className="textarea-field">
            {label && <label htmlFor={id}>{label}</label>}

            <textarea
                id={id}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                rows={rows}
                required={required}
                minLength={minLength}
                disabled={disabled}
            />
        </div>
    );
}
