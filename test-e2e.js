const API_URL = 'http://localhost:5001/api';
let token = '';

const report = { passed: [], failed: [] };

async function fetchJSON(url, options = {}) {
  const res = await fetch(url, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}), ...options.headers },
  });
  const data = await res.json().catch(() => null);
  if (!res.ok) throw { status: res.status, data };
  return data;
}

async function testFeature(featureName, endpoint, createPayload, updatePayload) {
  let createdId = '';
  try {
    const createRes = await fetchJSON(`${API_URL}${endpoint}`, { method: 'POST', body: JSON.stringify(createPayload) });
    createdId = createRes.data.id;
    if (!createdId) throw new Error('No ID returned');
    report.passed.push(`[${featureName}] CREATE`);

    await fetchJSON(`${API_URL}${endpoint}/${createdId}`);
    report.passed.push(`[${featureName}] READ`);

    if (updatePayload) {
      await fetchJSON(`${API_URL}${endpoint}/${createdId}`, { method: 'PUT', body: JSON.stringify(updatePayload) });
      report.passed.push(`[${featureName}] UPDATE`);
    }

    await fetchJSON(`${API_URL}${endpoint}/${createdId}`, { method: 'DELETE' });
    report.passed.push(`[${featureName}] DELETE`);
  } catch (err) {
    report.failed.push({
      feature: featureName,
      action: createdId ? (updatePayload ? 'UPDATE/DELETE' : 'DELETE') : 'CREATE',
      error: err.data || err.message,
    });
    if (createdId) {
      try { await fetchJSON(`${API_URL}${endpoint}/${createdId}`, { method: 'DELETE' }); } catch (e) {}
    }
  }
}

async function runTests() {
  try {
    const loginRes = await fetchJSON(`${API_URL}/auth/login`, { method: 'POST', body: JSON.stringify({ email: 'admin@vadhvanport.in', password: 'Admin@123' }) });
    token = loginRes.data.accessToken;
    console.log('Login successful...');

    await testFeature('Vehicles', '/vehicles', { registrationNo: `TEST-TRK-${Date.now()}`, name: 'Test Truck', model: 'Volvo', type: 'TRUCK', maxCapacity: 40 }, { maxCapacity: 45 });
    await testFeature('Drivers', '/drivers', { name: 'Test Driver', licenseNo: `TEST-DL-${Date.now()}`, licenseCategory: 'HMV', licenseExpiry: new Date(Date.now() + 31536000000).toISOString(), phone: '+919999999999' }, { experienceYears: 5 });
    await testFeature('Ships', '/ships', { imoNumber: `IMO-${Date.now()}`, name: 'Test Ship', arrivalTime: new Date().toISOString(), priority: 'MEDIUM', cargoType: 'Containers' }, { priority: 'HIGH' });
    await testFeature('Rail Tracks', '/rail-tracks', { trackNumber: `TRK-TEST-${Date.now()}`, capacity: 100, status: 'AVAILABLE' }, { capacity: 150 });
    await testFeature('Equipment', '/equipment', { name: 'Test Crane', equipmentNumber: `EQ-TEST-${Date.now()}`, type: 'CRANE', status: 'AVAILABLE' }, { healthScore: 90 });

    const vRes = await fetchJSON(`${API_URL}/vehicles`, { method: 'POST', body: JSON.stringify({ registrationNo: `V-M-${Date.now()}`, name: 'M-Truck', model: 'Volvo', type: 'TRUCK', maxCapacity: 40 }) });
    const vId = vRes.data.id;

    await testFeature('Maintenance', '/maintenance', { vehicleId: vId, type: 'INSPECTION', description: 'Test inspection', cost: 100 }, { status: 'COMPLETED' });
    await testFeature('Fuel Logs', '/fuel', { vehicleId: vId, quantityLitres: 100, costPerLitre: 1.5, mileage: 8 }, { quantityLitres: 120 });

    await fetchJSON(`${API_URL}/vehicles/${vId}`, { method: 'DELETE' });

    console.log(JSON.stringify(report, null, 2));
  } catch (error) {
    console.error('Test crashed:', error);
  }
}

runTests();
