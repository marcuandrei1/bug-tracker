
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getBugs } from '../../api';
import './Bugs.css';

function Bugs({ user }) {
  const navigate = useNavigate();
  const [bugs, setBugs] = useState([]);
  const [titleFilter, setTitleFilter] = useState('');
  const [tagFilter, setTagFilter] = useState('');
  const [userFilter, setUserFilter] = useState('');
  const [myBugs, setMyBugs] = useState(false);
  const [loading, setLoading] = useState(true);

  const loadBugs = async (params = {}) => {
    setLoading(true);
    try {
      const data = await getBugs(params);
      setBugs(data);
    } catch (e) {
      console.error(e);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadBugs();
  }, []);

  const handleFilter = () => {
    const params = {};
    if (titleFilter.trim()) {
      params.title = titleFilter.trim();
    }
    if (tagFilter.trim()) {
      params.tags = tagFilter.trim();
    }
    if (myBugs && user && !params.title) {
      params.authorId = user.id;
    }
    loadBugs(params);
  };

  const handleReset = () => {
    setTitleFilter('');
    setTagFilter('');
    setUserFilter('');
    setMyBugs(false);
    loadBugs();
  };

  const filteredBugs = myBugs && user
    ? bugs.filter(b => b.author?.id === user.id)
    : bugs;

  return (
    <div className="container">
      <div className="filters">
        <input
          type="text"
          placeholder="Cauta dupa titlu..."
          value={titleFilter}
          onChange={e => setTitleFilter(e.target.value)}
        />
        <input
          type="text"
          placeholder="Filtreaza dupa tag..."
          value={tagFilter}
          onChange={e => setTagFilter(e.target.value)}
        />
        <label style={{ display: 'flex', alignItems: 'center', gap: '0.3rem', fontSize: '0.9rem' }}>
          <input
            type="checkbox"
            checked={myBugs}
            onChange={e => setMyBugs(e.target.checked)}
          />
          Bugurile mele
        </label>
        <button className="btn btn-secondary btn-sm" onClick={handleFilter}>
          Filtreaza
        </button>
        <button className="btn btn-sm" onClick={handleReset} style={{ background: '#ccc' }}>
          Reset
        </button>
        <button className="btn btn-danger btn-sm" onClick={() => navigate('/create-bug')}>
          + Raporteaza Bug
        </button>
      </div>

      <div>
        {loading ? (
          <p>Se incarca...</p>
        ) : filteredBugs.length === 0 ? (
          <p>Niciun bug gasit.</p>
        ) : (
          filteredBugs.map(bug => (
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
                De {bug.author?.username || 'Anonim'} &middot; {new Date(bug.createdAt).toLocaleString()}
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
    </div>
  );
}

export default Bugs;
