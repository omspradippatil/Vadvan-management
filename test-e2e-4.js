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

    await testFeature('Docks', '/docks', { dockNumber: `DK-${Date.now()}` }, { warehouseId: null });
    await testFeature('Warehouses', '/warehouses', { name: `WH-${Date.now()}`, capacity: 1000 }, { capacity: 2000 });

    console.log(JSON.stringify(report, null, 2));
  } catch (error) {
    console.error('Test crashed:', error);
  }
}

runTests();
