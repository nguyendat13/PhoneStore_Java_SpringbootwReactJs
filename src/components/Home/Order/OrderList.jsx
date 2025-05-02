import React, { useEffect, useState } from "react";
import axios from "axios";
import { Button, Card, Table, Badge } from "react-bootstrap";
import "../../../assets/css/order1.css";

const TABS = [
  { label: "Tất cả", value: "Tất cả", color: "secondary" },
  { label: "🕓 Chờ xác nhận", value: "Chờ xác nhận", color: "info" },
  { label: "📦 Đang xử lý", value: "Đang xử lý", color: "warning" },
  { label: "✅ Đã thanh toán", value: "Đã thanh toán", color: "success" },
  { label: "❌ Đã hủy", value: "Đã hủy", color: "danger" },
];

const statusColors = {
  "Đang xử lý": "warning",
  "Chờ xác nhận": "info",
  "Đã thanh toán": "success",
  "Đã hủy": "danger",
};

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [filteredOrders, setFilteredOrders] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("Tất cả");

  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  useEffect(() => {
    if (!userId) return;

    axios
      .get(`http://localhost:8080/api/public/order/user/${userId}`)
      .then((res) => {
        setOrders(res.data);
        setFilteredOrders(res.data);
      });
  }, [userId]);

  useEffect(() => {
    if (selectedStatus === "Tất cả") {
      setFilteredOrders(orders);
    } else {
      const filtered = orders.filter(
        (order) => order.orderStatus === selectedStatus
      );
      setFilteredOrders(filtered);
    }
  }, [selectedStatus, orders]);

  const formatCurrency = (value) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);

  const formatDate = (dateStr) =>
    new Date(dateStr).toLocaleDateString("vi-VN");

  return (
    <div className="container my-5">
      <h2 className="mb-4 text-primary">📋 Danh sách đơn hàng</h2>

      {/* Tabs */}
      <div className="order-tab-wrapper mb-4">
        {TABS.map((tab) => (
          <button
            key={tab.value}
            className={`order-tab-tab btn btn-outline-${tab.color} ${
              selectedStatus === tab.value ? `active btn-${tab.color}` : ""
            }`}
            onClick={() => setSelectedStatus(tab.value)}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Danh sách đơn */}
      {filteredOrders.length === 0 ? (
        <div className="alert alert-warning text-center">
          Không có đơn hàng nào thuộc trạng thái "
          <strong>{selectedStatus}</strong>".
        </div>
      ) : (
        filteredOrders.map((order) => (
          <Card key={order.orderId} className="mb-4 shadow-sm">
            <Card.Header className="bg-light d-flex justify-content-between">
              <span className="fw-bold">🧾 Mã đơn: #{order.orderId}</span>
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
                      <th>Thanh toán</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.orderItems.map((item) => (
                      <tr key={item.orderItemId}>
                        <td>
                          <img
                            src={`http://localhost:8080/api/public/products/image/${encodeURIComponent(item.productImage || "default.png")}`}
                            alt={item.productName}
                            style={{ width: "80px", height: "80px", objectFit: "contain" }}
                            className="img-thumbnail"
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

              <div className="text-end">
                <strong className="text-danger">
                  💰 Tổng tiền: {formatCurrency(order.totalAmount)}
                </strong>
              </div>
            </Card.Body>
          </Card>
        ))
      )}
    </div>
  );
};

export default OrderList;
