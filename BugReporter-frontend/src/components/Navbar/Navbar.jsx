import { Link } from 'react-router-dom';
import logo from '../../assets/logo.png';
import writeIcon from '../../assets/write-icon.png';
import './Navbar.css';

function Navbar({ user, onLogout }) {
  return (
    <nav className="navbar">
      <Link to="/bugs" className="brand">
        <img src={logo} alt="TrackMyBug" className="navbar-logo" />
      </Link>
      <div className="nav-links">
        <Link to="/bugs">Bugs</Link>
        <Link to="/create-bug" className="write-btn" title="Raporteaza bug">
          <img src={writeIcon} alt="Write" className="write-icon" />
          <span>Write</span>
        </Link>
        <Link to="/profile" className="username-link">{user.username}</Link>
      </div>
    </nav>
  );
}

export default Navbar;
