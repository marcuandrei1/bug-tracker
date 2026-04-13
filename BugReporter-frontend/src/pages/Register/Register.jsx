import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser } from '../../api';
import './Register.css';

function Register({ onLogin }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    setError('');
    if (!username || !email || !password) {
      setError('Completeaza toate campurile');
      return;
    }
    try {
      const user = await registerUser(username, email, password);
      localStorage.setItem('user', JSON.stringify(user));
      onLogin(user);
      navigate('/bugs');
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div className="auth-wrapper">
      <h2>Inregistrare</h2>
      <div className="form-group">
        <label>Username</label>
        <input value={username} onChange={e => setUsername(e.target.value)} />
      </div>
      <div className="form-group">
        <label>Email</label>
        <input type="email" value={email} onChange={e => setEmail(e.target.value)} />
      </div>
      <div className="form-group">
        <label>Parola</label>
        <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
      </div>
      <button className="btn btn-primary" onClick={handleSubmit}>Creeaza cont</button>
      {error && <p style={{ color: 'red', marginTop: '0.5rem' }}>{error}</p>}
      <div className="switch">
        Ai deja cont? <Link to="/login">Logheaza-te</Link>
      </div>
    </div>
  );
}

export default Register;
