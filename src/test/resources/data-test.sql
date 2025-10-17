-- Test data for Sprint 1

-- Insert Specialities
INSERT INTO speciality (name, description) VALUES
                                               ('General Practice', 'General veterinary care'),
                                               ('Surgery', 'Surgical procedures');

-- Insert Veterinarians
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES
                                                                                        (1, 'Test', 'Vet1', '666111222', 'test1@vet.com', 'Test Address 1'),
                                                                                        (2, 'Test', 'Vet2', '666222333', 'test2@vet.com', 'Test Address 2');

INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES
                                                                              (1, 'VET-TEST-001', 3),
                                                                              (2, 'VET-TEST-002', 5);

-- Associate Veterinarians with Specialities
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES
                                                                         (1, 1),
                                                                         (2, 2);

-- Insert Medications
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES
                                                                                                 ('Test Med 1', 'Test Ingredient 1', 'mg', 2.00, 50),
                                                                                                 ('Test Med 2', 'Test Ingredient 2', 'ml', 3.00, 20);

-- Insert Medication Batches
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES
                                                                                                                                                                        (1, 'TEST-001', '2024-01-01', '2026-01-01', 100, 40, 1.50, 'Test Shelf A'),
                                                                                                                                                                        (2, 'TEST-002', '2024-01-01', '2025-12-01', 50, 15, 2.00, 'Test Shelf B');

-- Insert Loyalty Tiers
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES
                                                                                                     ('Bronze', 0, 5.0, 'Entry level'),
                                                                                                     ('Silver', 100, 10.0, 'Mid level'),
                                                                                                     ('Gold', 250, 15.0, 'Premium level');

-- Insert Promotions
INSERT INTO promotion (name, description, discount_code, start_date, end_date) VALUES
    ('Test Promo 1', 'Test promotion', 'TEST2024', '2024-01-01', '2024-12-31');

-- Insert Discounts
INSERT INTO discount (code, type, value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES
    ('TEST2024', 'PERCENTAGE', 10.0, '2024-01-01', '2024-12-31', 100, 0, 1, NULL);

-- Insert Availabilities
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES
                                                                                                            (1, 'MONDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (1, 'TUESDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');