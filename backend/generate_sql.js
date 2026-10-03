const fs = require('fs');

const randInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;
const randPick = (arr) => arr[Math.floor(Math.random() * arr.length)];
const cuidMock = () => 'c' + Math.random().toString(36).substr(2, 9) + Math.random().toString(36).substr(2, 9);

let sql = `-- VADHVAN PORT 50+ DEMO DATA SEED\n\n`;

// Warehouses
sql += `-- WAREHOUSES\n`;
const whIds = Array.from({length: 15}, () => cuidMock());
for (let i = 0; i < 15; i++) {
  sql += `INSERT INTO "warehouses" ("id", "name", "capacity", "availableSpace", "occupiedSpace", "updatedAt") VALUES ('${whIds[i]}', 'Warehouse ${i+1}', 50000, 30000, 20000, CURRENT_TIMESTAMP);\n`;
}

// Docks
sql += `\n-- DOCKS\n`;
const dockIds = Array.from({length: 20}, () => cuidMock());
for (let i = 0; i < 20; i++) {
  const status = randPick(['AVAILABLE', 'MAINTENANCE', 'AVAILABLE']);
  const wh = randPick(whIds);
  sql += `INSERT INTO "docks" ("id", "dockNumber", "status", "containerCount", "warehouseId", "updatedAt") VALUES ('${dockIds[i]}', 'DOCK-${100+i}', '${status}', ${randInt(0, 500)}, '${wh}', CURRENT_TIMESTAMP);\n`;
}

// Ships (50 records)
sql += `\n-- SHIPS (50 records)\n`;
const shipIds = Array.from({length: 50}, () => cuidMock());
for (let i = 0; i < 50; i++) {
  const name = randPick(['MSC', 'Maersk', 'CMA CGM', 'Evergreen', 'Hapag-Lloyd']) + ' ' + randPick(['Ocean', 'Star', 'Express', 'Voyager', 'Global']) + ' ' + (i+1);
  const type = randPick(['Ultra Large Container', 'General', 'Liquid Bulk', 'RoRo']);
  const status = randPick(['AT_SEA', 'DOCKED', 'WAITING']);
  const prio = randPick(['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']);
  const imo = 'IMO' + randInt(9000000, 9999999);
  sql += `INSERT INTO "ships" ("id", "imoNumber", "name", "arrivalTime", "cargoType", "priority", "status", "containerCount", "shipLength", "shipWidth", "draft", "updatedAt") VALUES ('${shipIds[i]}', '${imo}', '${name}', CURRENT_TIMESTAMP - INTERVAL '${randInt(1,48)} hours', '${type}', '${prio}', '${status}', ${randInt(1000, 20000)}, ${randInt(200, 400)}, ${randInt(30, 60)}, ${randInt(10, 16)}, CURRENT_TIMESTAMP);\n`;
}

// Drivers (50 records)
sql += `\n-- DRIVERS (50 records)\n`;
const driverIds = Array.from({length: 50}, () => cuidMock());
for (let i = 0; i < 50; i++) {
  const name = randPick(['Ramesh', 'Suresh', 'Amit', 'Raj', 'Vikram']) + ' ' + randPick(['Kumar', 'Singh', 'Patil', 'Sharma', 'Desai']);
  const status = randPick(['AVAILABLE', 'ON_TRIP', 'OFF_DUTY']);
  sql += `INSERT INTO "drivers" ("id", "name", "licenseNumber", "phone", "status", "updatedAt") VALUES ('${driverIds[i]}', '${name}', 'MH-DL-${randInt(1000,9999)}', '+9198${randInt(10000000,99999999)}', '${status}', CURRENT_TIMESTAMP);\n`;
}

// Vehicles (50 records)
sql += `\n-- VEHICLES (50 records)\n`;
const vehicleIds = Array.from({length: 50}, () => cuidMock());
for (let i = 0; i < 50; i++) {
  const type = randPick(['HEAVY_TRUCK', 'AUTONOMOUS_TRUCK', 'FORKLIFT']);
  const status = randPick(['AVAILABLE', 'ON_TRIP', 'IN_SHOP']);
  sql += `INSERT INTO "vehicles" ("id", "registrationNumber", "type", "capacity", "model", "status", "updatedAt") VALUES ('${vehicleIds[i]}', 'MH-04-TRK-${1000+i}', '${type}', ${randInt(20, 80)}, 'Volvo FH', '${status}', CURRENT_TIMESTAMP);\n`;
}

// Equipment (50 records)
sql += `\n-- EQUIPMENT (50 records)\n`;
for (let i = 0; i < 50; i++) {
  const type = randPick(['CRANE', 'GANTRY', 'REACH_STACKER']);
  const status = randPick(['AVAILABLE', 'IN_USE', 'MAINTENANCE']);
  sql += `INSERT INTO "equipment" ("id", "name", "equipmentNumber", "type", "status", "healthScore", "updatedAt") VALUES ('${cuidMock()}', '${type} ${i+1}', 'EQ-${100+i}', '${type}', '${status}', ${randInt(50, 100)}, CURRENT_TIMESTAMP);\n`;
}

fs.writeFileSync('/Users/om/Desktop/Projects/Vadvan-management/backend/demo_data_50.sql', sql);
console.log('Generated demo_data_50.sql');
