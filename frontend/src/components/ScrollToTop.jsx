import { useEffect } from "react";
import { useLocation } from "react-router-dom";

export default function ScrollToTop() {
    const { pathname, hash, search } = useLocation();

    useEffect(() => {
        if (!hash) {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        }
    }, [pathname, hash, search]);

    return null;
}