-- Insert Specialities
INSERT INTO speciality (name, description) VALUES
                                               ('General Practice', 'General veterinary care and consultation'),
                                               ('Surgery', 'Surgical procedures for animals'),
                                               ('Dermatology', 'Treatment of skin conditions'),
                                               ('Oncology', 'Cancer treatment for animals'),
                                               ('Exotics', 'Care for exotic animals'),
                                               ('Cardiology', 'Heart and cardiovascular care');

-- Insert Veterinarians
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES
                                                                                        (1, 'Maria', 'Garcia', '666111222', 'maria.garcia@vetclinic.com', 'Calle Mayor 10, Barcelona'),
                                                                                        (2, 'Juan', 'Martinez', '666222333', 'juan.martinez@vetclinic.com', 'Avenida Diagonal 50, Barcelona');

INSERT INTO veterinarian (person_id, license_number, years_of_experience) VALUES
                                                                              (1, 'VET-2020-001', 5),
                                                                              (2, 'VET-2018-002', 7);

-- Associate Veterinarians with Specialities
INSERT INTO veterinarian_speciality (veterinarian_id, speciality_id) VALUES
                                                                         (1, 1), -- Maria: General Practice
                                                                         (1, 3), -- Maria: Dermatology
                                                                         (2, 1), -- Juan: General Practice
                                                                         (2, 2); -- Juan: Surgery

-- Insert Medications
INSERT INTO medication (name, active_ingredient, dosage_unit, unit_price, reorder_threshold) VALUES
                                                                                                 ('Amoxicillin 250mg', 'Amoxicillin', 'mg', 2.50, 50),
                                                                                                 ('Meloxicam 1.5mg', 'Meloxicam', 'mg', 3.00, 30),
                                                                                                 ('Prednisolone 5mg', 'Prednisolone', 'mg', 1.80, 40),
                                                                                                 ('Fenbendazole 100mg', 'Fenbendazole', 'mg', 4.50, 20);

-- Insert Medication Batches
INSERT INTO medication_batch (medication_id, lot_number, received_date, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, storage_location) VALUES
                                                                                                                                                                        (1, 'AMOX-2024-001', '2024-01-15', '2026-01-15', 100, 75, 1.50, 'Shelf A1'),
                                                                                                                                                                        (1, 'AMOX-2024-002', '2024-06-10', '2026-06-10', 100, 90, 1.45, 'Shelf A1'),
                                                                                                                                                                        (2, 'MELOX-2024-001', '2024-02-20', '2025-12-20', 50, 25, 2.00, 'Shelf A2'),
                                                                                                                                                                        (3, 'PRED-2024-001', '2024-03-15', '2026-03-15', 80, 35, 1.20, 'Shelf B1'),
                                                                                                                                                                        (4, 'FENB-2024-001', '2024-04-10', '2025-10-10', 30, 15, 3.00, 'Shelf B2');

-- Insert Loyalty Tiers
INSERT INTO loyalty_tier (tier_name, required_points, discount_percentage, benefits_description) VALUES
                                                                                                     ('Bronze', 0, 5.0, 'Entry level - 5% discount on services'),
                                                                                                     ('Silver', 100, 10.0, 'Mid level - 10% discount on services and free annual checkup'),
                                                                                                     ('Gold', 250, 15.0, 'Premium level - 15% discount on services, free annual checkup and priority booking');

-- Insert Promotions
INSERT INTO promotion (name, description, discount_code, start_date, end_date) VALUES
                                                                                   ('Summer Wellness', 'Summer wellness check promotion', 'SUMMER2024', '2024-06-01', '2024-09-30'),
                                                                                   ('Winter Vaccination', 'Winter vaccination campaign', 'WINTER2024', '2024-12-01', '2025-03-31');

-- Insert Discounts for Promotions
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES
                                                                                                                                 ('SUMMER2024', 'PERCENTAGE', 20.0, '2024-06-01', '2024-09-30', 100, 15, 1, NULL),
                                                                                                                                 ('WINTER2024', 'PERCENTAGE', 15.0, '2024-12-01', '2025-03-31', 150, 5, 2, NULL),
                                                                                                                                 ('FIRSTVISIT', 'FIXED_AMOUNT', 25.0, '2024-01-01', '2025-12-31', NULL, 50, NULL, NULL);

-- Insert Discounts for Loyalty Tiers
INSERT INTO discount (code, type, discount_value, start_date, end_date, max_uses, uses_count, promotion_id, loyalty_tier_id) VALUES
                                                                                                                                 ('BRONZE_TIER', 'LOYALTY_TIER', 5.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 1),
                                                                                                                                 ('SILVER_TIER', 'LOYALTY_TIER', 10.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 2),
                                                                                                                                 ('GOLD_TIER', 'LOYALTY_TIER', 15.0, '2024-01-01', '2025-12-31', NULL, 0, NULL, 3);

-- Insert Availabilities for Veterinarians
INSERT INTO availability (veterinarian_id, day_of_week, start_time, end_time, initial_date, final_date) VALUES
                                                                                                            (1, 'MONDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (1, 'TUESDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (1, 'WEDNESDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (1, 'THURSDAY', '15:00:00', '20:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (1, 'FRIDAY', '15:00:00', '20:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (2, 'MONDAY', '15:00:00', '20:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (2, 'TUESDAY', '15:00:00', '20:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (2, 'WEDNESDAY', '15:00:00', '20:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (2, 'THURSDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31'),
                                                                                                            (2, 'FRIDAY', '09:00:00', '14:00:00', '2024-01-01', '2024-12-31');

-- Insert some availability exceptions
INSERT INTO availability_exception (availability_id, exception_date, start_time, end_time, reason) VALUES
                                                                                                       (1, '2024-12-25', NULL, NULL, 'Christmas Day'),
                                                                                                       (2, '2024-12-25', NULL, NULL, 'Christmas Day'),
                                                                                                       (6, '2024-12-25', NULL, NULL, 'Christmas Day');

-- Insert Pet Types (Sprint 2)
INSERT INTO pet_type (name, description) VALUES
                                             ('Dog', 'Domestic dog'),
                                             ('Cat', 'Domestic cat'),
                                             ('Bird', 'Various bird species'),
                                             ('Reptile', 'Reptiles including snakes and lizards'),
                                             ('Rabbit', 'Domestic rabbit'),
                                             ('Guinea Pig', 'Small rodent pet');

-- Insert Pet Owners (Sprint 2)
INSERT INTO person (person_id, first_name, last_name, phone_number, email, address) VALUES
                                                                                        (100, 'Carlos', 'Lopez', '666444555', 'carlos.lopez@email.com', 'Calle Rosales 25, Barcelona'),
                                                                                        (101, 'Ana', 'Fernandez', '666555666', 'ana.fernandez@email.com', 'Avenida Libertad 15, Barcelona');

INSERT INTO pet_owner (person_id, loyalty_points, loyalty_tier_id) VALUES
                                                                       (100, 50, 1), -- Carlos: Bronze tier
                                                                       (101, 150, 2); -- Ana: Silver tier

-- Insert Pets (Sprint 2)
INSERT INTO pet (name, date_of_birth, gender, breed, color, weight, microchip_id, owner_id, pet_type_id) VALUES
                                                                                                             ('Max', '2020-05-10', 'Male', 'Golden Retriever', 'Golden', 30.5, 'CHIP001', 100, 1),
                                                                                                             ('Luna', '2021-03-15', 'Female', 'Siamese', 'White/Brown', 4.2, 'CHIP002', 100, 2),
                                                                                                             ('Rocky', '2019-08-20', 'Male', 'German Shepherd', 'Black/Brown', 35.0, 'CHIP003', 101, 1);

-- Insert Sample Visits (Sprint 2)
INSERT INTO visit (pet_id, pet_owner_id, veterinarian_id, date_time, duration, reason_for_visit, price_per_block, status) VALUES
                                                                                                                              (1, 100, 1, '2024-11-10 10:00:00', 15, 'Annual checkup', 20.0, 'COMPLETED'),
                                                                                                                              (2, 100, 1, '2024-11-12 11:00:00', 30, 'Vaccination', 20.0, 'SCHEDULED'),
                                                                                                                              (3, 101, 2, '2024-11-15 16:00:00', 15, 'General consultation', 20.0, 'SCHEDULED');