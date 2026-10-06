fetch('http://localhost:5000/api/health').then(r => r.json()).then(console.log).catch(console.error);
