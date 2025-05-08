import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import baseURL from "../../../api/BaseUrl";
function UpdateProfile() {
  const navigate = useNavigate();
  const storedUser = JSON.parse(localStorage.getItem("user"));

  const [formData, setFormData] = useState({
    fullname: "",
    phone: "",
    gender: "",
    username: "",
    roleIds: [3],
    address: {
      street: "",
      buildingName: "",
      city: "",
      state: "",
      country: "",
      pincode: "",
    },
  });

  useEffect(() => {
    if (storedUser) {
      setFormData({
        fullname: storedUser.fullname || "",
        phone: storedUser.phone || "",
        gender: storedUser.gender || "",
        username: storedUser.username || "",
        roleIds: [3],
        address: storedUser.addresses?.[0] || {
          street: "",
          buildingName: "",
          city: "",
          state: "",
          country: "",
          pincode: "",
        },
      });
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name in formData.address) {
      setFormData({
        ...formData,
        address: {
          ...formData.address,
          [name]: value,
        },
      });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${baseURL}/public/users/${storedUser.userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...formData,
          email: storedUser.email,
          addresses: [formData.address],

        }),
      });

      if (response.ok) {
        alert("✅ Cập nhật thông tin thành công!");
        navigate("/profile");
      } else {
        alert("❌ Có lỗi xảy ra khi cập nhật.");
      }
    } catch (error) {
      console.error(error);
      alert("❌ Lỗi khi gửi yêu cầu cập nhật.");
    }
  };

  const inputStyle = {
    padding: "8px",
    width: "100%",
    marginTop: "4px",
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <h2>✏️ Cập nhật thông tin cá nhân</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "15px" }}>
          <label>Họ và tên:</label>
          <input type="text" name="fullname" value={formData.fullname} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Số điện thoại:</label>
          <input type="text" name="phone" value={formData.phone} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Giới tính:</label>
          <select name="gender" value={formData.gender} onChange={handleChange} style={inputStyle}>
            <option value="">Chọn giới tính</option>
            <option value="Nam">Nam</option>
            <option value="Nữ">Nữ</option>
            <option value="Khác">Khác</option>
          </select>
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Tên đăng nhập:</label>
          <input type="text" name="username" value={formData.username} onChange={handleChange} style={inputStyle} />
        </div>

        <h3 style={{ marginTop: "20px", marginBottom: "10px" }}>🏠 Địa chỉ</h3>
        <div style={{ marginBottom: "15px" }}>
          <label>Đường:</label>
          <input type="text" name="street" value={formData.address.street} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Tòa nhà:</label>
          <input type="text" name="buildingName" value={formData.address.buildingName} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Thành phố:</label>
          <input type="text" name="city" value={formData.address.city} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Tỉnh:</label>
          <input type="text" name="state" value={formData.address.state} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Quốc gia:</label>
          <input type="text" name="country" value={formData.address.country} onChange={handleChange} style={inputStyle} />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>Mã bưu chính:</label>
          <input type="text" name="pincode" value={formData.address.pincode} onChange={handleChange} style={inputStyle} />
        </div>

        <button type="submit" style={{ padding: "10px 20px", backgroundColor: "#007bff", color: "#fff", border: "none", cursor: "pointer" }}>
          Lưu thay đổi
        </button>
      </form>
    </div>
  );
}

export default UpdateProfile;
