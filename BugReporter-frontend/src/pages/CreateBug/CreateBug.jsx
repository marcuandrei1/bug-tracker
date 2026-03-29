import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './CreateBug.css';

function CreateBug() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [tags, setTags] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = () => {
    if (!title || !text) {
      setError('Titlul si descrierea sunt obligatorii.');
      return;
    }
    alert('Bug creat (doar UI, fara backend)');
    navigate('/bugs');
  };

  return (
    <div className="container">
      <Link to="/bugs">&larr; Inapoi la lista</Link>

      <div className="card mt-2">
        <h2>Raporteaza un Bug</h2>

        <div className="form-group">
          <label>Titlu</label>
          <input
            type="text"
            placeholder="Titlul bugului"
            value={title}
            onChange={e => setTitle(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Descriere</label>
          <textarea
            placeholder="Descrie bugul in detaliu..."
            value={text}
            onChange={e => setText(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>URL Imagine (optional)</label>
          <input
            type="text"
            placeholder="https://..."
            value={imageUrl}
            onChange={e => setImageUrl(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Taguri (separate prin virgula)</label>
          <input
            type="text"
            placeholder="ui, crash, login..."
            value={tags}
            onChange={e => setTags(e.target.value)}
          />
        </div>

        <button className="btn btn-primary" onClick={handleSubmit}>
          Trimite Bug
        </button>
        {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
      </div>
    </div>
  );
}

export default CreateBug;
