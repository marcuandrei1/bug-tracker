import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  return (
    <nav className="navbar">
      <Link to="/bugs" className="brand">Bug Reporter</Link>
      <div className="nav-links">
        <Link to="/bugs">Bugs</Link>
        <Link to="/create-bug">+ Raporteaza</Link>
      </div>
    </nav>
  );
}

export default Navbar;
