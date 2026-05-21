import { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { getBugs } from '../../api';
import './Profile.css';

function Profile({ currentUser, onLogout }) {
  const navigate = useNavigate();
  const { id } = useParams();

  const [myBugs, setMyBugs] = useState([]);
  const [loading, setLoading] = useState(true);

  const [displayedUser, setDisplayedUser] = useState(currentUser);

  const isOwnProfile = !id || Number(id) === currentUser.id;

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        if (isOwnProfile) {
          // own profile
          setDisplayedUser(currentUser);
          const data = await getBugs({ authorId: currentUser.id });
          setMyBugs(data);
        } else {
          // different profile
          const data = await getBugs({ authorId: Number(id) });
          setMyBugs(data);

          if (data.length > 0 && data[0].author) {
            setDisplayedUser(data[0].author);
          } else {
            setDisplayedUser({ username: 'Utilizator', score: 0 });
          }
        }
      } catch (e) {
        console.error(e);
      }
      setLoading(false);
    };
    load();
  }, [id, currentUser, isOwnProfile]);

  const solved = myBugs.filter(b => b.status === 'SOLVED').length;
  const inProgress = myBugs.filter(b => b.status === 'IN_PROGRESS').length;
  const received = myBugs.filter(b => b.status === 'RECEIVED').length;

  const formatUserScore = (score) => `${Number(score ?? 0).toFixed(1)} puncte`;

  return (
      <div className="container">
        <Link to="/bugs">&larr; Inapoi la lista</Link>

        <div className="profile-header mt-2">
          <div className="profile-avatar">{displayedUser.username?.charAt(0).toUpperCase()}</div>
          <div style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <h2>{displayedUser.username}</h2>
              {isOwnProfile && (
                  <button className="btn btn-danger btn-sm" onClick={onLogout}>Logout</button>
              )}
            </div>
            <p className="text-muted">Scor: {formatUserScore(displayedUser.score)}</p>
          </div>
        </div>

        <div className="stats-container mt-2">
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

        <h3 className="mt-2">{isOwnProfile ? "Bugurile mele" : `Bugurile lui ${displayedUser.username}`}</h3>

        {loading ? (
            <p>Se incarca...</p>
        ) : myBugs.length === 0 ? (
            <p>Niciun bug raportat.</p>
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