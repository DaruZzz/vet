-- ============================================
-- Clean database before inserting test data
-- ============================================
DELETE FROM availability_exception;
DELETE FROM availability;
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
DELETE FROM medication_batch;
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
INSERT INTO speciality (name, description) VALUES ('Dermatology', 'Skin conditions');

-- Insert Veterinarians
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (1, 'Test', 'Vet1', '666111222', 'test1@vet.com', 'Test Address 1');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (2, 'Test', 'Vet2', '666222333', 'test2@vet.com', 'Test Address 2');

INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (1, 'VET-TEST-001', 3);
INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (2, 'VET-TEST-002', 5);

-- Associate Veterinarians with Specialities
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (1, 1);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (1, 3);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (2, 2);

-- Insert Medications
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold)
VALUES ('Test Med 1', 'Test Ingredient 1', 'mg', 2.00, 50);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold)
VALUES ('Test Med 2', 'Test Ingredient 2', 'ml', 3.00, 20);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold)
VALUES ('Test Med 3', 'Test Ingredient 3', 'mg', 1.50, 30);

-- Insert Medication Batches (with future expiry dates)
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location)
VALUES (1, 'TEST-001', '2024-01-01', '2026-01-01', 100, 40, 1.50, 'Test Shelf A');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location)
VALUES (2, 'TEST-002', '2024-01-01', '2026-12-01', 50, 15, 2.00, 'Test Shelf B');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location)
VALUES (3, 'TEST-003', '2024-01-01', '2026-06-01', 80, 25, 1.00, 'Test Shelf C');

-- Insert Loyalty Tiers
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description)
VALUES ('Bronze', 0, 5.0, 'Entry level');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description)
VALUES ('Silver', 100, 10.0, 'Mid level');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description)
VALUES ('Gold', 250, 15.0, 'Premium level');

-- Insert Promotions
INSERT INTO promotion (name, description, discount_code, start_date, end_date)
VALUES ('Test Promo 1', 'Test promotion', 'TEST2024', '2024-01-01', '2025-12-31');

-- Insert Discounts
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id)
VALUES ('TEST2024', 'PERCENTAGE', 10.0, '2024-01-01', '2025-12-31', 100, 5, 1, NULL);

-- Insert Availabilities for Veterinarian 1
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date)
VALUES (1, 'MONDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date)
VALUES (1, 'TUESDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');

-- Insert Pet Types
INSERT INTO pet_type (name, description) VALUES ('Dog', 'Domestic dog');
INSERT INTO pet_type (name, description) VALUES ('Cat', 'Domestic cat');

-- Insert Pet Owners
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address)
VALUES (100, 'Test', 'Owner1', '666444555', 'owner1@test.com', 'Test Owner Address 1');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address)
VALUES (101, 'Test', 'Owner2', '666555666', 'owner2@test.com', 'Test Owner Address 2');

INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (100, 50, 1);
INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (101, 150, 2);

-- Insert Pets
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id)
VALUES ('TestDog', '2020-05-10', 'Male', 'Test Breed', 'Brown', 25.5, 'TEST001', 100, 1);
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id)
VALUES ('TestCat', '2021-03-15', 'Female', 'Test Breed', 'White', 4.2, 'TEST002', 101, 2);

-- Insert COMPLETED Visits with full medical history
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (1, 100, 1, '2024-09-15 09:00:00', 30, 'Annual checkup', 20.0, 'COMPLETED', 'Pet in good health', 'No issues found');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (2, 101, 2, '2024-10-05 10:30:00', 15, 'Skin allergy', 20.0, 'COMPLETED', 'Mild dermatitis', 'Prescribed anti-inflammatory');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (1, 100, 1, '2024-11-01 11:00:00', 30, 'Vaccination', 20.0, 'COMPLETED', 'Vaccination successful', 'All shots up to date');

-- Insert SCHEDULED Visits for demand statistics
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (1, 100, 1, '2024-12-01 10:00:00', 15, 'Follow-up', 20.0, 'SCHEDULED');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (2, 101, 2, '2024-12-05 14:00:00', 15, 'Checkup', 20.0, 'SCHEDULED');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (1, 100, 1, '2024-12-10 09:00:00', 30, 'Surgery consultation', 20.0, 'SCHEDULED');

-- Add treatments to completed visits
INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Blood Test', 'Complete blood count', 45.00, 1);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Vaccination - Rabies', 'Annual rabies shot', 35.00, 1);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Skin Treatment', 'Topical anti-inflammatory', 25.00, 2);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('General Vaccination', 'Standard vaccination', 30.00, 3);

-- Add medication prescriptions to completed visits
INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (1, 1, 1, 14, '1 tablet twice daily', '7 days');

INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (2, 2, 2, 10, '1 tablet once daily', '10 days');

INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (3, 3, 3, 15, '1 tablet in the morning', '15 days');

-- Add invoices for completed visits
INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (100, 1, '2024-09-15', 120.00, 6.00, 114.00, 'PAID');

INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (101, 2, '2024-10-05', 95.00, 9.50, 85.50, 'PAID');

INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (100, 3, '2024-11-01', 110.00, 5.50, 104.50, 'UNPAID');

-- Add invoice items for visit 1
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (1, 'Consultation - Annual checkup', 2, 20.00, 40.00, 'VISIT', 1);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (1, 'Blood Test', 1, 45.00, 45.00, 'TREATMENT', 1);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (1, 'Vaccination - Rabies', 1, 35.00, 35.00, 'TREATMENT', 2);

-- Add invoice items for visit 2
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Consultation - Skin allergy', 1, 20.00, 20.00, 'VISIT', 2);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Skin Treatment', 1, 25.00, 25.00, 'TREATMENT', 3);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Test Med 2', 10, 3.00, 30.00, 'MEDICATION', 2);

-- Add invoice items for visit 3
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Consultation - Vaccination', 2, 20.00, 40.00, 'VISIT', 3);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'General Vaccination', 1, 30.00, 30.00, 'TREATMENT', 4);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Test Med 3', 15, 1.50, 22.50, 'MEDICATION', 3);

-- Add payments for paid invoices
INSERT INTO payment (invoice_id, payment_date, amount, payment_method, transaction_ref)
VALUES (1, '2024-09-15', 114.00, 'Credit Card', 'TXN-TEST-001');

INSERT INTO payment (invoice_id, payment_date, amount, payment_method, transaction_ref)
VALUES (2, '2024-10-05', 85.50, 'Cash', 'TXN-TEST-002');