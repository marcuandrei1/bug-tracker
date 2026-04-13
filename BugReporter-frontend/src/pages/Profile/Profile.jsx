import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getBugs } from '../../api';
import './Profile.css';

function Profile({ user, onLogout }) {
  const navigate = useNavigate();
  const [myBugs, setMyBugs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await getBugs({ authorId: user.id });
        setMyBugs(data);
      } catch (e) {
        console.error(e);
      }
      setLoading(false);
    };
    load();
  }, [user.id]);

  const solved = myBugs.filter(b => b.status === 'SOLVED').length;
  const inProgress = myBugs.filter(b => b.status === 'IN_PROGRESS').length;
  const received = myBugs.filter(b => b.status === 'RECEIVED').length;

  return (
    <div className="container">
      <Link to="/bugs">&larr; Inapoi la lista</Link>

      <div className="profile-header mt-2">
        <div className="profile-avatar">{user.username.charAt(0).toUpperCase()}</div>
        <div style={{ flex: 1 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <h2>{user.username}</h2>
            <button className="btn btn-danger btn-sm" onClick={onLogout}>Logout</button>
          </div>
          <p className="meta">{user.email}</p>
          <p className="meta">Membru din {new Date(user.createdAt).toLocaleDateString()}</p>
        </div>
      </div>

      <div className="stats mt-2">
        <div className="stat-card">
          <span className="stat-number">{myBugs.length}</span>
          <span className="stat-label">Total buguri</span>
        </div>
        <div className="stat-card">
          <span className="stat-number">{received}</span>
          <span className="stat-label">Received</span>
        </div>
        <div className="stat-card">
          <span className="stat-number">{inProgress}</span>
          <span className="stat-label">In Progress</span>
        </div>
        <div className="stat-card">
          <span className="stat-number">{solved}</span>
          <span className="stat-label">Solved</span>
        </div>
      </div>

      <h3 className="mt-2">Bugurile mele</h3>

      {loading ? (
        <p>Se incarca...</p>
      ) : myBugs.length === 0 ? (
        <p>Nu ai raportat niciun bug inca.</p>
      ) : (
        myBugs.map(bug => (
          <div
            key={bug.id}
            className="card"
            style={{ cursor: 'pointer' }}
            onClick={() => navigate(`/bugs/${bug.id}`)}
          >
            <div className="flex-between">
              <h3>{bug.title}</h3>
              <span className={`status ${bug.status}`}>
                {bug.status.replace('_', ' ')}
              </span>
            </div>
            <div className="meta">
              {new Date(bug.createdAt).toLocaleString()}
            </div>
            <p>
              {bug.text?.substring(0, 150) || ''}
              {(bug.text?.length || 0) > 150 ? '...' : ''}
            </p>
            <div className="tags">
              {(bug.tags || []).map(t => (
                <span key={t.name} className="tag">{t.name}</span>
              ))}
            </div>
          </div>
        ))
      )}
    </div>
  );
}

export default Profile;
