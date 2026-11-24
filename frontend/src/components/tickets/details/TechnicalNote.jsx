import { useState } from "react";
import { NotebookPen } from "lucide-react";
import Button from "../../common/Button.jsx";
import TextArea from "../../common/TextArea.jsx";
import "../../../styles/components/serviceTickets/details/TechnicalNote.css";

export default function TechnicianNote({ onAddNote }) {
    const [note, setNote] = useState("");

    const handleSave = () => {
        if (!note.trim()) return;

        onAddNote(note.trim());
        setNote("");
    };

    return (
        <div className="ticket-details-panel">
            <div className="ticket-panel-title">
                <NotebookPen size={20} />
                <h2>Notatka serwisanta</h2>
            </div>

            <div className="technician-note-form">
                <TextArea
                    id="technician-note"
                    placeholder="Wprowadź informacje dotyczące naprawy..."
                    value={note}
                    onChange={event => setNote(event.target.value)}
                    rows={6}
                />

                <div className="technician-note-actions">
                    <Button
                        text="Zapisz notatkę"
                        variant="primary"
                        onClick={handleSave}
                        disabled={!note.trim()}
                    />
                </div>
            </div>
        </div>
    );
}