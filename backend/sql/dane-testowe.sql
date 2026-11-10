-- Uruchom raz w konsoli SQL IntelliJ IDEA, na właściwej bazie biblioteka.db
PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO autorzy (imie, nazwisko) VALUES ('Stanisław', 'Lem');
INSERT OR IGNORE INTO autorzy (imie, nazwisko) VALUES ('George', 'Orwell');
INSERT OR IGNORE INTO wydawcy (nazwa) VALUES ('Wydawnictwo MON');
INSERT OR IGNORE INTO wydawcy (nazwa) VALUES ('Secker & Warburg');

INSERT INTO ksiazki (autor_id, tytul_pl, tytul_en, rok_wydania, wydawca_id)
SELECT a.id, 'Solaris', 'Solaris', 1961, w.id
FROM autorzy a, wydawcy w
WHERE a.imie = 'Stanisław' AND a.nazwisko = 'Lem'
  AND w.nazwa = 'Wydawnictwo MON'
  AND NOT EXISTS (SELECT 1 FROM ksiazki WHERE tytul_pl = 'Solaris' AND rok_wydania = 1961);

INSERT INTO ksiazki (autor_id, tytul_pl, tytul_en, rok_wydania, wydawca_id)
SELECT a.id, 'Rok 1984', 'Nineteen Eighty-Four', 1949, w.id
FROM autorzy a, wydawcy w
WHERE a.imie = 'George' AND a.nazwisko = 'Orwell'
  AND w.nazwa = 'Secker & Warburg'
  AND NOT EXISTS (SELECT 1 FROM ksiazki WHERE tytul_pl = 'Rok 1984' AND rok_wydania = 1949);
