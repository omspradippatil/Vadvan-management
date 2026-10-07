const jwt = require('./backend/node_modules/jsonwebtoken');
const token = jwt.sign({ id: '1', role: 'ADMIN' }, 'cdf7e1900bb07f5305fc2fffbd945a1a8691012fb900925331f12bcba79698f5', { expiresIn: '1d' });

fetch('http://localhost:5001/api/port-health', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
}).then(r => r.json()).then(console.log).catch(console.error);
