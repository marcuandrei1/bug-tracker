import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { createBug, uploadFile } from '../../api';
import './CreateBug.css';

function CreateBug({ user }) {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [imageFile, setImageFile] = useState(null);
  const [tags, setTags] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    if (!title || !text) {
      setError('Titlul si descrierea sunt obligatorii.');
      return;
    }

    const tagList = tags
      .split(',')
      .map(t => t.trim())
      .filter(t => t.length > 0)
      .map(t => ({ name: t }));

    try {
      let imageUrl = null;
      if (imageFile) {
        imageUrl = await uploadFile(imageFile);
      }

      await createBug({
        title,
        text,
        imageUrl,
        author: { id: user.id },
        tags: tagList
      });
      navigate('/bugs');
    } catch (e) {
      setError(e.message);
    }
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
          <label>Imagine (optional)</label>
          <input
            type="file"
            accept="image/*"
            onChange={e => setImageFile(e.target.files[0] || null)}
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
