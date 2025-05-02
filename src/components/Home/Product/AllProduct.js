// AllProduct.jsx

import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { IonIcon } from "@ionic/react";
import {
  heartOutline,
  eyeOutline,
  repeatOutline,
  bagAddOutline,
} from "ionicons/icons";
import { handleAddToCart } from "../../../services/cartService";

const API_BASE_URL = "http://localhost:8080/api";

const AllProduct = () => {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const productResponse = await axios.get(
          `${API_BASE_URL}/public/products`
        );
        const productData = Array.isArray(productResponse.data.content)
          ? productResponse.data.content
          : [];
        setProducts(productData);

        const brandResponse = await axios.get(`${API_BASE_URL}/public/brands`);
        if (brandResponse.data && Array.isArray(brandResponse.data.content)) {
          const brandMap = brandResponse.data.content.reduce((acc, brand) => {
            acc[brand.brandId] = brand.brandName;
            return acc;
          }, {});
          setBrands(brandMap);
        }
      } catch (error) {
        setError("Lỗi khi lấy dữ liệu sản phẩm.");
        console.error("Lỗi khi lấy dữ liệu sản phẩm:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

 

  const addToFavorites = async (productId) => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/favorites/${productId}`,
        {}
      );

      if (response.status === 200 || response.status === 201) {
        alert("Đã thêm vào danh sách yêu thích!");
      } else {
        alert("Không thể thêm vào yêu thích. Vui lòng thử lại.");
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert("Sản phẩm đã có trong danh sách yêu thích.");
      } else {
        console.error("Lỗi khi thêm vào yêu thích:", error);
        alert("Có lỗi xảy ra khi thêm vào yêu thích.");
      }
    }
  };

  if (loading) {
    return <div className="text-center">Đang tải sản phẩm...</div>;
  }

  if (error) {
    return <div className="text-center text-danger">{error}</div>;
  }

  return (
    <div className="product-box">
      <div className="product-main">
        <h2 className="text-center mb-4">Tất cả sản phẩm</h2>
        <div className="product-grid">
          {products.length > 0 ? (
            products.map((product) => (
              <div key={product.productId} className="showcase">
                <div className="showcase-banner">
                  <img
                    src={`http://localhost:8080/api/public/products/image/${encodeURIComponent(
                      product.image || "default.png"
                    )}`}
                    alt={product.productName}
                    className="product-image"
                    style={{
                      display: "block",
                      margin: "0 auto",
                      width: "200px",
                      height: "200px",
                      objectFit: "contain",
                      borderRadius: "10px",
                    }}
                  />
                  <div className="showcase-actions">
                    <button
                      className="btn-action"
                      onClick={() => addToFavorites(product.productId)}
                    >
                      <IonIcon icon={heartOutline} />
                    </button>
                    <Link
                      to={`/product/${product.productId}`}
                      className="btn-action"
                    >
                      <IonIcon icon={eyeOutline} />
                    </Link>
                    <button className="btn-action">
                      <IonIcon icon={repeatOutline} />
                    </button>
                    <button
                      onClick={() => handleAddToCart(product)}
                      className="btn-action"
                    >
                      <IonIcon icon={bagAddOutline} />
                    </button>
                  </div>
                </div>
                <div className="showcase-content">
                  <a href="#" className="showcase-category">
                    {`Thương hiệu: ${brands[product.brandId] || "Không rõ"}`}
                  </a>
                  <a href="#">
                    <h3 className="showcase-title">{product.productName}</h3>
                  </a>
                  <div className="showcase-rating">
                    <IonIcon icon="star" />
                    <IonIcon icon="star" />
                    <IonIcon icon="star" />
                    <IonIcon icon="star-outline" />
                    <IonIcon icon="star-outline" />
                  </div>
                  <div className="price-box">
                    <p className="price">
                      {product.price.toLocaleString("vi-VN")}₫
                    </p>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p>Không có sản phẩm nào.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AllProduct;
