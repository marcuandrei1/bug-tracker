import { useParams, Link } from 'react-router-dom';
import { useState } from 'react';
import './BugDetail.css';

const mockBugs = {
  1: {
    id: 1, title: 'Butonul de login nu functioneaza',
    text: 'Cand apas pe butonul de login, nu se intampla nimic. Pagina ramane blocata.',
    status: 'RECEIVED', createdAt: '2026-03-29T10:00:00',
    author: { id: 1, username: 'ion' },
    tags: [{ name: 'ui' }, { name: 'login' }],
    imageUrl: null
  },
  2: {
    id: 2, title: 'Crash la upload imagine',
    text: 'Aplicatia se blocheaza cand incarci o imagine mai mare de 5MB.',
    status: 'IN_PROGRESS', createdAt: '2026-03-28T14:30:00',
    author: { id: 2, username: 'maria' },
    tags: [{ name: 'crash' }, { name: 'upload' }],
    imageUrl: null
  },
  3: {
    id: 3, title: 'Textul dispare la resize',
    text: 'Pe mobile, textul din sidebar dispare complet.',
    status: 'SOLVED', createdAt: '2026-03-27T09:15:00',
    author: { id: 1, username: 'ion' },
    tags: [{ name: 'ui' }, { name: 'responsive' }],
    imageUrl: null
  }
};

const mockComments = {
  1: [
    { id: 1, text: 'Am si eu aceeasi problema pe Chrome.', createdAt: '2026-03-29T11:00:00', author: { id: 2, username: 'maria' }, imageUrl: null }
  ],
  2: [
    { id: 2, text: 'Incerc sa reproduc bugul, revin.', createdAt: '2026-03-28T15:00:00', author: { id: 1, username: 'ion' }, imageUrl: null },
    { id: 3, text: 'Am gasit cauza — limita de memorie.', createdAt: '2026-03-28T16:30:00', author: { id: 2, username: 'maria' }, imageUrl: null }
  ],
  3: []
};

function BugDetail() {
  const { id } = useParams();
  const bug = mockBugs[id];
  const [commentText, setCommentText] = useState('');
  const comments = mockComments[id] || [];

  if (!bug) {
    return (
      <div className="container">
        <p style={{ color: 'red' }}>Bug negasit.</p>
      </div>
    );
  }

  return (
    <div className="container">
      <Link to="/bugs">&larr; Inapoi la lista</Link>

      <div className="card mt-2">
        <div className="flex-between">
          <h2>{bug.title}</h2>
          <span className={`status ${bug.status}`}>
            {bug.status.replace('_', ' ')}
          </span>
        </div>
        <div className="meta">
          De {bug.author?.username || 'Anonim'} &middot; {new Date(bug.createdAt).toLocaleString()}
        </div>
        {bug.imageUrl && (
          <img src={bug.imageUrl} alt="bug image" style={{ maxWidth: '100%', margin: '0.5rem 0', borderRadius: '6px' }} />
        )}
        <p className="mt-1">{bug.text}</p>
        <div className="tags mt-1">
          {(bug.tags || []).map(t => (
            <span key={t.name} className="tag">{t.name}</span>
          ))}
        </div>
      </div>

      <h3 className="mt-2">Comentarii ({comments.length})</h3>

      <div>
        {comments.length === 0 ? (
          <p>Niciun comentariu inca.</p>
        ) : (
          comments.map(c => (
            <div key={c.id} className="comment">
              <div className="meta">
                {c.author?.username || 'Anonim'} &middot; {new Date(c.createdAt).toLocaleString()}
              </div>
              {c.imageUrl && (
                <img src={c.imageUrl} alt="comment image" style={{ maxWidth: '100%', margin: '0.3rem 0', borderRadius: '4px' }} />
              )}
              <p className="mt-1">{c.text}</p>
            </div>
          ))
        )}
      </div>

      <div className="card mt-2">
        <h4>Adauga comentariu</h4>
        <div className="form-group">
          <textarea
            placeholder="Scrie un comentariu..."
            value={commentText}
            onChange={e => setCommentText(e.target.value)}
          />
        </div>
        <button className="btn btn-primary">Trimite</button>
      </div>
    </div>
  );
}

export default BugDetail;
