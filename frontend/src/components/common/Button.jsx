import '../../styles/components/common/Button.css';

export default function Button({ text, type = "button", variant = "primary", onClick , icon:Icon,  disabled = false , ariaLabel}) {
    return (
        <button
            type={type}
            className={`btn-${variant}`}
            onClick={onClick}
            disabled={disabled}
            aria-label={ariaLabel}
        >
            {Icon && <Icon size={18} />}
            {text}
        </button>
    );
}