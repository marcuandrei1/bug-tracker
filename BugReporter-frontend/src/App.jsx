import { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar.jsx';
import Bugs from './pages/Bugs/Bugs.jsx';
import BugDetail from './pages/BugDetail/BugDetail.jsx';
import CreateBug from './pages/CreateBug/CreateBug.jsx';
import EditBug from './pages/EditBug/EditBug.jsx';
import Profile from './pages/Profile/Profile.jsx';
import Login from './pages/Login/Login.jsx';
import Register from './pages/Register/Register.jsx';

function App() {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });

  const handleLogin = (u) => setUser(u);

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };

  if (!user) {
    return (
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register onLogin={handleLogin} />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  return (
    <>
      <Navbar user={user} onLogout={handleLogout} />
      <Routes>
        <Route path="/" element={<Navigate to="/bugs" replace />} />
        <Route path="/bugs" element={<Bugs user={user} />} />
        <Route path="/bugs/:id" element={<BugDetail user={user} />} />
        <Route path="/bugs/:id/edit" element={<EditBug user={user} />} />
        <Route path="/create-bug" element={<CreateBug user={user} />} />
        <Route path="/profile" element={<Profile user={user} onLogout={handleLogout} />} />
        <Route path="*" element={<Navigate to="/bugs" replace />} />
      </Routes>
    </>
  );
}

export default App;
