import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser } from '../../api';
import './Register.css';

function Register({ onLogin }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    setError('');
    if (!username || !email || !password || !phone) {
      setError('Completeaza toate campurile');
      return;
    }
    try {
      const user = await registerUser(username, email, password, phone);
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
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Utilizator</label>
                    <input type="text" value={username} onChange={e => setUsername(e.target.value)} />
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input type="email" value={email} onChange={e => setEmail(e.target.value)} />
                </div>

                <div className="form-group">
                    <label>Numar de telefon</label>
                    <input
                        type="text"
                        placeholder="+407xxxxxxxx"
                        value={phone}
                        onChange={e => setPhone(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label>Parola</label>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
                </div>

                <button type="submit" className="btn btn-primary">Creeaza cont</button>
            </form>

            {error && <p className="error" style={{ color: 'red' }}>{error}</p>}

            <div className="switch">
                Ai deja cont? <Link to="/login">Logheaza-te aici</Link>
            </div>
        </div>
    );
}

export default Register;
