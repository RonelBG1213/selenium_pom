import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'

export default function RegisterPage() {
  const [form, setForm] = useState({ name: '', username: '', password: '', confirm: '' })
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  function handleChange(e) {
    const { name, value } = e.target
    setForm(f => ({ ...f, [name]: value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)

    if (!form.name.trim() || !form.username.trim() || !form.password.trim()) {
      setError('All fields are required.')
      return
    }
    if (form.password !== form.confirm) {
      setError('Passwords do not match.')
      return
    }

    const res = await fetch('/api/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: form.name, username: form.username, password: form.password })
    })
    const data = await res.json()

    if (data.success) {
      localStorage.setItem('user', form.username)
      navigate('/')
    } else {
      setError(data.error)
    }
  }

  return (
    <div className="page-wrapper">
      <div className="login-card">
        <h1 className="login-title">Create account</h1>
        <p className="login-subtitle">Register to get started</p>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label htmlFor="name">Full name</label>
            <input
              id="name"
              name="name"
              type="text"
              value={form.name}
              onChange={handleChange}
              autoComplete="name"
            />
          </div>
          <div className="form-group">
            <label htmlFor="reg-email">Email</label>
            <input
              id="reg-email"
              name="username"
              type="email"
              value={form.username}
              onChange={handleChange}
              autoComplete="email"
            />
          </div>
          <div className="form-group">
            <label htmlFor="reg-password">Password</label>
            <input
              id="reg-password"
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              autoComplete="new-password"
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirm">Confirm password</label>
            <input
              id="confirm"
              name="confirm"
              type="password"
              value={form.confirm}
              onChange={handleChange}
              autoComplete="new-password"
            />
          </div>
          <button type="submit" id="register-btn">Create account</button>
        </form>

        <p className="auth-link">
          Already have an account?{' '}
          <Link to="/">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
