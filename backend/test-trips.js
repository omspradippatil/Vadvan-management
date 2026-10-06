fetch('http://localhost:5001/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'admin@vadhvanport.in', password: 'Admin@123' })
})
.then(res => res.json())
.then(data => {
  const token = data.data.accessToken;
  return fetch('http://localhost:5001/api/vehicles/available', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
})
.then(res => res.json())
.then(console.log)
