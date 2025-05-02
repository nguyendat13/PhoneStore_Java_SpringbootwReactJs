// src/pages/OrderSuccess.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";

const OrderSuccess = () => {
  const navigate = useNavigate();

  return (
    <div className="container text-center mt-5">
      <h2 className="text-success mb-4">🎉 Đặt hàng thành công!</h2>
      <p>Cảm ơn bạn đã mua sắm tại cửa hàng của chúng tôi.</p>

      <div className="d-flex justify-content-center gap-3 mt-4">
        <Button variant="primary" onClick={() => navigate("/")}>
          🏠 Về trang chủ
        </Button>
        <Button variant="success" onClick={() => navigate("/orders")}>
          📦 Đến đơn hàng của tôi
        </Button>
      </div>
    </div>
  );
};

export default OrderSuccess;