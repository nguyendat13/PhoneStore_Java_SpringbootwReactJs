import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { GoogleLogin } from '@react-oauth/google';
import {jwtDecode} from 'jwt-decode';  // Import jwt-decode đúng cách

const Profile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        // Lấy token từ localStorage
        const storedToken = localStorage.getItem('token'); 

        if (!storedToken) {
          setError('Không tìm thấy token người dùng. Vui lòng đăng nhập.');
          return;
        }

        // Giải mã token để lấy email
        const decoded = jwtDecode(storedToken);
        const email = decoded.email;

        if (!email) {
          setError('Không tìm thấy email trong token. Vui lòng đăng nhập lại.');
          return;
        }

        // Gọi API để lấy thông tin người dùng
        const response = await fetch(`http://localhost:8080/api/public/users/email/${email}`);
        if (!response.ok) throw new Error('Không thể lấy thông tin người dùng');

        const data = await response.json();
        localStorage.setItem('user', JSON.stringify(data));  // Lưu thông tin người dùng vào localStorage

        // Cập nhật state với thông tin người dùng
        setUser({
          fullname: data.fullname,
          email: data.email,
          phone: data.phone,
          gender: data.gender,
          username: data.username,
          addresses: data.addresses || []
        });

      } catch (err) {
        setError(err.message);
      }
    };

    fetchUser();
  }, []);

 

  const handleGoogleLoginSuccess = (credentialResponse) => {
    // Lưu token vào localStorage khi người dùng đăng nhập thành công
    localStorage.setItem("token", credentialResponse.credential);

    // Gọi lại useEffect để lấy thông tin người dùng
    window.location.reload();
  };

  const handleGoogleLoginError = () => {
    setError("Đăng nhập Google thất bại.");
  };

  if (error) return <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>;

  if (!user) {
    return (
      <div style={{ textAlign: 'center', marginTop: '50px' }}>
        <p>Đang tải...</p>

      
        {/* Google Login */}
        <GoogleLogin
          onSuccess={handleGoogleLoginSuccess}
          onError={handleGoogleLoginError}
        />
      </div>
    );
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>👤 Hồ sơ người dùng</h2>
        <div style={styles.infoGroup}>
          <p><strong>Tên:</strong> {user.fullname || 'N/A'}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>UserName:</strong> {user.username}</p>
          <p><strong>SĐT:</strong> {user.phone || 'N/A'}</p>
          <p><strong>Giới tính:</strong> {user.gender || 'N/A'}</p>
        </div>

        {/* Địa chỉ */}
        <div style={{ marginTop: '20px' }}>
          <h3 style={styles.subtitle}>🏠 Địa chỉ</h3>
          {user.addresses.length > 0 ? (
            user.addresses.map((addr, idx) => (
              <div key={idx} style={styles.addressCard}>
                <p><strong>Đường:</strong> {addr.street}</p>
                <p><strong>Tòa nhà:</strong> {addr.buildingName}</p>
                <p><strong>Thành phố:</strong> {addr.city}</p>
                <p><strong>Tỉnh:</strong> {addr.state}</p>
                <p><strong>Quốc gia:</strong> {addr.country}</p>
                <p><strong>Mã bưu chính:</strong> {addr.pincode}</p>
              </div>
            ))
          ) : <p>Không có địa chỉ.</p>}
        </div>

        <button
          style={styles.button}
          onClick={() => navigate("/update-profile")}
        >
          ✏️ Cập nhật thông tin
        </button>
      </div>
    </div>
  );
};

const styles = {
  container: {
    backgroundColor: '#f2f4f8',
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '40px',
  },
  card: {
    backgroundColor: '#ffffff',
    padding: '30px',
    borderRadius: '16px',
    boxShadow: '0 8px 24px rgba(0,0,0,0.1)',
    maxWidth: '600px',
    width: '100%',
  },
  title: {
    marginBottom: '20px',
    color: '#2c3e50',
    textAlign: 'center'
  },
  subtitle: {
    marginBottom: '10px',
    color: '#34495e',
  },
  infoGroup: {
    lineHeight: '1.6',
    color: '#333',
  },
  addressCard: {
    backgroundColor: '#f9f9f9',
    padding: '15px',
    borderRadius: '10px',
    border: '1px solid #ddd',
    marginBottom: '15px',
    lineHeight: '1.6'
  },
  button: {
    marginTop: '20px',
    backgroundColor: '#007BFF',
    color: '#fff',
    padding: '10px 18px',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    cursor: 'pointer',
    width: '100%',
    transition: 'background 0.3s',
  },
  form: {
    marginBottom: '20px'
  },
  inputGroup: {
    marginBottom: '10px',
    textAlign: 'left'
  }
};

export default Profile;
