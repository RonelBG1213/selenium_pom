require('dotenv').config({ path: require('path').join(__dirname, '..', '.env') });

const express = require('express');
const path    = require('path');

const app  = express();
const PORT = process.env.PORT || 3000;

// Seeded admin account — loaded from root .env (ADMIN_EMAIL / ADMIN_PASSWORD)
const ADMIN_EMAIL    = process.env.ADMIN_EMAIL    || 'admin@example.com';
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || 'Admin123!';

const users = {
  [ADMIN_EMAIL]: ADMIN_PASSWORD
};

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// POST /api/login
app.post('/api/login', (req, res) => {
  const { username = '', password = '' } = req.body;

  if (!username.trim() || !password.trim()) {
    return res.json({ success: false, error: 'Email and password are required.' });
  }

  if (users[username] && users[username] === password) {
    return res.json({ success: true });
  }

  res.json({ success: false, error: 'Invalid email or password. Please try again.' });
});

// POST /api/register
app.post('/api/register', (req, res) => {
  const { name = '', username = '', password = '' } = req.body;

  if (!name.trim() || !username.trim() || !password.trim()) {
    return res.json({ success: false, error: 'All fields are required.' });
  }

  if (users[username]) {
    return res.json({ success: false, error: 'An account with this email already exists.' });
  }

  users[username] = password;
  res.json({ success: true });
});

// Catch-all: let React Router handle client-side routes
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Test app running at http://localhost:${PORT}`);
});
