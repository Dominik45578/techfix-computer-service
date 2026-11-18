-- Dane testowe (uruchamiane przy DANE_TESTOWE=true). Skrypt jest idempotentny.

INSERT INTO device_categories (device_name, description)
SELECT 'Laptop', 'Laptopy i notebooki'
WHERE NOT EXISTS (SELECT 1 FROM device_categories WHERE device_name = 'Laptop');

INSERT INTO device_categories (device_name, description)
SELECT 'Smartfon', 'Telefony komórkowe'
WHERE NOT EXISTS (SELECT 1 FROM device_categories WHERE device_name = 'Smartfon');

INSERT INTO device_categories (device_name, description)
SELECT 'Komputer stacjonarny', 'Komputery PC typu desktop'
WHERE NOT EXISTS (SELECT 1 FROM device_categories WHERE device_name = 'Komputer stacjonarny');

INSERT INTO service_kind (kind_name, description)
SELECT 'Naprawa', 'Naprawa uszkodzonego sprzętu'
WHERE NOT EXISTS (SELECT 1 FROM service_kind WHERE kind_name = 'Naprawa');

INSERT INTO service_kind (kind_name, description)
SELECT 'Konserwacja', 'Czyszczenie i przegląd okresowy'
WHERE NOT EXISTS (SELECT 1 FROM service_kind WHERE kind_name = 'Konserwacja');

INSERT INTO service_kind (kind_name, description)
SELECT 'Diagnostyka', 'Ocena stanu technicznego urządzenia'
WHERE NOT EXISTS (SELECT 1 FROM service_kind WHERE kind_name = 'Diagnostyka');

INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                      base_cost, estimated_duration_minutes, is_active)
SELECT c.id, 'Wymiana matrycy', k.id, 'Wymiana uszkodzonego ekranu laptopa', 450.00, 90, 1
FROM device_categories c, service_kind k
WHERE c.device_name = 'Laptop' AND k.kind_name = 'Naprawa'
  AND NOT EXISTS (SELECT 1 FROM services WHERE service_name = 'Wymiana matrycy');

INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                      base_cost, estimated_duration_minutes, is_active)
SELECT c.id, 'Czyszczenie układu chłodzenia', k.id, 'Wyczyszczenie wentylatorów i wymiana pasty termoprzewodzącej', 120.00, 60, 1
FROM device_categories c, service_kind k
WHERE c.device_name = 'Laptop' AND k.kind_name = 'Konserwacja'
  AND NOT EXISTS (SELECT 1 FROM services WHERE service_name = 'Czyszczenie układu chłodzenia');

INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                      base_cost, estimated_duration_minutes, is_active)
SELECT c.id, 'Wymiana baterii', k.id, 'Wymiana zużytej baterii w smartfonie', 150.00, 45, 1
FROM device_categories c, service_kind k
WHERE c.device_name = 'Smartfon' AND k.kind_name = 'Naprawa'
  AND NOT EXISTS (SELECT 1 FROM services WHERE service_name = 'Wymiana baterii');

INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                      base_cost, estimated_duration_minutes, is_active)
SELECT c.id, 'Pełna diagnostyka PC', k.id, 'Testy podzespołów i raport z diagnozy', 80.00, 60, 1
FROM device_categories c, service_kind k
WHERE c.device_name = 'Komputer stacjonarny' AND k.kind_name = 'Diagnostyka'
  AND NOT EXISTS (SELECT 1 FROM services WHERE service_name = 'Pełna diagnostyka PC');
