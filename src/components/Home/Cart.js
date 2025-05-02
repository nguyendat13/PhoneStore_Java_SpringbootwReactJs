import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { QRCodeCanvas } from "qrcode.react"; // Use QRCodeCanvas here
import "../../assets/css/cart.css"
const CartPage = () => {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState(null);
  const [paymentQRCode, setPaymentQRCode] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCart();
    fetchPaymentMethods(); // Lấy danh sách phương thức thanh toán
  }, []);

  const fetchCart = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const cartId = user?.userId;

      if (cartId) {
        const response = await axios.get(
          `http://localhost:8080/api/public/cart/${cartId}`
        );
        setCart(response.data);

        // ✅ Cập nhật localStorage và gửi sự kiện
        localStorage.setItem(
          "cart",
          JSON.stringify(response.data.cartItems || [])
        );
        window.dispatchEvent(new Event("storageUpdate"));
      } else {
        console.error("Không tìm thấy userId trong localStorage");
      }
    } catch (error) {
      console.error("Lỗi khi lấy giỏ hàng:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchPaymentMethods = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/public/payments/methods"
      );
      setPaymentMethods(response.data);
    } catch (error) {
      console.error("Lỗi khi lấy phương thức thanh toán:", error);
    }
  };

  const handleQuantityChange = async (productId, newQuantity) => {
    if (newQuantity <= 0) return;
    const cartId = cart.cartId;
    try {
      await axios.put(
        `http://localhost:8080/api/public/carts/${cartId}/products/${productId}/quantity/${newQuantity}`
      );
      fetchCart(); // Cập nhật lại giỏ hàng
    } catch (error) {
      console.error("Lỗi khi cập nhật số lượng:", error);
    }
  };

  const handleDelete = async (productId) => {
    const cartId = cart.cartId;
    try {
      await axios.delete(
        `http://localhost:8080/api/public/carts/${cartId}/product/${productId}`
      );
      fetchCart();
    } catch (error) {
      console.error("Lỗi khi xóa sản phẩm:", error);
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);
  };

  const increaseQuantity = (productId, currentQuantity) => {
    const newQuantity = currentQuantity + 1;
    handleQuantityChange(productId, newQuantity);
  };

  const decreaseQuantity = (productId, currentQuantity) => {
    if (currentQuantity > 1) {
      const newQuantity = currentQuantity - 1;
      handleQuantityChange(productId, newQuantity);
    }
  };

  const handleCheckout = async () => {
    const user = JSON.parse(localStorage.getItem("user"));

    if (!user || !cart || !selectedPaymentMethod) {
      console.error("Thiếu thông tin user, giỏ hàng hoặc phương thức thanh toán");
      return;
    }

    const paymentData = {
      userId: user.userId,
      fullname: user.fullname,
      address: "", // có thể thay bằng form nhập nếu muốn
      phone: user.phone,
      paymentAmount: cart.totalPrice,
      paymentMethodId: selectedPaymentMethod.id,
      paymentStatusId: 1,
    };

    try {
      // Gửi yêu cầu thanh toán
      const response = await axios.post(
        "http://localhost:8080/api/public/checkout",
        paymentData
      );
      const savedPayment = response.data;

      // Tạo QR code thanh toán
      const paymentQRCodeData = generateQRCode(savedPayment);

      // Lưu QR code vào state
      setPaymentQRCode(paymentQRCodeData);

      // Gọi API để lấy danh sách thanh toán theo userId
      const paymentResponse = await axios.get(
        `http://localhost:8080/api/public/payments/user/${user.userId}`
      );
      const payments = paymentResponse.data;
      console.log("Danh sách thanh toán:", payments);

      // Lưu danh sách thanh toán vào localStorage
      localStorage.setItem("payments", JSON.stringify(payments));

      // Điều hướng sang trang xác nhận với thông tin thanh toán
      navigate("/confirm", { state: { payment: savedPayment,

        paymentMethodName: selectedPaymentMethod.name, // Thêm tên phương thức thanh toán

       } });
    } catch (error) {
      console.error("Lỗi khi thanh toán:", error);
    }
  };

  const generateQRCode = (payment) => {
    // Chỉnh sửa logic tạo QR code theo yêu cầu
    return `http://localhost:8080/api/public/payments/qr/${payment.paymentId}`;
  };

  if (loading) return <div>Đang tải...</div>;
  if (!cart || !cart.cartItems?.length)
    return <div>Không có sản phẩm trong giỏ hàng</div>;

  const styles = {
    container: {
      maxWidth: "800px",
      margin: "0 auto",
      padding: "20px",
      fontFamily: "Arial, sans-serif",
    },
    cartTitle: {
      fontSize: "28px",
      fontWeight: "bold",
      marginBottom: "20px",
      textAlign: "center",
    },
    cartItem: {
      display: "flex",
      border: "1px solid #ccc",
      borderRadius: "10px",
      padding: "10px",
      marginBottom: "15px",
      alignItems: "center",
    },
    image: {
      width: "120px",
      height: "120px",
      objectFit: "cover",
      marginRight: "20px",
      borderRadius: "10px",
    },
    info: {
      flex: 1,
    },
    name: {
      fontSize: "18px",
      fontWeight: "bold",
      marginBottom: "10px",
    },
    price: {
      fontSize: "16px",
      color: "#e91e63",
      marginBottom: "10px",
    },
    quantityWrapper: {
      display: "flex",
      alignItems: "center",
      marginBottom: "10px",
    },
    quantityButton: {
      backgroundColor: "#4caf50",
      color: "white",
      border: "none",
      padding: "5px 10px",
      borderRadius: "5px",
      cursor: "pointer",
      margin: "0 5px",
    },
    quantityInput: {
      width: "60px",
      textAlign: "center",
      padding: "5px",
      marginLeft: "10px",
    },
    deleteButton: {
      backgroundColor: "#f44336",
      color: "white",
      border: "none",
      padding: "8px 12px",
      borderRadius: "5px",
      cursor: "pointer",
    },
    summary: {
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      marginTop: "30px",
      fontSize: "18px",
      fontWeight: "bold",
      padding: "10px",
      backgroundColor: "#f5f5f5",
      borderRadius: "10px",
    },
    checkoutButton: {
      backgroundColor: "#4caf50",
      color: "white",
      border: "none",
      padding: "12px 20px",
      fontSize: "16px",
      borderRadius: "8px",
      cursor: "pointer",
      marginTop: "20px",
      float: "right",
    },
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.cartTitle}>Giỏ hàng</h1>

      {cart.cartItems.map((item) => (
        <div style={styles.cartItem} key={item.cartItemId}>
          <img
            style={styles.image}
            src={`http://localhost:8080/api/public/products/image/${item.productImage}`}
            alt={item.productName}
          />
          <div style={styles.info}>
            <p style={styles.name}>{item.productName}</p>
            <p style={styles.price}>
              {formatCurrency(item.finalPrice * item.quantity)}
            </p>
            <div style={styles.quantityWrapper}>
              Số lượng:
              <button
                style={styles.quantityButton}
                onClick={() => decreaseQuantity(item.productId, item.quantity)}
              >
                -
              </button>
              <input
                type="number"
                min="1"
                value={item.quantity}
                style={styles.quantityInput}
                onChange={(e) =>
                  handleQuantityChange(item.productId, e.target.value)
                }
              />
              <button
                style={styles.quantityButton}
                onClick={() => increaseQuantity(item.productId, item.quantity)}
              >
                +
              </button>
            </div>
            <button
              style={styles.deleteButton}
              onClick={() => handleDelete(item.productId)}
            >
              Xóa
            </button>
          </div>
        </div>
      ))}

      <div>
        <label htmlFor="paymentMethod">Chọn phương thức thanh toán:</label>
        <select
          id="paymentMethod"
          onChange={(e) => setSelectedPaymentMethod(paymentMethods.find(pm => pm.id === parseInt(e.target.value)))}
          value={selectedPaymentMethod ? selectedPaymentMethod.id : ''}
        >
          <option value="">-- Chọn phương thức --</option>
          {paymentMethods.map((method) => (
            <option key={method.id} value={method.id}>
              {method.name}
            </option>
          ))}
        </select>
      </div>

      <div style={styles.summary}>
        <span>Tổng tiền:</span>
        <span>{formatCurrency(cart.totalPrice)}</span>
      </div>

      {paymentQRCode && (
        <div>
          <h3>QR Code Thanh Toán:</h3>
          <QRCodeCanvas value={paymentQRCode} />
        </div>
      )}

<button
  className="btn-checkout"
  onMouseOver={(e) => (e.target.style.backgroundColor = "#388e3c")}
  onMouseOut={(e) => (e.target.style.backgroundColor = "#4caf50")}
  onClick={handleCheckout}
>
  Thanh toán
</button>

    </div>
  );
};

export default CartPage;
