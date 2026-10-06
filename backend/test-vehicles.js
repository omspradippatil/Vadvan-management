fetch('http://localhost:5001/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'admin@vadhvanport.in', password: 'Admin@123' })
})
.then(res => res.json())
.then(data => {
  if (!data.success) throw new Error(JSON.stringify(data));
  const token = data.data.accessToken;
  return fetch('http://localhost:5001/api/vehicles', {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({
      registrationNo: 'TEST-1234',
      name: 'Test Vehicle',
      model: 'Tata Model X',
      type: 'TRUCK',
      maxCapacity: 50
    })
  });
})
.then(res => res.json())
.then(console.log)
.catch(console.error);
