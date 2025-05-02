import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { FaArrowLeft } from "react-icons/fa";
import { handleAddToCart } from "../../../services/cartService";

const ProductDetail = () => {
  const { productId } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [relatedProducts, setRelatedProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  const styles = {
    container: {
      padding: "20px",
      maxWidth: "1200px",
      margin: "0 auto",
    },
    backButton: {
      display: "flex",
      alignItems: "center",
      color: "#1d4ed8",
      marginBottom: "20px",
      border: "none",
      background: "none",
      cursor: "pointer",
      fontSize: "16px",
    },
    productDetail: {
      display: "grid",
      gridTemplateColumns: "1fr 1fr",
      gap: "30px",
      background: "white",
      padding: "30px",
      borderRadius: "16px",
      boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
    },
    productImage: {
      width: "100%",
      height: "auto",
      borderRadius: "8px",
      objectFit: "cover",
    },
    productInfo: {
      display: "flex",
      flexDirection: "column",
    },
    relatedProducts: {
      marginTop: "40px",
    },
    relatedProductItem: {
      textDecoration: "none",
      color: "black",
      border: "1px solid #ddd",
      padding: "10px",
      borderRadius: "8px",
      transition: "transform 0.3s, box-shadow 0.3s",
      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
    },
    relatedProductItemHover: {
      transform: "scale(1.05)",
      boxShadow: "0 6px 15px rgba(0, 0, 0, 0.15)",
    },
    addToCartButton: {
      marginTop: "20px",
      padding: "10px 20px",
      backgroundColor: "#1d4ed8",
      color: "white",
      borderRadius: "8px",
      cursor: "pointer",
      border: "none",
      fontWeight: "500",
      transition: "background-color 0.3s, transform 0.3s",
    },
    addToCartButtonHover: {
      backgroundColor: "#2563eb",
      transform: "scale(1.05)",
    },
    relatedProductImage: {
      width: "100%",
      height: "180px",
      borderRadius: "8px",
      objectFit: "cover",
    },
    productName: {
      fontSize: "1.1rem",
      fontWeight: "500",
      margin: "10px 0",
    },
    priceSale: {
      color: "#dc2626",
      fontWeight: "500",
    },
    originalPrice: {
      textDecoration: "line-through",
      color: "#6b7280",
    },
  };

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const { data } = await axios.get(
          `http://localhost:8080/api/public/products/${productId}`
        );
        setProduct(data);

        const relatedResponse = await axios.get(
          `http://localhost:8080/api/public/products/${productId}/related`
        );
        setRelatedProducts(relatedResponse.data);
      } catch (error) {
        console.error("Lỗi khi lấy dữ liệu sản phẩm:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [productId]);
  

  
  const handleHover = (event) => {
    event.target.style.transform = "scale(1.05)";
    event.target.style.boxShadow = "0 6px 15px rgba(0, 0, 0, 0.15)";
  };

  const handleMouseOut = (event) => {
    event.target.style.transform = "scale(1)";
    event.target.style.boxShadow = "0 4px 8px rgba(0, 0, 0, 0.1)";
  };

  if (loading) {
    return (
      <div style={{ textAlign: "center", marginTop: "20px", fontSize: "20px" }}>
        Đang tải...
      </div>
    );
  }

  if (!product) {
    return (
      <div style={{ textAlign: "center", marginTop: "20px", fontSize: "20px" }}>
        Không tìm thấy sản phẩm!
      </div>
    );
  }

  return (
    <div style={styles.container}>
      <button onClick={() => navigate(-1)} style={styles.backButton}>
        <FaArrowLeft style={{ marginRight: "5px" }} /> Quay lại
      </button>

      <div style={styles.productDetail}>
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
          <h1>{product.productName}</h1>
          <p>{product.description}</p>
          <p style={styles.priceSale}>
            Giá: {product.price.toLocaleString("vi-VN")}₫
          </p>
          <p>Màu sắc: {product.color}</p>
          <p>Số lượng còn lại: {product.quantity}</p>
          <button
            style={styles.addToCartButton}
            onMouseEnter={(e) => (e.target.style.backgroundColor = "#2563eb")}
            onMouseLeave={(e) => (e.target.style.backgroundColor = "#1d4ed8")}
            onClick={() => handleAddToCart(product)}
          >
            Thêm vào giỏ hàng
          </button>
        </div>
      </div>
      {relatedProducts.length > 0 && (
        <div style={styles.relatedProducts}>
          <h2>Sản phẩm liên quan</h2>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
              gap: "20px",
            }}
          >
            {relatedProducts.slice(0, 4).map((relatedProduct) => (
              <div
                key={relatedProduct.productId}
                style={styles.relatedProductItem}
                onMouseEnter={handleHover}
                onMouseLeave={handleMouseOut}
              >
                <a
                  href={`/product/${relatedProduct.productId}`}
                  style={{ textDecoration: "none", color: "black" }}
                >
                  <img
                    src={`http://localhost:8080/api/public/products/image/${relatedProduct.image}`}
                    alt={relatedProduct.productName}
                    style={styles.relatedProductImage}
                  />
                  <h3 style={styles.productName}>
                    {relatedProduct.productName}
                  </h3>
                  <p style={styles.priceSale}>
                    Giá: {relatedProduct.price.toLocaleString("vi-VN")}₫
                  </p>
                </a>
                <button
                  style={styles.addToCartButton}
                  onClick={() => handleAddToCart(relatedProduct)}
                >
                  Thêm vào giỏ hàng
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductDetail;
