import { useEffect, useRef } from "react";

export default function HeroParticles() {
    const canvasRef = useRef(null);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
        const hero = canvas.parentElement;

        let particles = [];
        let animationId = null;
        let lastTrail = 0;
        let lastFrame = 0;

        const FPS = 30;
        const frameInterval = 1000 / FPS;
        const MAX_PARTICLES = 80;

        function resize() {
            canvas.width = hero.clientWidth;
            canvas.height = hero.clientHeight;
        }

        function createParticle(x, y, burst = false) {
            const angle = Math.random() * Math.PI * 2;

            const speed = burst
                ? Math.random() * 5 + 2
                : Math.random() * 1.5 + 0.5;

            return {
                x,
                y,
                vx: Math.cos(angle) * speed,
                vy: Math.sin(angle) * speed,
                size: Math.random() * 8 + 4,
                life: 1,
                decay: burst ? 0.035 : 0.06,
                rotation: Math.random() * Math.PI,
                spin: (Math.random() - 0.5) * 0.12,
                type: Math.random() > 0.5
                    ? "square"
                    : "triangle",
            };
        }

        function getPosition(event) {
            const rect = hero.getBoundingClientRect();

            return {
                x: event.clientX - rect.left,
                y: event.clientY - rect.top,
            };
        }

        function addParticle(particle) {
            if (particles.length >= MAX_PARTICLES) {
                particles.shift();
            }

            particles.push(particle);
            startAnimation();
        }

        function handleMove(event) {
            if (event.pointerType === "touch") return;

            const now = performance.now();

            if (now - lastTrail < 60) return;

            lastTrail = now;

            const { x, y } = getPosition(event);

            addParticle(createParticle(x, y));
        }

        function handleClick(event) {
            const { x, y } = getPosition(event);

            for (let i = 0; i < 12; i++) {
                addParticle(createParticle(x, y, true));
            }
        }

        function drawParticle(p) {
            ctx.save();

            ctx.translate(p.x, p.y);
            ctx.rotate(p.rotation);

            ctx.globalAlpha = Math.max(0, p.life);
            ctx.fillStyle = "#93B4FF";
            ctx.shadowColor = "#7395FF";
            ctx.shadowBlur = 6;

            if (p.type === "square") {
                ctx.fillRect(
                    -p.size / 2,
                    -p.size / 2,
                    p.size,
                    p.size
                );
            } else {
                ctx.beginPath();
                ctx.moveTo(0, -p.size / 2);
                ctx.lineTo(-p.size / 2, p.size / 2);
                ctx.lineTo(p.size / 2, p.size / 2);
                ctx.closePath();
                ctx.fill();
            }

            ctx.restore();
        }

        function animate(timestamp) {
            if (timestamp - lastFrame < frameInterval) {
                animationId = requestAnimationFrame(animate);
                return;
            }

            lastFrame = timestamp;

            ctx.clearRect(
                0,
                0,
                canvas.width,
                canvas.height
            );

            particles = particles.filter(p => p.life > 0);

            if (particles.length === 0) {
                animationId = null;
                return;
            }

            particles.forEach(p => {
                p.x += p.vx;
                p.y += p.vy;
                p.rotation += p.spin;
                p.life -= p.decay;

                drawParticle(p);
            });

            animationId = requestAnimationFrame(animate);
        }

        function startAnimation() {
            if (animationId !== null) return;

            lastFrame = 0;
            animationId = requestAnimationFrame(animate);
        }

        resize();

        const resizeObserver = new ResizeObserver(resize);
        resizeObserver.observe(hero);

        hero.addEventListener("pointermove", handleMove);
        hero.addEventListener("click", handleClick);

        return () => {
            hero.removeEventListener("pointermove", handleMove);
            hero.removeEventListener("click", handleClick);

            resizeObserver.disconnect();

            if (animationId !== null) {
                cancelAnimationFrame(animationId);
            }
        };
    }, []);

    return (
        <canvas
            ref={canvasRef}
            className="home-hero-particles"
            aria-hidden="true"
        />
    );
}
