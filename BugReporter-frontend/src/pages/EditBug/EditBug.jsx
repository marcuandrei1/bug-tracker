import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getBugById, updateBug, uploadFile } from '../../api';
import './EditBug.css';

function EditBug({ user }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [currentImageUrl, setCurrentImageUrl] = useState('');
  const [newImageFile, setNewImageFile] = useState(null);
  const [tags, setTags] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const bug = await getBugById(id);
        if (bug.author?.id !== user.id) {
          navigate('/bugs');
          return;
        }
        setTitle(bug.title);
        setText(bug.text);
        setCurrentImageUrl(bug.imageUrl || '');
        setTags((bug.tags || []).map(t => t.name).join(', '));
      } catch (e) {
        setError(e.message);
      }
      setLoading(false);
    };
    load();
  }, [id]);

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
      let imageUrl = currentImageUrl || null;
      if (newImageFile) {
        imageUrl = await uploadFile(newImageFile);
      }

      await updateBug(id, {
        title,
        text,
        imageUrl,
        author: { id: user.id },
        tags: tagList
      });
      navigate(`/bugs/${id}`);
    } catch (e) {
      setError(e.message);
    }
  };

  if (loading) return <div className="container"><p>Se incarca...</p></div>;

  return (
    <div className="container">
      <Link to={`/bugs/${id}`}>&larr; Inapoi la bug</Link>

      <div className="card mt-2">
        <h2>Editeaza Bug</h2>

        <div className="form-group">
          <label>Titlu</label>
          <input
            type="text"
            value={title}
            onChange={e => setTitle(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Descriere</label>
          <textarea
            value={text}
            onChange={e => setText(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Imagine (optional)</label>
          {currentImageUrl && !newImageFile && (
            <div style={{ marginBottom: '0.5rem' }}>
              <img src={`http://localhost:8080${currentImageUrl}`} alt="current" style={{ maxWidth: '200px', borderRadius: '6px' }} />
              <br />
              <button type="button" className="btn btn-danger btn-sm mt-1" onClick={() => setCurrentImageUrl('')}>Sterge imaginea</button>
            </div>
          )}
          <input
            type="file"
            accept="image/*"
            onChange={e => setNewImageFile(e.target.files[0] || null)}
          />
        </div>

        <div className="form-group">
          <label>Taguri (separate prin virgula)</label>
          <input
            type="text"
            value={tags}
            onChange={e => setTags(e.target.value)}
          />
        </div>

        <button className="btn btn-primary" onClick={handleSubmit}>
          Salveaza Modificarile
        </button>
        {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
      </div>
    </div>
  );
}

export default EditBug;
