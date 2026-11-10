-- V1: schemat początkowy (tabele z kroku 03/04 skryptu TI)

CREATE TABLE uzytkownicy (
    id INTEGER PRIMARY KEY,
    login TEXT NOT NULL UNIQUE COLLATE NOCASE,
    haslo TEXT NOT NULL,
    uprawnienia INTEGER NOT NULL DEFAULT 0
        CHECK (uprawnienia IN (0, 1, 2))
);

CREATE TABLE autorzy (
    id INTEGER PRIMARY KEY,
    imie TEXT NOT NULL,
    nazwisko TEXT NOT NULL,
    UNIQUE (imie, nazwisko)
);

CREATE TABLE wydawcy (
    id INTEGER PRIMARY KEY,
    nazwa TEXT NOT NULL UNIQUE
);

CREATE TABLE ksiazki (
    id INTEGER PRIMARY KEY,
    autor_id INTEGER NOT NULL,
    tytul_pl TEXT NOT NULL,
    tytul_en TEXT NOT NULL,
    rok_wydania INTEGER NOT NULL CHECK (rok_wydania >= 0),
    wydawca_id INTEGER NOT NULL,
    FOREIGN KEY (autor_id) REFERENCES autorzy(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (wydawca_id) REFERENCES wydawcy(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX idx_ksiazki_autor ON ksiazki(autor_id);
CREATE INDEX idx_ksiazki_wydawca ON ksiazki(wydawca_id);
