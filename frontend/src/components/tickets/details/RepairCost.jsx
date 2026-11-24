import { useState } from "react";
import { Calculator, Plus, Trash2 } from "lucide-react";
import Button from "../../common/Button.jsx";
import Input from "../../common/Input.jsx";
import "../../../styles/components/serviceTickets/details/RepairCost.css";

export default function RepairCost({ onSave, totalCost }) {

    const [isOpen, setIsOpen] = useState(false);

    const [items, setItems] = useState([
        {
            id: 1,
            name: "",
            quantity: 1,
            unitPrice: ""
        }
    ]);

    const total = items.reduce((sum, item) => {
        return sum + Number(item.quantity)
            * Math.round(Number(item.unitPrice) * 100);
    }, 0) / 100;

    const isValid = items.length > 0 && items.every(item =>
        item.name.trim() !== "" &&
        Number.isInteger(Number(item.quantity)) &&
        Number(item.quantity) > 0 &&
        item.unitPrice !== "" &&
        Number.isFinite(Number(item.unitPrice)) &&
        Number(item.unitPrice) >= 0
    );

    const handleAddItem = () => {
        setItems(prev => [
            ...prev,
            {
                id: crypto.randomUUID(),
                name: "",
                quantity: 1,
                unitPrice: ""
            }
        ]);
    };

    const handleRemoveItem = (id) => {
        setItems(prev => prev.filter(item => item.id !== id));
    };

    const handleItemChange = (id, field, value) => {
        setItems(prev => prev.map(item =>
            item.id === id
                ? { ...item, [field]: value }
                : item
        ));
    };

    const handleSave = () => {
        if (!isValid || totalCost != null) return;

        onSave(items, total);
        setIsOpen(false);
    };

    return (
        <div className="ticket-details-panel">

            <div className="ticket-panel-title">
                <Calculator size={20} />
                <h2>Koszty naprawy</h2>
            </div>

            {!isOpen ? (
                <div className="repair-cost-intro">

                    {totalCost != null ? (
                        <p>
                            Zapisany koszt naprawy:{" "}
                            <strong>
                                {Number(totalCost).toFixed(2)} zł
                            </strong>
                        </p>
                    ) : (
                        <>
                            <p>
                                Na samym końcu realizacji zgłoszenia
                                dodaj rozliczenie wykonanych prac.
                            </p>

                            <div className="repair-cost-actions">
                                <Button
                                    text="Dodaj rozliczenie"
                                    variant="primary"
                                    onClick={() => setIsOpen(true)}
                                />
                            </div>
                        </>
                    )}

                </div>
            ) : (
                <div className="repair-cost-form">

                    {items.map((item, index) => (
                        <div
                            className="repair-cost-row"
                            key={item.id}
                        >
                            <Input
                                label={index === 0 ? "Nazwa" : undefined}
                                name={`name-${item.id}`}
                                placeholder="Nazwa pozycji"
                                value={item.name}
                                onChange={event =>
                                    handleItemChange(
                                        item.id,
                                        "name",
                                        event.target.value
                                    )
                                }
                            />

                            <Input
                                label={index === 0 ? "Ilość" : undefined}
                                name={`quantity-${item.id}`}
                                type="number"
                                min={1}
                                step={1}
                                value={item.quantity}
                                onChange={event =>
                                    handleItemChange(
                                        item.id,
                                        "quantity",
                                        event.target.value
                                    )
                                }
                            />

                            <Input
                                label={index === 0 ? "Cena jedn. (zł)" : undefined}
                                name={`price-${item.id}`}
                                type="number"
                                min={0}
                                step={0.01}
                                placeholder="0,00"
                                value={item.unitPrice}
                                onChange={event =>
                                    handleItemChange(
                                        item.id,
                                        "unitPrice",
                                        event.target.value
                                    )
                                }
                            />

                            <button
                                type="button"
                                className="repair-cost-remove"
                                aria-label="Usuń pozycję"
                                disabled={items.length === 1}
                                onClick={() =>
                                    handleRemoveItem(item.id)
                                }
                            >
                                <Trash2 size={18} />
                            </button>
                        </div>
                    ))}

                    <button
                        type="button"
                        className="repair-cost-add"
                        onClick={handleAddItem}
                    >
                        <Plus size={18} />
                        Dodaj pozycję
                    </button>

                    <div className="repair-cost-total">
                        <span>Całkowity koszt:</span>
                        <strong>{total.toFixed(2)} zł</strong>
                    </div>

                    <div className="repair-cost-actions">
                        <Button
                            text="Anuluj"
                            variant="outline"
                            onClick={() => setIsOpen(false)}
                        />

                        <Button
                            text="Zapisz rozliczenie"
                            variant="primary"
                            onClick={handleSave}
                            disabled={!isValid}
                        />
                    </div>

                </div>
            )}

        </div>
    );
}