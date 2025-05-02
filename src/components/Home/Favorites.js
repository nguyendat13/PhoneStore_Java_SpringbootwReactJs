import React, { useEffect, useState } from "react";
import axios from "axios";

const styles = {
  container: {
    padding: "16px",
    backgroundColor: "#f0f4f8",
    minHeight: "100vh",
  },
  card: {
    padding: "24px",
    backgroundColor: "#fff",
    boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
    borderRadius: "16px",
    maxWidth: "800px",
    margin: "auto",
  },
  header: {
    paddingBottom: "16px",
    borderBottom: "1px solid #e0e0e0",
    marginBottom: "16px",
  },
  title: {
    fontSize: "24px",
    fontWeight: "bold",
    color: "#333",
  },
  productList: {
    display: "flex",
    flexDirection: "column",
    gap: "16px",
  },
  productItem: {
    display: "flex",
    alignItems: "center",
    padding: "16px",
    backgroundColor: "#f9fafb",
    borderRadius: "12px",
    transition: "transform 0.3s, box-shadow 0.3s",
    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.05)",
  },
  productItemHover: {
    transform: "scale(1.02)",
    boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
  },
  productImage: {
    width: "60px",
    height: "60px",
    borderRadius: "8px",
    marginRight: "16px",
  },
  productInfo: {
    flex: 1,
  },
  productName: {
    fontWeight: "bold",
    color: "#222",
  },
  productPrice: {
    color: "#555",
  },
  removeButton: {
    padding: "8px 16px",
    color: "#f44336",
    border: "1px solid #f44336",
    borderRadius: "8px",
    backgroundColor: "transparent",
    cursor: "pointer",
    transition: "all 0.3s",
  },
};

const ProductItem = ({ product, onRemove }) => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div
      style={{
        ...styles.productItem,
        ...(isHovered ? styles.productItemHover : {}),
      }}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <img
        src={`http://localhost:8080/api/public/products/image/${product.image}`}
        alt={product.productName}
        className="product-image"
        style={{
          width: "200px",
          height: "200px",
          objectFit: "cover",
          borderRadius: "10px",
        }}
      />
      <div style={styles.productInfo}>
        <p style={styles.productName}>{product.productName}</p>
        <p style={styles.productPrice}>
          Giá: {product.price.toLocaleString()} VND
        </p>
      </div>
      <button
        style={styles.removeButton}
        onClick={() => onRemove(product.productId)}
      >
        Xóa khỏi yêu thích
      </button>
    </div>
  );
};

const Favorites = () => {
  const [favorites, setFavorites] = useState([]);
  const user = JSON.parse(localStorage.getItem("user"));

  const fetchFavorites = () => {
    if (!user?.userId) return;

    axios
      .get(`http://localhost:8080/api/public/favorites/user/${user.userId}`)
      .then((res) => setFavorites(res.data))
      .catch((err) => console.error("Lỗi khi lấy danh sách yêu thích:", err));
  };

  useEffect(() => {
    fetchFavorites();
  }, []);

  const handleRemove = async (productId) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/public/favorites/user/${user.userId}/product/${productId}`
      );
      setFavorites((prev) => prev.filter((p) => p.productId !== productId));
    } catch (err) {
      console.error("Lỗi khi xoá yêu thích:", err);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <div style={styles.header}>
          <h2 style={styles.title}>Sản phẩm yêu thích</h2>
        </div>
        <div style={styles.productList}>
          {favorites.length > 0 ? (
            favorites.map((product) => (
              <ProductItem
                key={product.productId}
                product={product}
                onRemove={handleRemove}
              />
            ))
          ) : (
            <p>Không có sản phẩm yêu thích nào.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Favorites;
