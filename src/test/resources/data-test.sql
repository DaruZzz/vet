-- ============================================
-- Clean database before inserting test data
-- ============================================
DELETE FROM availability_exception;
DELETE FROM availability;
DELETE FROM medication_batch;
DELETE FROM medication_prescription;
DELETE FROM treatment;
DELETE FROM invoice_item;
DELETE FROM payment;
DELETE FROM invoice;
DELETE FROM visit;
DELETE FROM pet;
DELETE FROM pet_owner;
DELETE FROM discount;
DELETE FROM promotion;
DELETE FROM loyalty_tier;
DELETE FROM medication;
DELETE FROM veterinarian_speciality;
DELETE FROM veterinarian;
DELETE FROM administrator;
DELETE FROM person;
DELETE FROM speciality;
DELETE FROM pet_type;

-- ============================================
-- Reset sequences to start from 1
-- ============================================
ALTER TABLE person ALTER COLUMN person_id RESTART WITH 1;
ALTER TABLE speciality ALTER COLUMN speciality_id RESTART WITH 1;
ALTER TABLE availability ALTER COLUMN availability_id RESTART WITH 1;
ALTER TABLE availability_exception ALTER COLUMN exception_id RESTART WITH 1;
ALTER TABLE medication ALTER COLUMN medication_id RESTART WITH 1;
ALTER TABLE medication_batch ALTER COLUMN batch_id RESTART WITH 1;
ALTER TABLE loyalty_tier ALTER COLUMN tier_id RESTART WITH 1;
ALTER TABLE promotion ALTER COLUMN promotion_id RESTART WITH 1;
ALTER TABLE discount ALTER COLUMN discount_id RESTART WITH 1;
ALTER TABLE pet_type ALTER COLUMN type_id RESTART WITH 1;
ALTER TABLE pet ALTER COLUMN pet_id RESTART WITH 1;
ALTER TABLE visit ALTER COLUMN visit_id RESTART WITH 1;
ALTER TABLE treatment ALTER COLUMN treatment_id RESTART WITH 1;
ALTER TABLE medication_prescription ALTER COLUMN prescription_id RESTART WITH 1;
ALTER TABLE invoice ALTER COLUMN invoice_id RESTART WITH 1;
ALTER TABLE invoice_item ALTER COLUMN item_id RESTART WITH 1;
ALTER TABLE payment ALTER COLUMN payment_id RESTART WITH 1;

-- ============================================
-- Insert Test Data
-- ============================================

-- Insert Specialities
INSERT INTO speciality (name, description) VALUES ('General Practice', 'General veterinary care');
INSERT INTO speciality (name, description) VALUES ('Surgery', 'Surgical procedures');

-- Insert Veterinarians
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (1, 'Test', 'Vet1', '666111222', 'test1@vet.com', 'Test Address 1');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (2, 'Test', 'Vet2', '666222333', 'test2@vet.com', 'Test Address 2');

INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (1, 'VET-TEST-001', 3);
INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (2, 'VET-TEST-002', 5);

-- Associate Veterinarians with Specialities
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (1, 1);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (2, 2);

-- Insert Medications
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Test Med 1', 'Test Ingredient 1', 'mg', 2.00, 50);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Test Med 2', 'Test Ingredient 2', 'ml', 3.00, 20);

-- Insert Medication Batches (with future expiry dates to avoid BatchExpiredException)
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (1, 'TEST-001', '2024-01-01', '2026-01-01', 100, 40, 1.50, 'Test Shelf A');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (2, 'TEST-002', '2024-01-01', '2026-12-01', 50, 15, 2.00, 'Test Shelf B');

-- Insert Loyalty Tiers
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Bronze', 0, 5.0, 'Entry level');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Silver', 100, 10.0, 'Mid level');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Gold', 250, 15.0, 'Premium level');

-- Insert Promotions
INSERT INTO promotion (name, description, discount_code, start_date, end_date) VALUES ('Test Promo 1', 'Test promotion', 'TEST2024', '2024-01-01', '2025-12-31');

-- Insert Discounts
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('TEST2024', 'PERCENTAGE', 10.0, '2024-01-01', '2025-12-31', 100, 0, 1, NULL);

-- Insert Availabilities for Veterinarian 1
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'MONDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'TUESDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');

-- Insert Pet Types
INSERT INTO pet_type (name, description) VALUES ('Dog', 'Domestic dog');
INSERT INTO pet_type (name, description) VALUES ('Cat', 'Domestic cat');

-- Insert Pet Owners
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (100, 'Test', 'Owner1', '666444555', 'owner1@test.com', 'Test Owner Address 1');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (101, 'Test', 'Owner2', '666555666', 'owner2@test.com', 'Test Owner Address 2');

INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (100, 50, 1);
INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (101, 150, 2);

-- Insert Pets
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES ('TestDog', '2020-05-10', 'Male', 'Test Breed', 'Brown', 25.5, 'TEST001', 100, 1);
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES ('TestCat', '2021-03-15', 'Female', 'Test Breed', 'White', 4.2, 'TEST002', 101, 2);