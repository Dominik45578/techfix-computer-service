import React from 'react';
import '../styles/components/Button.css';

export default function Button({ text, type = "button", variant = "primary", onClick }) {
    return (
        <button
            type={type}
            className={`btn-${variant}`}
            onClick={onClick}
        >
            {text}
        </button>
    );
}