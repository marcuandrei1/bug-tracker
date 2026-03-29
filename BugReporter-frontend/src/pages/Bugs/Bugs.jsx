import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Bugs.css';

const mockBugs = [
  {
    id: 1,
    title: 'Butonul de login nu functioneaza',
    text: 'Cand apas pe butonul de login, nu se intampla nimic. Pagina ramane blocata.',
    status: 'RECEIVED',
    createdAt: '2026-03-29T10:00:00',
    author: { id: 1, username: 'ion' },
    tags: [{ name: 'ui' }, { name: 'login' }]
  },
  {
    id: 2,
    title: 'Crash la upload imagine',
    text: 'Aplicatia se blocheaza cand incarci o imagine mai mare de 5MB.',
    status: 'IN_PROGRESS',
    createdAt: '2026-03-28T14:30:00',
    author: { id: 2, username: 'maria' },
    tags: [{ name: 'crash' }, { name: 'upload' }]
  },
  {
    id: 3,
    title: 'Textul dispare la resize',
    text: 'Pe mobile, textul din sidebar dispare complet.',
    status: 'SOLVED',
    createdAt: '2026-03-27T09:15:00',
    author: { id: 1, username: 'ion' },
    tags: [{ name: 'ui' }, { name: 'responsive' }]
  }
];

function Bugs() {
  const navigate = useNavigate();
  const [titleFilter, setTitleFilter] = useState('');
  const [tagFilter, setTagFilter] = useState('');
  const [filtered, setFiltered] = useState(mockBugs);

  const handleFilter = () => {
    let result = mockBugs;
    if (titleFilter) {
      result = result.filter(b => b.title.toLowerCase().includes(titleFilter.toLowerCase()));
    }
    if (tagFilter) {
      result = result.filter(b =>
        (b.tags || []).some(t => t.name.toLowerCase().includes(tagFilter.toLowerCase()))
      );
    }
    setFiltered(result);
  };

  return (
    <div className="container">
      <div className="flex-between mb-1">
        <h2>Bugs</h2>
        <button className="btn btn-primary" onClick={() => navigate('/create-bug')}>
          + Raporteaza Bug
        </button>
      </div>

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
        <button className="btn btn-secondary btn-sm" onClick={handleFilter}>
          Filtreaza
        </button>
      </div>

      <div>
        {filtered.length === 0 ? (
          <p>Niciun bug gasit.</p>
        ) : (
          filtered.map(bug => (
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
