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
} from '@coreui/react';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    phone: '',
    fullname: '',
    gender: 'Nam',
    roleIds: [3],
    addresses: [
      {
        street: '',
        buildingName: '',
        city: '',
        state: '',
        country: '',
        pincode: ''
      }
    ],
    cart: {
      totalPrice: 0,
      cartItems: [],
      email: '', 
    },
    favorites: [],
    orders: [],
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => {
      const addresses = [...prevData.addresses];
      addresses[0] = { ...addresses[0], [name]: value };
      return { ...prevData, addresses };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Dữ liệu đăng ký gửi đi:', formData);
  
    // Chỉ gửi những field cần thiết
    const payload = {
      fullname: formData.fullname,
    email: formData.email,
    username: formData.username,
    phone: formData.phone,
    gender: formData.gender,
    password: formData.password,
    roleIds: formData.roleIds,
    addresses: formData.addresses,
    cart: {
      totalPrice: formData.cart.totalPrice,
      cartItems: [],
      email: formData.email, // backend yêu cầu có email
    },
    favorites: [],
    orders: []
    };
  
    try {
      const response = await fetch('http://localhost:8080/api/public/users/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });
  
      if (!response.ok) {
        let errorMessage = 'Đăng ký không thành công!';
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (err) {
          console.error('Lỗi khi đọc JSON từ server:', err);
        }
        throw new Error(errorMessage);
      }
  
      const data = await response.json();
      console.log('User registered:', data);
      localStorage.setItem('user', JSON.stringify(data));
      navigate('/dang-nhap');
    } catch (err) {
      setError(err.message);
    }
  };
  

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#f4f4f4',
      }}
    >
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={8} lg={6}> {/* Thay đổi size để làm cho form rộng hơn */}
            <CCard
              style={{
                padding: '30px',
                borderRadius: '20px',
                boxShadow: '0px 12px 24px rgba(0, 0, 0, 0.1)',
                background: '#fff',
              }}
            >
              <CCardBody>
                <h2 className="text-center fw-bold mb-4 text-primary">Đăng Ký</h2>
                {error && <p className="text-danger text-center">{error}</p>}
                <CForm onSubmit={handleSubmit}>
                  {['username', 'email', 'password', 'phone', 'fullname'].map((field) => (
                    <CFormInput
                      key={field}
                      type={field === 'password' ? 'password' : 'text'}
                      placeholder={
                        field === 'username' ? 'Tên đăng nhập' :
                        field === 'email' ? 'Email' :
                        field === 'password' ? 'Mật khẩu' :
                        field === 'phone' ? 'Số điện thoại' :
                        'Họ và tên'
                      }
                      name={field}
                      value={formData[field]}
                      onChange={handleChange}
                      className="mb-3"
                      required
                      style={{
                        fontSize: '16px',
                        padding: '14px',
                        border: '1px solid #ccc',
                        borderRadius: '0 10px 10px 0',
                      }}
                    />
                  ))}
                  <select
                    name="gender"
                    value={formData.gender}
                    onChange={handleChange}
                    className="form-select mb-3"
                    required
                    style={{
                      fontSize: '16px',
                      padding: '14px',
                      border: '1px solid #ccc',
                      borderRadius: '0 10px 10px 0',
                    }}
                  >
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                    <option value="Khác">Khác</option>
                  </select>

                  <h5 className="mb-3">Thông tin địa chỉ</h5>
                  {['street', 'buildingName', 'city', 'state', 'country', 'pincode'].map((field) => (
                    <CFormInput
                      key={field}
                      type="text"
                      placeholder={
                        field === 'street' ? 'Street' :
                        field === 'buildingName' ? 'Building Name' :
                        field === 'city' ? 'City' :
                        field === 'state' ? 'State' :
                        field === 'country' ? 'Country' :
                        'Pincode'
                      }
                      name={field}
                      value={formData.addresses[0][field]}
                      onChange={handleAddressChange}
                      className="mb-3"
                      required
                      style={{
                        fontSize: '16px',
                        padding: '14px',
                        border: '1px solid #ccc',
                        borderRadius: '0 10px 10px 0',
                      }}
                    />
                  ))}

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
                    Đăng Ký
                  </CButton>
                </CForm>

                <div className="text-center mt-3">
                  <p className="mb-1 text-muted">Đã có tài khoản?</p>
                  <CButton
                    color="link"
                    onClick={() => navigate('/dang-nhap')}
                    style={{
                      fontSize: '14px',
                      textDecoration: 'none',
                      color: '#667eea',
                      fontWeight: '500',
                    }}
                    onMouseOver={(e) => (e.target.style.color = '#6b46c1')}
                    onMouseOut={(e) => (e.target.style.color = '#667eea')}
                  >
                    Đăng nhập ngay
                  </CButton>
                </div>
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  );
};

export default Register;
