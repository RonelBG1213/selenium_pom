import Layout from '../components/Layout'

export default function DashboardPage() {
  return (
    <Layout activePage="dashboard">
      <div className="dashboard-content">
        <div className="dashboard-icon">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="24" cy="24" r="24" fill="#ecfdf5"/>
            <path d="M16 24l6 6 10-12" stroke="#10b981" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
        <h1 className="welcome-header">Welcome, Admin!</h1>
        <p className="welcome-sub">You have successfully signed in.</p>
        <div className="dashboard-stats">
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
        </div>
      </div>
    </Layout>
  )
}
