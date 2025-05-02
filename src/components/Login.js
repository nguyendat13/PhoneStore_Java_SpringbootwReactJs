import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  CButton,
  CCard,
  CCardBody,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CRow,
  CInputGroup,
  CInputGroupText,
} from '@coreui/react';
import { FaUser, FaLock } from 'react-icons/fa';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: username, password: password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Tên đăng nhập hoặc mật khẩu không đúng!');
      }

      const data = await response.json();
      localStorage.setItem('token', data.token);

      localStorage.setItem('user', JSON.stringify(data));
      navigate('/profile');
      window.location.reload();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <CContainer style={{ paddingTop: '50px', minHeight: '100vh' }}>
      <CRow
        className="justify-content-center align-items-center"
        style={{ height: '100%' }}
      >
        <CCol md={8} lg={6}>
          <CCard
            style={{
              padding: '40px',
              borderRadius: '20px',
              boxShadow: '0 12px 24px rgba(0,0,0,0.1)',
              backgroundColor: '#ffffff',
            }}
          >
            <CCardBody>
              <h2 className="text-center fw-bold mb-4 text-primary">Đăng Nhập</h2>
              {error && <p className="text-danger text-center">{error}</p>}
              <CForm onSubmit={handleLogin}>
                <CInputGroup className="mb-4">
                  <CInputGroupText style={{ backgroundColor: '#f0f0f0' }}>
                    <FaUser color="#555" />
                  </CInputGroupText>
                  <CFormInput
                    type="text"
                    placeholder="Tên đăng nhập"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    style={{
                      fontSize: '16px',
                      padding: '14px',
                      border: '1px solid #ccc',
                      borderRadius: '0 10px 10px 0',
                    }}
                  />
                </CInputGroup>

                <CInputGroup className="mb-4">
                  <CInputGroupText style={{ backgroundColor: '#f0f0f0' }}>
                    <FaLock color="#555" />
                  </CInputGroupText>
                  <CFormInput
                    type="password"
                    placeholder="Mật khẩu"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    style={{
                      fontSize: '16px',
                      padding: '14px',
                      border: '1px solid #ccc',
                      borderRadius: '0 10px 10px 0',
                    }}
                  />
                </CInputGroup>

                <div className="text-center mb-3">
                  <CButton
                    type="submit"
                    className="w-100"
                    style={{
                      padding: '14px',
                      fontSize: '18px',
                      borderRadius: '10px',
                      background: 'linear-gradient(90deg, #667eea, #764ba2)',
                      border: 'none',
                      color: '#fff',
                      fontWeight: 'bold',
                      transition: 'all 0.3s ease-in-out',
                    }}
                    onMouseOver={(e) =>
                      (e.target.style.background = 'linear-gradient(90deg, #5a67d8, #6b46c1)')
                    }
                    onMouseOut={(e) =>
                      (e.target.style.background = 'linear-gradient(90deg, #667eea, #764ba2)')
                    }
                  >
                    Đăng Nhập
                  </CButton>
                </div>

                <div className="text-center mt-3">
                  <p className="mb-1 text-muted">Chưa có tài khoản?</p>
                  <CButton
                    color="link"
                    onClick={() => navigate('/register')}
                    style={{
                      fontSize: '14px',
                      textDecoration: 'none',
                      color: '#667eea',
                      fontWeight: '500',
                    }}
                    onMouseOver={(e) => (e.target.style.color = '#6b46c1')}
                    onMouseOut={(e) => (e.target.style.color = '#667eea')}
                  >
                    Đăng ký ngay
                  </CButton>
                </div>
              </CForm>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </CContainer>
  );
};

export default Login;
