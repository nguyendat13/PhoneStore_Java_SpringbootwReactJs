import React, { useEffect, useState } from "react";
import axios from "axios";
import CountdownBox from "./CountdownBox ";
import { IonIcon } from "@ionic/react";
import "../../../assets/css/DealOfDayProduct.css"; // Import file CSS
import { bagAddOutline, star, starOutline } from "ionicons/icons";
import { handleAddToCart } from "../../../services/cartService";
import baseURL from "../../../api/BaseUrl";
function DealOfDayProduct() {
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSaleProduct = async () => {
      try {
        const response = await axios.get(
          `${baseURL}/public/products/sale?pageNumber=1&pageSize=1&sortBy=price&sortOrder=desc`
        );
        const data = response.data;

        if (data?.content?.length > 0) {
          setProduct(data.content[0]);
        } else {
          setError("Không có sản phẩm khuyến mãi nào");
        }
      } catch (err) {
        setError(
          `Lỗi khi gọi API: ${err.response?.data?.message || err.message}`
        );
      } finally {
        setLoading(false);
      }
    };

    fetchSaleProduct();
  }, []);
 

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>{error}</div>;
  if (!product) return <div>Không tìm thấy sản phẩm khuyến mãi.</div>;

  return (
    <div className="product-featured">
      <h2 className="title">Sản phẩm khuyến mãi</h2>
      <div className="showcase-container">
        <div className="showcase">
          <div className="showcase-banner">
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "320px", // cao hơn ảnh chút để canh giữa theo chiều dọc
              }}
            >
              <img
                src={`${baseURL}/public/products/image/${product.image}`}
                alt={product.productName}
                className="product-image"
                style={{
                  width: "300px",
                  height: "300px",
                  objectFit: "cover",
                  borderRadius: "10px",
                }}
              />
            </div>
          </div>
          <div className="showcase-content">
            <div className="showcase-rating">
              <IonIcon icon={star} />
              <IonIcon icon={star} />
              <IonIcon icon={star} />
              <IonIcon icon={starOutline} />
              <IonIcon icon={starOutline} />
            </div>
            <h3 className="showcase-title">{product.productName}</h3>
            <p className="showcase-desc" style={{ fontSize: 14.8 }}>
              {product.productDescription}
            </p>
            <div className="price-box">
              {product.priceSale > 0 ? (
                <p className="price">
                  {product.priceSale.toLocaleString("vi-VN")}₫{" "}
                  <del>{product.price.toLocaleString("vi-VN")}₫</del>
                </p>
              ) : (
                <p className="price">
                  {product.price.toLocaleString("vi-VN")}₫
                </p>
              )}
            </div>
            <button
              onClick={() => handleAddToCart(product)}
              className="button-add-to-cart"
            >
              Thêm vào giỏ hàng
            </button>
            <div className="showcase-status">
              <div className="wrapper">
                <p>
                  Số lượng còn lại: <b>{product.quantity || 0}</b>
                </p>
              </div>

              <div className="showcase-status-bar" />
            </div>
            <CountdownBox />
            {/* <div className="countdown-box">
              <p className="countdown-desc">Nhanh tay! Ưu đãi kết thúc sau:</p>
              <div className="countdown">
                <div className="countdown-content">
                  <p className="display-number">01</p>
                  <p className="display-text">Ngày</p>
                </div>
                <div className="countdown-content">
                  <p className="display-number">12</p>
                  <p className="display-text">Giờ</p>
                </div>
                <div className="countdown-content">
                  <p className="display-number">30</p>
                  <p className="display-text">Phút</p>
                </div>
                <div className="countdown-content">
                  <p className="display-number">00</p>
                  <p className="display-text">Giây</p>
                </div>
              </div>
            </div> */}
          </div>
        </div>
      </div>
    </div>
  );
}

export default DealOfDayProduct;
