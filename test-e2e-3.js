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
    createdId = createRes.data?.id || createRes.data?.dockNumber; // some might not return standard id
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

    // Docks (uses core port controllers?) wait, let me check dock schema/routes. I don't see docks endpoints in previous grep.
    // wait, I saw `GET /api/docks` earlier. But are there POST/PUT/DELETE for docks?
    // Let's test Warehouses and Containers.
    
    await testFeature('Containers', '/containers', { containerCode: `C-${Date.now()}`, weight: 20.5, status: 'WAITING' }, { priority: 'HIGH' });
    
    // Test Expenses
    const vRes = await fetchJSON(`${API_URL}/vehicles`, { method: 'POST', body: JSON.stringify({ registrationNo: `V-E-${Date.now()}`, name: 'E-Truck', model: 'Volvo', type: 'TRUCK', maxCapacity: 40 }) });
    const vId = vRes.data.id;
    
    await testFeature('Expenses', '/expenses', { vehicleId: vId, type: 'TOLL', amount: 50.0, description: 'Test Toll' }, { amount: 60.0 });
    
    await fetchJSON(`${API_URL}/vehicles/${vId}`, { method: 'DELETE' });

    console.log(JSON.stringify(report, null, 2));
  } catch (error) {
    console.error('Test crashed:', error);
  }
}

runTests();
