import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaUser, FaLock } from 'react-icons/fa';
import { FcGoogle } from 'react-icons/fc';
import '../assets/css/Login.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: username, password }),
      });

      if (!res.ok) {
        const data = await res.json();
        throw new Error(data.message || 'Đăng nhập thất bại');
      }

      const data = await res.json();
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data));
      navigate('/profile');
      window.location.reload();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleLoginWithGoogle = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="login-wrapper">
      <form className="login-box" onSubmit={handleLogin}>
        <h2>Đăng Nhập</h2>
        {error && <p className="error">{error}</p>}

        <div className="input-group">
          <FaUser />
          <input
            type="text"
            placeholder="Email"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="input-group">
          <FaLock />
          <input
            type="password"
            placeholder="Mật khẩu"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn-login">Đăng Nhập</button>

        <button type="button" className="btn-google" onClick={handleLoginWithGoogle}>
          <FcGoogle size={20} style={{ marginRight: 8 }} />
          Đăng nhập với Google
        </button>

        <p className="register-text">
          Chưa có tài khoản?{' '}
          <span onClick={() => navigate('/register')}>Đăng ký ngay</span>
        </p>
      </form>
    </div>
  );
};

export default Login;
