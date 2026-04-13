import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { loginUser } from '../../api';
import './Login.css';

function Login({ onLogin }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    setError('');
    if (!username || !password) {
      setError('Completeaza toate campurile');
      return;
    }
    try {
      const user = await loginUser(username, password);
      localStorage.setItem('user', JSON.stringify(user));
      onLogin(user);
      navigate('/bugs');
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div className="auth-wrapper">
      <h2>Login</h2>
      <div className="form-group">
        <label>Username</label>
        <input value={username} onChange={e => setUsername(e.target.value)} />
      </div>
      <div className="form-group">
        <label>Parola</label>
        <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
      </div>
      <button className="btn btn-primary" onClick={handleSubmit}>Intra in cont</button>
      {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
      <div className="switch">
        Nu ai cont? <Link to="/register">Inregistreaza-te</Link>
      </div>
    </div>
  );
}

export default Login;
