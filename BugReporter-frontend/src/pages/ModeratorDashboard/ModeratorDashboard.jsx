import { useState } from 'react';
import { searchUsers, banUser, unbanUser } from '../../api.js';

function ModeratorDashboard({ currentUser }) {
    const [query, setQuery] = useState('');
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!query.trim()) return;

        setLoading(true);
        setError('');
        try {
            const data = await searchUsers(query, currentUser);
            setUsers(data);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    const handleToggleBan = async (targetUser) => {
        const actionText = targetUser.banned ? 'DEBLOCHEZI' : 'BANEZI';
        if (!window.confirm(`Esti sigur că vrei să ${actionText} utilizatorul ${targetUser.username}?`)) return;

        try {
            if (targetUser.banned) {
                await unbanUser(targetUser.id, currentUser);
                alert('Utilizator deblocat!');
            } else {
                await banUser(targetUser.id, currentUser);
                alert('Utilizator banat!');
            }

            setUsers(prevUsers =>
                prevUsers.map(u => u.id === targetUser.id ? { ...u, banned: !u.banned } : u)
            );
        } catch (e) {
            alert(e.message);
        }
    };


    return (
        <div className="container">
            <h2>Restrictioneaza utilizatori</h2>
            <p style={{ color: '#6c757d' }}>Cauta utilizatori pentru a le gestiona statusul contului.</p>

            {/* Formularul de căutare */}
            <form onSubmit={handleSearch} style={{ display: 'flex', gap: '0.5rem', margin: '1.5rem 0' }}>
                <input
                    type="text"
                    placeholder="Introdu username..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    style={{ flex: 1, padding: '0.6rem', borderRadius: '4px', border: '1px solid #ccc' }}
                />
                <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Se caută...' : 'Caută'}
                </button>
            </form>

            {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}

            <div className="card" style={{ padding: '1rem', overflowX: 'auto' }}>
                {users.length === 0 ? (
                    <p style={{ textAlign: 'center', color: '#888', margin: '2rem 0' }}>
                        Niciun utilizator căutat sau găsit încă.
                    </p>
                ) : (
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '0.5rem' }}>
                        <thead>
                        <tr style={{ borderBottom: '2px solid #eee', textAlign: 'left' }}>
                            <th style={{ padding: '0.5rem' }}>Username</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th>Status</th>
                            <th>Acțiune</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map(u => (
                            <tr key={u.id} style={{ borderBottom: '1px solid #eee' }}>
                                <td style={{ padding: '0.8rem 0.5rem', fontWeight: '500' }}>{u.username}</td>
                                <td>{u.email}</td>
                                <td>
                    <span style={{
                        backgroundColor: u.role === 'MODERATOR' ? '#d9534f' : '#0275d8',
                        color: 'white',
                        padding: '2px 6px',
                        borderRadius: '4px',
                        fontSize: '0.8rem'
                    }}>
                      {u.role}
                    </span>
                                </td>
                                <td>
                                    {u.banned ? (
                                        <span style={{ color: '#d9534f', fontWeight: 'bold' }}>Banned</span>
                                    ) : (
                                        <span style={{ color: '#5cb85c', fontWeight: 'bold' }}>Activ</span>
                                    )}
                                </td>
                                <td>
                                    {u.id !== currentUser.id ? (
                                        <button
                                            className={`btn ${u.banned ? 'btn-primary' : 'btn-danger'} btn-sm`}
                                            onClick={() => handleToggleBan(u)}
                                            style={{ padding: '4px 10px' }}
                                        >
                                            {u.banned ? 'Unban' : 'Ban'}
                                        </button>
                                    ) : (
                                        <span style={{ color: '#888', fontStyle: 'italic' }}>You</span>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default ModeratorDashboard;