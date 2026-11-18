import { useNavigate } from "react-router-dom";
import Hero from "../components/home/Hero.jsx"
import Specialties from "../components/home/Specialties.jsx"
import  Services from "../components/home/Services.jsx"
import "../styles/pages/Home.css";

export function Home() {
    const navigate = useNavigate();

    return (
        <div className="home-page">
            <Hero />
            <Specialties />
            <Services />
        </div>
    );
}