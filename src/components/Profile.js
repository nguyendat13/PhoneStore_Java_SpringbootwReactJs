import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        if (!storedUser || !storedUser.email) {
          setError('Không tìm thấy email người dùng. Vui lòng đăng nhập.');
          return;
        }

        const response = await fetch(`http://localhost:8080/api/public/users/email/${storedUser.email}`);
        if (!response.ok) throw new Error('Không thể lấy thông tin người dùng');

        const data = await response.json();
        localStorage.setItem('user', JSON.stringify(data));

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

  if (error) return <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>;
  if (!user) return <p style={{ textAlign: 'center' }}>Đang tải...</p>;

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
  }
};

export default Profile;
