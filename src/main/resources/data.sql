-- ============================================
-- VETERINARY CLINIC - INITIAL DATA
-- ============================================

-- Insert Specialities (one by one)
INSERT INTO speciality (name, description) VALUES ('General Practice', 'General veterinary care and consultation');
INSERT INTO speciality (name, description) VALUES ('Surgery', 'Surgical procedures for animals');
INSERT INTO speciality (name, description) VALUES ('Dermatology', 'Treatment of skin conditions');
INSERT INTO speciality (name, description) VALUES ('Oncology', 'Cancer treatment for animals');
INSERT INTO speciality (name, description) VALUES ('Exotics', 'Care for exotic animals');
INSERT INTO speciality (name, description) VALUES ('Cardiology', 'Heart and cardiovascular care');

-- Insert Veterinarians
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (1, 'Maria', 'Garcia', '666111222', 'maria.garcia@vetclinic.com', 'Calle Mayor 10, Barcelona');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (2, 'Juan', 'Martinez', '666222333', 'juan.martinez@vetclinic.com', 'Avenida Diagonal 50, Barcelona');

INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (1, 'VET-2020-001', 5);
INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES (2, 'VET-2018-002', 7);

-- Associate Veterinarians with Specialities
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (1, 1);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (1, 3);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (2, 1);
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES (2, 2);

-- Insert Medications
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Amoxicillin 250mg', 'Amoxicillin', 'mg', 2.50, 50);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Meloxicam 1.5mg', 'Meloxicam', 'mg', 3.00, 30);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Prednisolone 5mg', 'Prednisolone', 'mg', 1.80, 40);
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES ('Fenbendazole 100mg', 'Fenbendazole', 'mg', 4.50, 20);

-- Insert Medication Batches
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (1, 'AMOX-2024-001', '2024-01-15', '2026-01-15', 100, 75, 1.50, 'Shelf A1');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (1, 'AMOX-2024-002', '2024-06-10', '2026-06-10', 100, 90, 1.45, 'Shelf A1');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (2, 'MELOX-2024-001', '2024-02-20', '2026-12-20', 50, 25, 2.00, 'Shelf A2');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (3, 'PRED-2024-001', '2024-03-15', '2026-03-15', 80, 35, 1.20, 'Shelf B1');
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES (4, 'FENB-2024-001', '2024-04-10', '2026-10-10', 30, 15, 3.00, 'Shelf B2');

-- Insert Loyalty Tiers
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Bronze', 0, 5.0, 'Entry level - 5% discount on services');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Silver', 100, 10.0, 'Mid level - 10% discount on services and free annual checkup');
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES ('Gold', 250, 15.0, 'Premium level - 15% discount on services, free annual checkup and priority booking');

-- Insert Promotions
INSERT INTO promotion (name, description, discount_code, start_date, end_date) VALUES ('Summer Wellness', 'Summer wellness check promotion', 'SUMMER2024', '2024-06-01', '2025-09-30');
INSERT INTO promotion (name, description, discount_code, start_date, end_date) VALUES ('Winter Vaccination', 'Winter vaccination campaign', 'WINTER2024', '2024-12-01', '2025-03-31');

-- Insert Discounts for Promotions
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('SUMMER2024', 'PERCENTAGE', 20.0, '2024-06-01', '2025-09-30', 100, 15, 1, NULL);
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('WINTER2024', 'PERCENTAGE', 15.0, '2024-12-01', '2025-03-31', 150, 5, 2, NULL);
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('FIRSTVISIT', 'FIXED_AMOUNT', 25.0, '2024-01-01', '2025-12-31', NULL, 50, NULL, NULL);

-- Insert Discounts for Loyalty Tiers
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('BRONZE_TIER', 'LOYALTY_TIER', 5.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 1);
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('SILVER_TIER', 'LOYALTY_TIER', 10.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 2);
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES ('GOLD_TIER', 'LOYALTY_TIER', 15.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 3);

-- Insert Availabilities for Veterinarians
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'MONDAY', '09:00:00', '14:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'TUESDAY', '09:00:00', '14:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'WEDNESDAY', '09:00:00', '14:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'THURSDAY', '15:00:00', '20:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (1, 'FRIDAY', '15:00:00', '20:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (2, 'MONDAY', '15:00:00', '20:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (2, 'TUESDAY', '15:00:00', '20:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (2, 'WEDNESDAY', '15:00:00', '20:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (2, 'THURSDAY', '09:00:00', '14:00:00', '2024-01-01', '2025-12-31');
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES (2, 'FRIDAY', '09:00:00', '14:00:00', '2024-01-01', '2025-12-31');

-- Insert Availability Exceptions
INSERT INTO availability_exception (availability_id, exception_date, start_time, end_time, reason) VALUES (1, '2024-12-25', NULL, NULL, 'Christmas Day');
INSERT INTO availability_exception (availability_id, exception_date, start_time, end_time, reason) VALUES (2, '2024-12-25', NULL, NULL, 'Christmas Day');
INSERT INTO availability_exception (availability_id, exception_date, start_time, end_time, reason) VALUES (6, '2024-12-25', NULL, NULL, 'Christmas Day');

-- Insert Pet Types
INSERT INTO pet_type (name, description) VALUES ('Dog', 'Domestic dog');
INSERT INTO pet_type (name, description) VALUES ('Cat', 'Domestic cat');
INSERT INTO pet_type (name, description) VALUES ('Bird', 'Various bird species');
INSERT INTO pet_type (name, description) VALUES ('Reptile', 'Reptiles including snakes and lizards');
INSERT INTO pet_type (name, description) VALUES ('Rabbit', 'Domestic rabbit');
INSERT INTO pet_type (name, description) VALUES ('Guinea Pig', 'Small rodent pet');

-- Insert Pet Owners
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (100, 'Carlos', 'Lopez', '666444555', 'carlos.lopez@email.com', 'Calle Rosales 25, Barcelona');
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES (101, 'Ana', 'Fernandez', '666555666', 'ana.fernandez@email.com', 'Avenida Libertad 15, Barcelona');

INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (100, 50, 1);
INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES (101, 150, 2);

-- Insert Pets
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES ('Max', '2020-05-10', 'Male', 'Golden Retriever', 'Golden', 30.5, 'CHIP001', 100, 1);
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES ('Luna', '2021-03-15', 'Female', 'Siamese', 'White/Brown', 4.2, 'CHIP002', 100, 2);
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES ('Rocky', '2019-08-20', 'Male', 'German Shepherd', 'Black/Brown', 35.0, 'CHIP003', 101, 1);

-- Insert Sample Visits
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status) VALUES (1, 100, 1, '2024-11-10 10:00:00', 15, 'Annual checkup', 20.0, 'COMPLETED');
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status) VALUES (2, 100, 1, '2024-11-12 11:00:00', 30, 'Vaccination', 20.0, 'SCHEDULED');
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status) VALUES (3, 101, 2, '2024-11-15 16:00:00', 15, 'General consultation', 20.0, 'SCHEDULED');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (1, 100, 1, '2024-09-15 09:00:00', 30, 'Vaccination follow-up', 20.0, 'COMPLETED', 'Vaccination successful', 'Pet in good health');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (2, 100, 1, '2024-10-05 10:30:00', 15, 'Skin allergy', 20.0, 'COMPLETED', 'Mild dermatitis', 'Prescribed anti-inflammatory');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status, diagnosis, notes)
VALUES (3, 101, 2, '2024-10-20 15:30:00', 30, 'Dental checkup', 20.0, 'COMPLETED', 'Mild tartar buildup', 'Recommended dental cleaning');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (1, 100, 1, '2024-12-01 11:00:00', 15, 'Annual checkup', 20.0, 'SCHEDULED');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (2, 100, 2, '2024-12-05 16:00:00', 15, 'Follow-up', 20.0, 'SCHEDULED');

INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status)
VALUES (3, 101, 1, '2024-12-10 10:00:00', 30, 'Surgery consultation', 20.0, 'SCHEDULED');

-- Add treatments to completed visits
INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Blood Test', 'Complete blood count analysis', 45.00, 4);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Vaccination - Rabies', 'Annual rabies vaccination', 35.00, 4);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Skin Treatment', 'Topical anti-inflammatory application', 25.00, 5);

INSERT INTO treatment (name, description, cost, visit_id)
VALUES ('Dental Examination', 'Complete oral examination', 40.00, 6);

-- Add medication prescriptions to completed visits
INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (4, 1, 1, 14, '1 tablet twice daily with food', '7 days');

INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (5, 2, 3, 10, '1 tablet once daily', '10 days');

INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (5, 3, 4, 15, '1 tablet in the morning', '15 days');

INSERT INTO medication_prescription (visit_id, medication_id, batch_id, quantity_prescribed, dosage_instructions, duration)
VALUES (6, 1, 2, 7, '1 tablet twice daily', '7 days');

-- Add sample invoices for the completed visits
INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (100, 4, '2024-09-15', 120.00, 6.00, 114.00, 'PAID');

INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (100, 5, '2024-10-05', 95.00, 4.75, 90.25, 'PAID');

INSERT INTO invoice (pet_owner_id, visit_id, invoice_date, total_amount, discount_amount, final_amount, status)
VALUES (101, 6, '2024-10-20', 110.00, 11.00, 99.00, 'PAID');

-- Add invoice items for visit 4
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Consultation - Vaccination follow-up', 2, 20.00, 40.00, 'VISIT', 4);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Blood Test', 1, 45.00, 45.00, 'TREATMENT', 1);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (2, 'Vaccination - Rabies', 1, 35.00, 35.00, 'TREATMENT', 2);

-- Add invoice items for visit 5
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Consultation - Skin allergy', 1, 20.00, 20.00, 'VISIT', 5);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Skin Treatment', 1, 25.00, 25.00, 'TREATMENT', 3);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Meloxicam 1.5mg', 10, 3.00, 30.00, 'MEDICATION', 2);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (3, 'Prednisolone 5mg', 15, 1.80, 27.00, 'MEDICATION', 3);

-- Add invoice items for visit 6
INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (4, 'Consultation - Dental checkup', 2, 20.00, 40.00, 'VISIT', 6);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (4, 'Dental Examination', 1, 40.00, 40.00, 'TREATMENT', 4);

INSERT INTO invoice_item (invoice_id, description, quantity, unit_price, item_total, item_type, reference_id)
VALUES (4, 'Amoxicillin 250mg', 7, 2.50, 17.50, 'MEDICATION', 1);

-- Add payments for the invoices
INSERT INTO payment (invoice_id, payment_date, amount, payment_method, transaction_ref)
VALUES (2, '2024-09-15', 114.00, 'Credit Card', 'TXN-20240915-001');

INSERT INTO payment (invoice_id, payment_date, amount, payment_method, transaction_ref)
VALUES (3, '2024-10-05', 90.25, 'Cash', 'TXN-20241005-001');

INSERT INTO payment (invoice_id, payment_date, amount, payment_method, transaction_ref)
VALUES (4, '2024-10-20', 99.00, 'Debit Card', 'TXN-20241020-001');

-- Amoxicillin and Meloxicam should not be taken together (always incompatible)
INSERT INTO medication_incompatibility (medication1_id, medication2_id, persisting_period_days)
VALUES (1, 2, NULL);

-- Meloxicam and Prednisolone - incompatible for 7 days after last dose
INSERT INTO medication_incompatibility (medication1_id, medication2_id, persisting_period_days)
VALUES (2, 3, 7);

-- Prednisolone and Fenbendazole - incompatible for 14 days
INSERT INTO medication_incompatibility (medication1_id, medication2_id, persisting_period_days)
VALUES (3, 4, 14);