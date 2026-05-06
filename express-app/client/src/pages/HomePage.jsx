import Layout from '../components/Layout'

export default function HomePage() {
  const user = localStorage.getItem('user') || 'User'

  return (
    <Layout activePage="home">
      <div className="home-header">
        <h1 className="welcome-header">Welcome, {user}!</h1>
        <p>Here&apos;s an overview of your test suite.</p>
      </div>
      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-value">42</span>
          <span className="stat-label">Tests Run</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">38</span>
          <span className="stat-label">Passed</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">4</span>
          <span className="stat-label">Failed</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">90%</span>
          <span className="stat-label">Pass Rate</span>
        </div>
      </div>
    </Layout>
  )
}
