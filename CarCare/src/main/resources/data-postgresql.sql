SELECT setval(pg_get_serial_sequence('car_brands', 'id'), COALESCE((SELECT MAX(id) FROM car_brands), 1), true);
SELECT setval(pg_get_serial_sequence('cars', 'id'), COALESCE((SELECT MAX(id) FROM cars), 1), true);
SELECT setval(pg_get_serial_sequence('car_options', 'id'), COALESCE((SELECT MAX(id) FROM car_options), 1), true);
SELECT setval(pg_get_serial_sequence('centers', 'id'), COALESCE((SELECT MAX(id) FROM centers), 1), true);
SELECT setval(pg_get_serial_sequence('schedule_drive', 'id'), COALESCE((SELECT MAX(id) FROM schedule_drive), 1), true);
