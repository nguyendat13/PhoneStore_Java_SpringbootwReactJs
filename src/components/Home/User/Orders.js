// src/pages/orders/OrderListByStatus.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card, Table, Badge } from "react-bootstrap";
import "../../../assets/css/order.css";

const statusColors = {
  "Đang xử lý": "warning",
  "Đã thanh toán": "success",
  "Đã hủy": "danger",
  "Chờ xác nhận": "info",
};

const OrderListByStatus = ({ statusFilter }) => {
  const [orders, setOrders] = useState([]);
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  useEffect(() => {
    if (!userId) return;

    axios
      .get(`http://localhost:8080/api/public/order/user/${userId}`)
      .then((res) => {
        const filtered = res.data.filter(
          (order) => order.orderStatus === statusFilter
        );
        setOrders(filtered);
      })
      .catch(() => setOrders([]));
  }, [userId, statusFilter]);

  const formatCurrency = (value) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const formatDate = (dateStr) =>
    new Date(dateStr).toLocaleDateString("vi-VN");

  return (
    <div className="container my-5">
      <h2 className="mb-4 text-primary">📦 Đơn hàng - {statusFilter}</h2>

      {orders.length === 0 ? (
        <div className="alert alert-warning text-center">
          Không có đơn hàng nào thuộc trạng thái "{statusFilter}".
        </div>
      ) : (
        orders.map((order) => (
          <Card
            key={order.orderId}
            className="mb-5 shadow-sm border-0 hover-card"
          >
            <Card.Header className="bg-light d-flex justify-content-between align-items-center">
              <span className="fw-bold">🧾 Mã đơn hàng: #{order.orderId}</span>
              <Badge bg={statusColors[order.orderStatus] || "secondary"}>
                {order.orderStatus}
              </Badge>
            </Card.Header>
            <Card.Body>
              <p><strong>📅 Ngày đặt:</strong> {formatDate(order.orderDate)}</p>
              <p><strong>👤 Khách hàng:</strong> {order.fullname}</p>
              <p><strong>📍 Địa chỉ:</strong> {order.address}</p>
              <p><strong>📞 Điện thoại:</strong> {order.phone}</p>

              <div className="table-responsive">
                <Table bordered hover className="text-center align-middle">
                  <thead className="table-dark">
                    <tr>
                      <th>Ảnh</th>
                      <th>Sản phẩm</th>
                      <th>SL</th>
                      <th>Giá</th>
                      <th>Giảm</th>
                      <th>PT Thanh toán</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.orderItems.map((item) => (
                      <tr key={item.orderItemId}>
                        <td>
                          <img
                            src={`http://localhost:8080/api/public/products/image/${encodeURIComponent(
                              item.productImage || "default.png"
                            )}`}
                            alt={item.productName}
                            className="img-thumbnail"
                            style={{
                              width: "80px",
                              height: "80px",
                              objectFit: "contain",
                              borderRadius: "10px",
                            }}
                          />
                        </td>
                        <td>{item.productName}</td>
                        <td>{item.quantity}</td>
                        <td>{formatCurrency(item.orderedProductPrice)}</td>
                        <td>{item.discount}%</td>
                        <td>{item.paymentMethod}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </div>

              <div className="text-end mt-3">
                <h5 className="text-danger">
                  💰 Tổng tiền: {formatCurrency(order.totalAmount)}
                </h5>
              </div>
            </Card.Body>
          </Card>
        ))
      )}
    </div>
  );
};

export default OrderListByStatus;
