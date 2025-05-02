import axios from "axios";

export const handleAddToCart = async (product) => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user || !user.userId) {
    alert("Vui lòng đăng nhập để thêm vào giỏ hàng.");
    return;
  }

  try {
    // Gửi request đến API
    await axios.post("http://localhost:8080/api/public/cart/add", {
      userId: user.userId,
      productId: product.productId,
      quantity: 1,
    });

    // Cập nhật localStorage
    let localCart = JSON.parse(localStorage.getItem("cart")) || [];
    const existingIndex = localCart.findIndex(
      (item) => item.productId === product.productId
    );

    if (existingIndex !== -1) {
      localCart[existingIndex].quantity += 1;
    } else {
      localCart.push({ ...product, quantity: 1 });
    }

    localStorage.setItem("cart", JSON.stringify(localCart));
    alert(`Đã thêm sản phẩm ${product.productName} vào giỏ hàng!`);
    window.location.reload(); // có thể bỏ nếu muốn kiểm soát tốt hơn ở component gọi
  } catch (error) {
    console.error("Lỗi khi thêm vào giỏ hàng:", error);
    alert("Thêm vào giỏ hàng thất bại!");
  }
};
