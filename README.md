# TechFix: serwis komputerowy

Aplikacja webowa serwisu komputerowego: usługi, rezerwacje, zakupy.
Backend w Javie (Servlety na Tomcacie, SQLite), frontend w React (Vite).

---

## ⚡ Najszybszy start (5 minut)

Potrzebujesz tylko **Dockera** (Docker Desktop na Windows/Mac).

```bash
git clone https://github.com/Dominik45578/techfix-computer-service.git
cd techfix-computer-service
cp .env.example .env          # potem zmień ADMIN_PASSWORD w .env
docker compose up -d --build
```

Otwórz w przeglądarce:

| Co               | Adres                   |
|------------------|-------------------------|
| Aplikacja        | http://localhost:8090   |
| Podgląd bazy     | http://localhost:8091   |

Logujesz się danymi z `.env` (`ADMIN_EMAIL` / `ADMIN_PASSWORD`).

> Pierwsze uruchomienie trwa kilka minut, bo Docker pobiera obrazy i buduje projekt.

---

## 🧭 Ściąga z komend

| Chcę...                          | Komenda                         |
|----------------------------------|---------------------------------|
| uruchomić / przebudować          | `docker compose up -d --build`  |
| zatrzymać                        | `docker compose down`           |
| zobaczyć logi                    | `docker compose logs -f`        |
| **wyczyścić bazę** od zera       | `docker compose down -v`        |

Masz `make`? Są też skróty: `make run`, `make down`, `make logs`.
(`make run` buduje dodatkowo lokalnie, więc wymaga Mavena i Node.)

---

## ⚙️ Konfiguracja (`.env`)

Wszystko jest opisane w [`.env.example`](.env.example). W skrócie:

| Zmienna           | Po co                                    | Domyślnie     |
|-------------------|------------------------------------------|---------------|
| `ADMIN_EMAIL`     | login pierwszego admina                  | admin@example.com |
| `ADMIN_PASSWORD`  | hasło admina (**min. 12 znaków**)        | zmień!        |
| `DANE_TESTOWE`    | `true` = wgraj przykładowe dane          | `true`        |
| `FRONTEND_PORT`   | port aplikacji                           | `8090`        |
| `SQLITE_WEB_PORT` | port podglądu bazy                       | `8091`        |

⚠️ Admin tworzy się **tylko przy pustej bazie**. Zmieniłeś hasło w `.env`
i nie działa? Wyczyść bazę: `docker compose down -v`.

---

## 🛠️ Praca nad kodem (bez Dockera)

Przydaje się, gdy chcesz szybko widzieć zmiany.

**Potrzebujesz:** JDK 17, Maven, Node 22+, Tomcat 10.1.

**1. Backend**
- Zbuduj: `mvn -f backend/pom.xml package` (wychodzi `backend/target/serwis.war`)
- Wdróż na Tomcat 10.1, port `8080`, context path `/`
- W IntelliJ jest gotowa konfiguracja **Tomcat TI-Projekt** (wystarczy wskazać swój Tomcat)
- Zmienne `ADMIN_EMAIL`, `ADMIN_PASSWORD`, `DANE_TESTOWE` ustaw w konfiguracji
  uruchomieniowej. **Tomcat nie czyta pliku `.env`.**

**2. Frontend**
```bash
cd frontend
npm install
npm run dev        # http://localhost:5173
```
Vite sam przekazuje `/logowanie`, `/uslugi` i `/wyloguj` do backendu na `:8080`.

---

## 📁 Co gdzie jest

```
backend/     Java: servlety, repozytoria (JDBC), migracje bazy (Flyway)
  src/main/java/pk/wieik/ti/
    auth/        logowanie, hasła
    db/          połączenie z bazą
    model/       rekordy (User, Service, Reservation...)
    repository/  zapytania SQL
    web/         servlety (adresy URL)
  src/main/resources/db/
    migration/      schemat bazy (V1__..., V2__...)
    dane-testowe/   przykładowe dane
frontend/    React + Vite
docker-compose.yml   cały stack: backend, frontend (nginx), podgląd bazy
```

---

## 📏 Zasady w projekcie

- **Baza:** tylko JDBC + HikariCP + Flyway. **Bez Hibernate/JPA.**
- **Zmiana schematu bazy = nowa migracja** `V<numer>__opis.sql`.
  Nigdy nie edytuj migracji, która już jest w repo.
- **Modele** to `record`, repozytoria zwracają `Optional`.
- **`.env` nie trafia do gita.** Nowa zmienna? Dopisz ją do `.env.example` z opisem.

---

## 🆘 Coś nie działa?

| Objaw                                            | Co zrobić                                         |
|--------------------------------------------------|---------------------------------------------------|
| `port is already allocated`                      | zmień `FRONTEND_PORT` / `SQLITE_WEB_PORT` w `.env` |
| backend się nie uruchamia, "Ustaw oba parametry" | ustaw **oba** `ADMIN_EMAIL` i `ADMIN_PASSWORD`, albo żadnego |
| nie mogę się zalogować                           | hasło < 12 znaków albo baza już istniała, wtedy `docker compose down -v` |
| frontend nie startuje (czeka na backend)         | `docker compose logs backend`, błąd jest w logach |
| gdzie leży baza w trybie dev?                    | backend wypisuje ścieżkę przy starcie: `SQLite: ...` |
