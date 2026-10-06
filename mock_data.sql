-- Clear existing data if necessary (Uncomment if you want to wipe data first)
-- TRUNCATE TABLE "users", "vehicles", "drivers", "ships", "docks", "warehouses", "rail_tracks", "containers", "equipment", "trips", "maintenance_logs", "fuel_logs" CASCADE;

-- Users (Demo credentials)
INSERT INTO "users" ("id", "email", "passwordHash", "name", "role", "status", "createdAt", "updatedAt") VALUES
('usr_admin_1', 'admin@vadhvanport.in', '$2a$10$X8Ld/k2.b.L7I4p8d6qA.OqC3/v24l3X6v2.d4k3C2', 'Administrator', 'ADMIN', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('usr_fleet_1', 'fleet@vadhvanport.in', '$2a$10$X8Ld/k2.b.L7I4p8d6qA.OqC3/v24l3X6v2.d4k3C2', 'Fleet Manager', 'FLEET_MANAGER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('usr_ops_1', 'ops@vadhvanport.in', '$2a$10$X8Ld/k2.b.L7I4p8d6qA.OqC3/v24l3X6v2.d4k3C2', 'Operations Manager', 'OPERATIONS_MANAGER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Ships (Real names requested by user)
INSERT INTO "ships" ("id", "imoNumber", "name", "arrivalTime", "expectedDeparture", "containerCount", "priority", "cargoType", "status", "createdAt", "updatedAt") VALUES
('ship_1', '9811000', 'MSC Oscar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 days', 19224, 'HIGH', 'General Containers', 'WAITING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ship_2', '9893905', 'Ever Given', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '2 days', 20124, 'CRITICAL', 'Mixed Cargo', 'DOCKED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ship_3', '9839179', 'CMA CGM Jacques Saade', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '4 days', 23112, 'MEDIUM', 'LNG/Containers', 'LOADING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ship_4', '9863297', 'HMM Algeciras', CURRENT_TIMESTAMP + INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '6 days', 23964, 'LOW', 'Containers', 'WAITING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ship_5', '9778791', 'Madrid Maersk', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '1 day', 20568, 'HIGH', 'Containers', 'UNLOADING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Docks
INSERT INTO "docks" ("id", "dockNumber", "status", "containerCount", "createdAt", "updatedAt") VALUES
('dock_1', 'Dock 1 - Mega Terminal', 'AVAILABLE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('dock_2', 'Dock 2 - Bulk Cargo', 'AVAILABLE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('dock_3', 'Dock 3 - Liquid Handling', 'AVAILABLE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Warehouses
INSERT INTO "warehouses" ("id", "name", "capacity", "availableSpace", "occupiedSpace", "createdAt", "updatedAt") VALUES
('wh_1', 'Warehouse A - North Wing', 50000, 40000, 10000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('wh_2', 'Warehouse B - East Wing', 75000, 75000, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('wh_3', 'Warehouse C - South Terminal', 100000, 80000, 20000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Rail Tracks
INSERT INTO "rail_tracks" ("id", "trackNumber", "capacity", "status", "destination", "createdAt", "updatedAt") VALUES
('rail_1', 'Track 1 - Northern Express', 150, 'AVAILABLE', 'Delhi Hub', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('rail_2', 'Track 2 - Bulk Transit', 200, 'AVAILABLE', 'Mumbai Central', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('rail_3', 'Track 3 - Container Yard A', 100, 'AVAILABLE', 'Ahmedabad Port', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('rail_4', 'Track 4 - East Wing Transfer', 120, 'AVAILABLE', 'Kolkata Junction', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('rail_5', 'Track 5 - South Gate Export', 180, 'AVAILABLE', 'Chennai Harbor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Equipment
INSERT INTO "equipment" ("id", "name", "equipmentNumber", "type", "status", "healthScore", "createdAt", "updatedAt") VALUES
('eq_1', 'STS Crane Alpha', 'CRN-STS-001', 'CRANE', 'AVAILABLE', 98, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('eq_2', 'Gantry Crane Beta', 'CRN-GNT-002', 'CRANE', 'AVAILABLE', 85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('eq_3', 'Heavy Forklift X', 'FLK-HVY-101', 'FORKLIFT', 'AVAILABLE', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('eq_4', 'Reach Stacker Omega', 'RCH-STK-044', 'REACH_STACKER', 'MAINTENANCE', 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Vehicles
INSERT INTO "vehicles" ("id", "registrationNo", "name", "model", "type", "status", "maxCapacity", "odometer", "fuelLevel", "healthScore", "createdAt", "updatedAt") VALUES
('veh_1', 'MH-04-TRK-1001', 'Port Truck 1', 'Volvo FH16', 'TRUCK', 'AVAILABLE', 40, 15000, 80, 95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('veh_2', 'MH-04-TRK-1002', 'Port Truck 2', 'Scania R500', 'TRUCK', 'AVAILABLE', 45, 22000, 65, 88, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('veh_3', 'MH-04-TRL-2001', 'Heavy Trailer A', 'Mercedes Actros', 'TRAILER', 'ON_TRIP', 60, 5000, 45, 99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('veh_4', 'MH-04-FLK-3001', 'Yard Forklift', 'Toyota 8FD', 'FORKLIFT', 'IN_SHOP', 10, 8000, 20, 42, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('veh_5', 'MH-04-TRK-1003', 'Port Truck 3', 'Volvo FH16', 'TRUCK', 'AVAILABLE', 40, 12000, 90, 98, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Drivers
INSERT INTO "drivers" ("id", "name", "licenseNo", "licenseCategory", "licenseExpiry", "phone", "safetyScore", "experienceYears", "status", "createdAt", "updatedAt") VALUES
('drv_1', 'Rajesh Kumar', 'MH-DL-4123', 'Heavy Motor Vehicle', CURRENT_TIMESTAMP + INTERVAL '2 years', '+919876543210', 95, 8, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('drv_2', 'Amit Singh', 'MH-DL-5521', 'Heavy Trailer', CURRENT_TIMESTAMP + INTERVAL '1 year', '+919876543211', 88, 5, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('drv_3', 'Suresh Patel', 'MH-DL-6632', 'Commercial', CURRENT_TIMESTAMP + INTERVAL '3 years', '+919876543212', 100, 12, 'ON_TRIP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('drv_4', 'Vikram Desai', 'MH-DL-7743', 'Heavy Equipment', CURRENT_TIMESTAMP + INTERVAL '6 months', '+919876543213', 75, 3, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Containers
INSERT INTO "containers" ("id", "containerCode", "weight", "priority", "status", "createdAt", "updatedAt") VALUES
('cnt_1', 'MSCU-123456-7', 24.5, 'HIGH', 'WAITING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cnt_2', 'CMAU-765432-1', 18.2, 'MEDIUM', 'IN_TRANSIT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cnt_3', 'EGLU-112233-4', 30.1, 'CRITICAL', 'LOADING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cnt_4', 'HMMU-998877-5', 22.0, 'LOW', 'DELIVERED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Trips
INSERT INTO "trips" ("id", "tripNumber", "vehicleId", "driverId", "source", "destination", "cargoWeight", "status", "priority", "createdAt", "updatedAt") VALUES
('trp_1', 'TRP-2026-001', 'veh_3', 'drv_3', 'Dock 1 - Mega Terminal', 'Warehouse A - North Wing', 45.5, 'DISPATCHED', 'HIGH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('trp_2', 'TRP-2026-002', 'veh_1', 'drv_1', 'Rail Track 2', 'Dock 3 - Liquid Handling', 20.0, 'COMPLETED', 'MEDIUM', CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('trp_3', 'TRP-2026-003', 'veh_2', 'drv_2', 'Warehouse B', 'Dock 2 - Bulk Cargo', 35.2, 'DRAFT', 'LOW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- Maintenance Logs
INSERT INTO "maintenance_logs" ("id", "vehicleId", "equipmentId", "type", "description", "technicianName", "cost", "status", "createdAt", "updatedAt") VALUES
('mtn_1', 'veh_4', NULL, 'REPAIR', 'Hydraulic lift failure repair', 'Ramesh Tech', 1500, 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('mtn_2', NULL, 'eq_4', 'SCHEDULED', 'Quarterly spreader alignment check', 'Sanjay Motors', 500, 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('mtn_3', 'veh_2', NULL, 'OIL_CHANGE', 'Routine synthetic oil change', 'AutoBot-X', 200, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days')
ON CONFLICT ("id") DO NOTHING;

-- Fuel Logs
INSERT INTO "fuel_logs" ("id", "vehicleId", "driverId", "tripId", "quantityLitres", "costPerLitre", "totalCost", "mileage", "loggedAt", "createdAt") VALUES
('fuel_1', 'veh_1', 'drv_1', 'trp_2', 150, 1.20, 180, 5.5, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
('fuel_2', 'veh_3', 'drv_3', 'trp_1', 300, 1.25, 375, 4.2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('fuel_3', 'veh_2', 'drv_2', NULL, 80, 1.20, 96, 6.0, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days')
ON CONFLICT ("id") DO NOTHING;
