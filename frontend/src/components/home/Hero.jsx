
import { useNavigate } from "react-router-dom";
import Button from "../Button.jsx";
import HeroParticles from "./HeroParticles.jsx";
import "../../styles/components/home/Hero.css";

export default function Hero() {
    const navigate = useNavigate();

    return (
        <section id="home" className="home-hero">
            <div className="home-hero-blur" aria-hidden="true" />

            <HeroParticles />

            <div className="home-hero-content">
                <h1>Profesjonalny serwis komputerowy</h1>

                <div className="home-hero-actions">
                    <Button
                        text="Przeglądaj części"
                        variant="primary"
                        onClick={() =>
                            document.getElementById("services")?.scrollIntoView({
                                behavior: "smooth",
                                block: "start"
                            })
                        }
                    />

                    <Button
                        text="Zgłoś usterkę"
                        variant="outline"
                        onClick={() => navigate("/report")}
                    />
                </div>
            </div>
        </section>
    );
}
