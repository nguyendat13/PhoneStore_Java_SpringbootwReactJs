import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../../../assets/css/confirmPage.css";
import baseURL from "../../../api/BaseUrl";
const ConfirmPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const paymentInfo = location.state?.payment;
  const { payment, paymentMethodName } = location.state || {};

  const handleCreateOrder = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const token = localStorage.getItem("token");
      const userId = user?.userId;

      if (!userId || !token) {
        alert("Không tìm thấy thông tin người dùng hoặc token.");
        return;
      }

      const response = await fetch(`${baseURL}/public/order/${userId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Tạo đơn hàng thất bại");
      }

      const orderData = await response.json();
      alert("Đặt hàng thành công!");

      // Xóa payment sau khi đặt hàng
      await fetch(`${baseURL}/public/payment-cancel/${paymentInfo.id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      const payments = JSON.parse(localStorage.getItem("payments")) || [];
      const updatedPayments = payments.filter(p => p.id !== paymentInfo.id);
      localStorage.setItem("payments", JSON.stringify(updatedPayments));

      navigate("/order-success", { state: { order: orderData } });
    } catch (error) {
      console.error("Lỗi khi tạo đơn hàng:", error);
      alert("Có lỗi xảy ra, vui lòng thử lại sau.");
    }
  };

  const handleCancelPayment = async () => {
    try {
      const token = localStorage.getItem("token");

      if (!paymentInfo?.id || !token) {
        alert("Không tìm thấy thông tin thanh toán hoặc token.");
        return;
      }

      const response = await fetch(`${baseURL}/public/payment-cancel/${paymentInfo.id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Hủy thanh toán thất bại");
      }

      const payments = JSON.parse(localStorage.getItem("payments")) || [];
      const updatedPayments = payments.filter(p => p.id !== paymentInfo.id);
      localStorage.setItem("payments", JSON.stringify(updatedPayments));

      alert("Hủy thanh toán thành công!");
      navigate("/");
    } catch (error) {
      console.error("Lỗi khi hủy thanh toán:", error);
      alert("Có lỗi xảy ra khi hủy, vui lòng thử lại sau.");
    }
  };

  if (!paymentInfo) {
    return <div>Không có thông tin thanh toán.</div>;
  }

  return (
    <div className="confirm-container">
      <h2>Xác nhận thanh toán</h2>
      <div className="confirm-details">
        <p><strong>Người nhận:</strong> {paymentInfo.fullname}</p>
        <p><strong>Email:</strong> {paymentInfo.email}</p>
        <p><strong>Địa chỉ:</strong> {paymentInfo.address}</p>
        <p><strong>Số điện thoại:</strong> {paymentInfo.phone}</p>
        <p>Phương thức thanh toán: {paymentMethodName}</p>
        <p><strong>Tổng tiền:</strong> {paymentInfo.paymentAmount.toLocaleString()} VNĐ</p>
      </div>
      <div className="confirm-buttons">
        <button className="confirm-button" onClick={handleCreateOrder}>Xác nhận đặt hàng</button>
        <button className="cancel-button" onClick={handleCancelPayment}>Hủy</button>
      </div>
    </div>
  );
};

export default ConfirmPage;