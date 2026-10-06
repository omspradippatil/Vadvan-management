import axios from 'axios';
axios.post('http://localhost:5001/api/auth/login', { email: 'admin@vadhvanport.in', password: 'Admin@123' })
  .then(res => {
    const cookie = res.headers['set-cookie']?.join('; ');
    const token = res.data.data.token; // check structure
    return axios.get('http://localhost:5001/api/vehicles/available', {
      headers: { Authorization: `Bearer ${token}` }
    });
  })
  .then(res => console.log(JSON.stringify(res.data, null, 2)))
  .catch(err => console.error(err.response?.data || err.message));
