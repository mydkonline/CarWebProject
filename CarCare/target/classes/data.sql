INSERT INTO admin (id, username, password) VALUES
('admin-seed-001', 'admin', 'admin123');

INSERT INTO car_brands (id, name) VALUES
(1, 'MotionVolt'),
(2, 'Lexus'),
(3, 'Hyundai');

INSERT INTO cars (id, car_brand_id, name) VALUES
(1, 1, 'LC500h'),
(2, 1, 'UX250hFSPORT'),
(3, 1, 'LS500'),
(4, 2, 'ES300h'),
(5, 2, 'NX450h'),
(6, 3, 'RZ450e');

INSERT INTO car_options (id, car_id, color, cc, km, price, grade) VALUES
(1, 1, 'Sonic Iridium', 3456, 14, 91000.00, 'Premium Hybrid'),
(2, 1, 'Deep Blue Mica', 3456, 13, 94000.00, 'Executive'),
(3, 2, 'Heat Blue Contrast', 1987, 17, 56000.00, 'F Sport'),
(4, 2, 'Graphite Black', 1987, 16, 54000.00, 'Urban'),
(5, 3, 'Sonic Quartz', 3445, 12, 88000.00, 'Luxury'),
(6, 4, 'Lunar Luster', 2487, 18, 62000.00, 'Hybrid'),
(7, 5, 'Sonic Titanium', 2487, 15, 74000.00, 'Overtrail'),
(8, 6, 'Ether Metallic', 0, 0, 69000.00, 'Electric');

INSERT INTO centers (id, name, address, number) VALUES
(1, 'MotionVolt 강남 센터', '서울특별시 강남구 테헤란로 152', '02-600-6000'),
(2, 'MotionVolt 용산 센터', '서울특별시 용산구 한강대로 100', '02-700-7000'),
(3, 'MotionVolt 부산 센터', '부산광역시 해운대구 센텀중앙로 55', '051-800-8000'),
(4, 'MotionVolt 대구 센터', '대구광역시 수성구 동대구로 210', '053-900-9000');

INSERT INTO kakaouserinfos (id, nickname, connected_at) VALUES
(9000000001, '샘플예약자', TIMESTAMP '2026-04-18 09:00:00'),
(9000000002, '김모션', TIMESTAMP '2026-04-18 10:00:00'),
(9000000003, '박볼트', TIMESTAMP '2026-04-18 11:00:00');

INSERT INTO schedule_drive (id, center_id, kakaouser_id, car_option_id, reservation_date, state) VALUES
(1, 1, 9000000001, 1, DATE '2026-04-22', TRUE),
(2, 2, 9000000002, 3, DATE '2026-04-23', FALSE),
(3, 3, 9000000003, 6, DATE '2026-04-24', TRUE);
