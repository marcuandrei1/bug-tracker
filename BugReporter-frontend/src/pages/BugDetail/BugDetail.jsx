import { useParams, Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { getBugById, getCommentsByBugId, createComment, deleteBug, voteBug, deleteComment, updateComment, voteComment,uploadFile, markBugAsSolved } from '../../api';
import './BugDetail.css';

function BugDetail({ user }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [bug, setBug] = useState(null);
  const [comments, setComments] = useState([]);
  const [commentText, setCommentText] = useState('');
  const [commentImageFile, setCommentImageFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editCommentText, setEditCommentText] = useState('');

  const loadData = async () => {
    try {
      const bugData = await getBugById(id,user?.id);
      setBug(bugData);
      const commentsData = await getCommentsByBugId(id, user?.id);
      setComments(commentsData);
    } catch (e) {
      setError(e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, [id]);

  const handleAddComment = async () => {
    if (!commentText.trim()) return;
    try {
      let imageUrl = null;
      if (commentImageFile) {
        imageUrl = await uploadFile(commentImageFile);
      }
      await createComment({
        text: commentText,
        imageUrl,
        author: { id: user.id },
        bug: { id: Number(id) }
      });
      setCommentText('');
      setCommentImageFile(null);
      loadData();
    } catch (e) {
      setError(e.message);
    }
  };

  const handleDeleteBug = async () => {
    if (!window.confirm('Esti sigur ca vrei sa stergi acest bug?')) return;
    try {
      await deleteBug(id);
      navigate('/bugs');
    } catch (e) {
      setError(e.message);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('Stergi comentariul?')) return;
    try {
      await deleteComment(commentId);
      loadData();
    } catch (e) {
      setError(e.message);
    }
  };

  const handleEditComment = (comment) => {
    setEditingCommentId(comment.id);
    setEditCommentText(comment.text);
  };

  const handleSaveComment = async (commentId) => {
    if (!editCommentText.trim()) return;
    try {
      await updateComment(commentId, { text: editCommentText });
      setEditingCommentId(null);
      setEditCommentText('');
      loadData();
    } catch (e) {
      setError(e.message);
    }
  };

  const handleCancelEdit = () => {
    setEditingCommentId(null);
    setEditCommentText('');
  };

  const handleMarkSolved = async () => {
    try {
      await markBugAsSolved(id);
      loadData();
    } catch (e) {
      setError(e.message);
    }
  };

  const handleVoteBug = async (type) => {
    try {
      const response = await voteBug(id, user.id, type);

      setBug(prevBug => ({
        ...prevBug,
        score: response.score,
        userVoteType: response.userVoteType
      }));
    } catch (e) {
      alert(e.message);
    }
  };

  const handleVoteComment = async (commentId, type) => {
    try {
      const response = await voteComment(commentId, user.id, type);

      setComments(prevComments =>
          prevComments.map(c =>
              c.id === commentId
                  ? { ...c, score: response.score, userVoteType: response.userVoteType }
                  : c
          )
      );
    } catch(e) {
      alert(e.message);
    }
  }

  if (loading) return <div className="container"><p>Se incarca...</p></div>;
  if (error && !bug) return <div className="container"><p style={{ color: 'red' }}>{error}</p></div>;
  if (!bug) return <div className="container"><p style={{ color: 'red' }}>Bug negasit.</p></div>;

  const isAuthor = user && bug.author && user.id === bug.author.id;

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
          <img src={`http://localhost:8080${bug.imageUrl}`} alt="bug image" style={{ maxWidth: '100%', margin: '0.5rem 0', borderRadius: '6px' }} />
        )}
        <p className="mt-1">{bug.text}</p>
        <div className="tags mt-1">
          {(bug.tags || []).map(t => (
            <span key={t.name} className="tag">{t.name}</span>
          ))}
        </div>
        <div className="vote-controls">
          <button
              disabled={isAuthor}
              className={bug.userVoteType === 'LIKE' ? 'vote-like-active' : ''}
              onClick={() => handleVoteBug('LIKE')}
          >👍🏼</button>

          <button
              disabled={isAuthor}
              className={bug.userVoteType === 'DISLIKE' ? 'vote-dislike-active' : ''}
              onClick={() => handleVoteBug('DISLIKE')}
          >👎🏼</button>
          <span>{bug.score || 0}</span>

        </div>
        {isAuthor && (
          <div className="mt-1" style={{ display: 'flex', gap: '0.5rem' }}>
            <button className="btn btn-secondary btn-sm" onClick={() => navigate(`/bugs/${bug.id}/edit`)}>Editeaza</button>
            <button className="btn btn-danger btn-sm" onClick={handleDeleteBug}>Sterge</button>
            {bug.status !== 'SOLVED' && (
              <button className="btn btn-primary btn-sm" onClick={handleMarkSolved}>Marcheaza ca rezolvat</button>
            )}
          </div>
        )}
      </div>

      <h3 className="mt-2">Comentarii ({comments.length})</h3>

      <div>
        {comments.length === 0 ? (
            <p>Niciun comentariu inca.</p>
        ) : (
            comments.map(c => {
              // 1. Calculăm variabila AICI, după acoladă, dar înainte de return
              const isCommentAuthor = user && c.author && user.id === c.author.id;

              // 2. Trebuie să adăugăm explicit "return"
              return (
                  <div key={c.id} className="comment">
                    <div className="flex-between">
                      <div className="meta">
                        {c.author?.username || 'Anonim'} &middot; {new Date(c.createdAt).toLocaleString()}
                      </div>
                      {isCommentAuthor && ( // Folosim variabila deja calculată
                          <div style={{ display: 'flex', gap: '0.3rem' }}>
                            {editingCommentId !== c.id && (
                                <button className="btn btn-secondary btn-sm" onClick={() => handleEditComment(c)}>Editeaza</button>
                            )}
                            <button className="btn btn-danger btn-sm" onClick={() => handleDeleteComment(c.id)}>Sterge</button>
                          </div>
                      )}
                    </div>

                    {c.imageUrl && (
                        <img src={`http://localhost:8080${c.imageUrl}`} alt="comment image" style={{ maxWidth: '100%', margin: '0.3rem 0', borderRadius: '4px' }} />
                    )}

                    {editingCommentId === c.id ? (
                        <div className="mt-1">
              <textarea
                  value={editCommentText}
                  onChange={e => setEditCommentText(e.target.value)}
                  style={{ width: '100%', minHeight: '60px' }}
              />
                          <div style={{ display: 'flex', gap: '0.3rem', marginTop: '0.3rem' }}>
                            <button className="btn btn-primary btn-sm" onClick={() => handleSaveComment(c.id)}>Salveaza</button>
                            <button className="btn btn-sm" onClick={handleCancelEdit} style={{ background: '#ccc' }}>Anuleaza</button>
                          </div>
                        </div>
                    ) : (
                        <p className="mt-1">{c.text}</p>
                    )}

                    <div className="comment-vote-controls">
                      <button
                          disabled={isCommentAuthor}
                          className={c.userVoteType === 'LIKE' ? 'vote-like-active' : ''}
                          onClick={() => handleVoteComment(c.id, 'LIKE')}
                          title={isCommentAuthor ? "Nu îți poți vota propriul comentariu" : ""}
                      >
                        👍🏼
                      </button>

                      <button
                          disabled={isCommentAuthor}
                          className={c.userVoteType === 'DISLIKE' ? 'vote-dislike-active' : ''}
                          onClick={() => handleVoteComment(c.id, 'DISLIKE')}
                          title={isCommentAuthor ? "Nu îți poți vota propriul comentariu" : ""}
                      >
                        👎🏼
                      </button>
                      <span>{c.score || 0}</span>
                    </div>
                  </div>
              );
            })
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
        <div className="form-group">
          <label>Imagine (optional)</label>
          <input
            type="file"
            accept="image/*"
            onChange={e => setCommentImageFile(e.target.files[0] || null)}
          />
        </div>
        <button className="btn btn-primary" onClick={handleAddComment}>Trimite</button>
        {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
      </div>
    </div>
  );
}

export default BugDetail;
