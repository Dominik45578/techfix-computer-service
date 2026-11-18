import "../../styles/components/home/ServicesFilters.css";

export default function ServiceFilters({categories, activeCategory, onCategoryChange}) {
    return (
        <div className="service-filters">
            {categories.map((category) => (
                <button
                    key={category}
                    type="button"
                    className={`service-filter ${
                        activeCategory === category ? "active" : ""
                    }`}
                    onClick={() => onCategoryChange(category)}
                    aria-pressed={activeCategory === category}
                >
                    {category}
                </button>
            ))}
        </div>
    );
}
