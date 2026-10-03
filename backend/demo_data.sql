-- ==============================================================================
-- VADHVAN PORT - REALISTIC DEMO DATA SQL SEED
-- ==============================================================================
-- Run this in your Supabase SQL Editor to populate the database with real-world 
-- simulating data for ships, warehouses, docks, and vehicles.

-- 1. Create Docks (Mega Terminals)
INSERT INTO "Dock" ("id", "name", "type", "capacity", "status", "createdAt", "updatedAt") VALUES
('DOCK-MCT-1', 'Mega Container Terminal 1', 'CONTAINER', 20000, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DOCK-MCT-2', 'Mega Container Terminal 2', 'CONTAINER', 18000, 'MAINTENANCE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DOCK-MPB-1', 'Multipurpose Berth 1', 'GENERAL', 10000, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DOCK-LIQ-1', 'Liquid Cargo Berth 1', 'LIQUID', 50000, 'OCCUPIED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Create Warehouses
INSERT INTO "Warehouse" ("id", "name", "capacity", "currentLoad", "type", "createdAt", "updatedAt") VALUES
('WH-COLD-A', 'Cold Storage Unit A', 5000, 3200, 'REFRIGERATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('WH-GEN-B', 'General Transit Shed B', 12000, 9500, 'GENERAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('WH-HAZ-1', 'Hazardous Materials Zone', 2000, 800, 'HAZARDOUS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. Create Ships
INSERT INTO "Ship" ("id", "name", "type", "capacity", "status", "eta", "createdAt", "updatedAt") VALUES
('IMO-9780471', 'Mumbai Maersk', 'ULTRA_LARGE_CONTAINER', 19038, 'AT_SEA', CURRENT_TIMESTAMP + INTERVAL '4 hours', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IMO-9406738', 'Nhava Sheva Express', 'CONTAINER', 8530, 'DOCKED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IMO-9231248', 'MSC India', 'CONTAINER_GENERAL', 4500, 'AT_SEA', CURRENT_TIMESTAMP + INTERVAL '12 hours', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IMO-LIQ-99', 'Gulf Oil Tanker', 'LIQUID_BULK', 25000, 'DOCKED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. Create Vehicles (Port Trucks / AGVs)
INSERT INTO "Vehicle" ("id", "registrationNumber", "type", "capacity", "model", "status", "createdAt", "updatedAt") VALUES
('VEH-AGV-01', 'MH-04-AGV-1001', 'AUTONOMOUS_TRUCK', 40, 'Tesla Semi', 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VEH-TRK-02', 'MH-43-TRK-8822', 'HEAVY_TRUCK', 60, 'Volvo FH16', 'ON_TRIP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VEH-AGV-03', 'MH-04-AGV-1002', 'AUTONOMOUS_TRUCK', 40, 'Tesla Semi', 'IN_SHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. Create Drivers
INSERT INTO "Driver" ("id", "name", "licenseNumber", "phone", "status", "createdAt", "updatedAt") VALUES
('DRV-001', 'Ramesh Kumar', 'MH-DL-2015-88493', '+91 9876543210', 'ON_TRIP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRV-002', 'Suresh Patil', 'MH-DL-2018-11234', '+91 9876543211', 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 6. Create Equipment (Cranes)
INSERT INTO "Equipment" ("id", "name", "type", "status", "lastMaintenance", "createdAt", "updatedAt") VALUES
('EQ-STS-01', 'Ship-to-Shore Crane 1', 'CRANE', 'OPERATIONAL', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('EQ-RTG-04', 'Rubber Tyred Gantry 4', 'GANTRY', 'MAINTENANCE', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 7. Create Containers
INSERT INTO "Container" ("id", "containerNumber", "type", "status", "weight", "warehouseId", "shipId", "createdAt", "updatedAt") VALUES
('CONT-1001', 'MSCU-123456-7', 'STANDARD_20FT', 'STORED', 14500, 'WH-GEN-B', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CONT-1002', 'MAEU-987654-3', 'REEFER_40FT', 'IN_TRANSIT', 28000, NULL, 'IMO-9406738', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ==============================================================================
-- END OF DEMO DATA
-- ==============================================================================
