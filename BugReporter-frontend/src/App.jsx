import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar.jsx';
import Bugs from './pages/Bugs/Bugs.jsx';
import BugDetail from './pages/BugDetail/BugDetail.jsx';
import CreateBug from './pages/CreateBug/CreateBug.jsx';

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/bugs" replace />} />
        <Route path="/bugs" element={<Bugs />} />
        <Route path="/bugs/:id" element={<BugDetail />} />
        <Route path="/create-bug" element={<CreateBug />} />
        <Route path="*" element={
          <div className="container">
            <h2>Pagina nu a fost gasita</h2>
          </div>
        } />
      </Routes>
    </>
  );
}

export default App;
