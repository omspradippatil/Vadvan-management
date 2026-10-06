import axios from 'axios';

const API_URL = 'http://localhost:5001/api';
let token = '';

const report = {
  passed: [] as string[],
  failed: [] as { feature: string; action: string; error: any }[],
};

async function testFeature(featureName: string, endpoint: string, createPayload: any, updatePayload: any) {
  let createdId = '';
  
  try {
    // 1. ADD a thing (CREATE)
    const createRes = await axios.post(`${API_URL}${endpoint}`, createPayload, { headers: { Authorization: `Bearer ${token}` } });
    createdId = createRes.data.data.id;
    if (!createdId) throw new Error('No ID returned on creation');
    report.passed.push(`[${featureName}] CREATE`);

    // 2. READ the thing
    await axios.get(`${API_URL}${endpoint}/${createdId}`, { headers: { Authorization: `Bearer ${token}` } });
    report.passed.push(`[${featureName}] READ`);

    // 3. UPDATE the thing (if updatePayload provided)
    if (updatePayload) {
      await axios.put(`${API_URL}${endpoint}/${createdId}`, updatePayload, { headers: { Authorization: `Bearer ${token}` } });
      report.passed.push(`[${featureName}] UPDATE`);
    }

    // 4. DELETE the thing
    await axios.delete(`${API_URL}${endpoint}/${createdId}`, { headers: { Authorization: `Bearer ${token}` } });
    report.passed.push(`[${featureName}] DELETE`);

  } catch (error: any) {
    report.failed.push({
      feature: featureName,
      action: createdId ? (updatePayload ? 'UPDATE/DELETE' : 'DELETE') : 'CREATE',
      error: error.response?.data || error.message,
    });
    
    // Attempt cleanup if it failed after creation
    if (createdId) {
      try { await axios.delete(`${API_URL}${endpoint}/${createdId}`, { headers: { Authorization: `Bearer ${token}` } }); } catch (e) {}
    }
  }
}

async function runTests() {
  try {
    // Login
    const loginRes = await axios.post(`${API_URL}/auth/login`, { email: 'admin@vadhvanport.in', password: 'Admin@123' });
    token = loginRes.data.data.token;
    console.log('Login successful. Starting CRUD testing...');

    await testFeature('Vehicles', '/vehicles', 
      { registrationNo: `TEST-TRK-${Date.now()}`, name: 'Test Truck', model: 'Volvo', type: 'TRUCK', maxCapacity: 40 },
      { maxCapacity: 45 }
    );
    
    await testFeature('Drivers', '/drivers', 
      { name: 'Test Driver', licenseNo: `TEST-DL-${Date.now()}`, licenseCategory: 'HMV', licenseExpiry: new Date(Date.now() + 31536000000).toISOString(), phone: '+919999999999' },
      { experienceYears: 5 }
    );
    
    await testFeature('Ships', '/ships', 
      { imoNumber: `IMO-${Date.now()}`, name: 'Test Ship', arrivalTime: new Date().toISOString(), priority: 'MEDIUM', cargoType: 'Containers' },
      { priority: 'HIGH' }
    );

    await testFeature('Rail Tracks', '/rail-tracks', 
      { trackNumber: `TRK-TEST-${Date.now()}`, capacity: 100, status: 'AVAILABLE' },
      { capacity: 150 }
    );
    
    await testFeature('Equipment', '/equipment', 
      { name: 'Test Crane', equipmentNumber: `EQ-TEST-${Date.now()}`, type: 'CRANE', status: 'AVAILABLE' },
      { healthScore: 90 }
    );

    // Test Trips (needs vehicle and driver)
    // To simplify, we'll test maintenance directly on the vehicle we create
    const vRes = await axios.post(`${API_URL}/vehicles`, { registrationNo: `V-M-${Date.now()}`, name: 'M-Truck', model: 'M', type: 'TRUCK', maxCapacity: 40 }, { headers: { Authorization: `Bearer ${token}` } });
    const vId = vRes.data.data.id;

    await testFeature('Maintenance', '/maintenance', 
      { vehicleId: vId, type: 'INSPECTION', description: 'Test inspection', cost: 100 },
      { status: 'COMPLETED' }
    );
    
    await testFeature('Fuel Logs', '/fuel', 
      { vehicleId: vId, quantityLitres: 100, costPerLitre: 1.5, mileage: 8 },
      { quantityLitres: 120 }
    );

    await axios.delete(`${API_URL}/vehicles/${vId}`, { headers: { Authorization: `Bearer ${token}` } });

    console.log(JSON.stringify(report, null, 2));

  } catch (error: any) {
    console.error('Test script crashed:', error.response?.data || error.message);
  }
}

runTests();
