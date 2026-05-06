import { Link, useNavigate } from 'react-router-dom'

export default function Layout({ children, activePage }) {
  const navigate = useNavigate()
  const user = localStorage.getItem('user') || 'User'

  function handleSignOut() {
    localStorage.removeItem('user')
    navigate('/')
  }

  return (
    <div className="app-body">
      <header className="navbar">
        <Link to="/home" className="navbar-brand">
          <svg width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="28" height="28" rx="6" fill="#4f46e5"/>
            <path d="M8 14l4 4 8-9" stroke="#ffffff" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          AutoTest
        </Link>
        <div className="navbar-right">
          <span className="navbar-user">{user}</span>
          <button className="navbar-logout" onClick={handleSignOut}>Sign out</button>
        </div>
      </header>

      <div className="app-layout">
        <aside className="sidebar">
          <nav className="sidebar-nav">
            <Link
              to="/home"
              id="nav-home"
              className={`sidebar-link${activePage === 'home' ? ' active' : ''}`}
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              Home
            </Link>
            <Link
              to="/dashboard"
              id="nav-dashboard"
              className={`sidebar-link${activePage === 'dashboard' ? ' active' : ''}`}
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="7" height="7"/>
                <rect x="14" y="3" width="7" height="7"/>
                <rect x="14" y="14" width="7" height="7"/>
                <rect x="3" y="14" width="7" height="7"/>
              </svg>
              Dashboard
            </Link>
          </nav>
        </aside>

        <main className="main-content">
          {children}
        </main>
      </div>
    </div>
  )
}
