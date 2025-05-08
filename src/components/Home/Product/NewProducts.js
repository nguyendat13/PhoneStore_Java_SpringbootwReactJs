import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom"; // Import Link từ react-router-dom
import { IonIcon } from "@ionic/react";
import {
  heartOutline,
  eyeOutline,
  repeatOutline,
  bagAddOutline,
} from "ionicons/icons";
import BestSellerProduct from "./BestSellerProduct";
import DealOfDayProduct from "./DealOfDayProduct";
import baseURL from "../../../api/BaseUrl";
// Import hàm handleAddToCart từ cartService.js
import { handleAddToCart } from "../../../services/cartService";
function NewProducts() {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState({}); // Lưu thông tin thương hiệu theo brandId

  useEffect(() => {
    const fetchNewProducts = async () => {
      try {
        // Lấy sản phẩm mới
        const productResponse = await axios.get(
         `${baseURL}/public/products`
        );

        if (
          productResponse.data &&
          Array.isArray(productResponse.data.content)
        ) {
          const limitedProducts = productResponse.data.content.slice(0, 4); // Lấy 4 sản phẩm đầu tiên
          setProducts(limitedProducts);
        } else {
          console.error("Dữ liệu không phải là mảng sản phẩm.");
        }

        // Lấy thông tin thương hiệu (giả sử API thương hiệu là /api/public/brands)
        const brandResponse = await axios.get(
          `${baseURL}/public/brands`
        );

        if (brandResponse.data && Array.isArray(brandResponse.data.content)) {
          // Tạo một object để lưu trữ thông tin thương hiệu theo brandId
          const brandMap = brandResponse.data.content.reduce((acc, brand) => {
            acc[brand.brandId] = brand.brandName; // Lưu brandId và brandName
            return acc;
          }, {});
          setBrands(brandMap);
        } else {
          console.error("Dữ liệu không phải là mảng thương hiệu.");
        }
      } catch (error) {
        console.error("Lỗi khi lấy dữ liệu sản phẩm hoặc thương hiệu:", error);
      }
    };

    fetchNewProducts();
  }, []);

  
 


  const handleAddToFavorites = async (productId) => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.userId) {
      alert("Vui lòng đăng nhập để thêm vào yêu thích.");
      return;
    }

    try {
      await axios.post(
        `${baseURL}/public/favorites/user/${user.userId}/product/${productId}`
      );
      alert("Đã thêm vào danh sách yêu thích!");
    } catch (error) {
      console.error("Lỗi khi thêm vào yêu thích:", error);
      alert("Không thể thêm vào yêu thích.");
    }
  };

  return (
    <div className="product-box">
      <BestSellerProduct />
      <DealOfDayProduct />

      <div className="product-main flex flex-col items-center">
        <h2 className="title">Sản phẩm mới</h2>
        <div className="product-grid">
          {products.length > 0 ? (
            products.map((product) => (
              <div key={product.productId} className="showcase">
                <div className="showcase-banner">
                  <img
                    src={`${baseURL}/public/products/image/${product.image}`}
                    alt={product.productName}
                    className="product-image"
                    style={{
                      width: "200px",
                      height: "200px",
                      objectFit: "cover",
                      borderRadius: "10px",
                    }}
                  />
                  <div className="showcase-actions">
                    <Link
                      to="#"
                      onClick={() => handleAddToFavorites(product.productId)}
                      className="btn-action"
                    >
                      <IonIcon icon={heartOutline} />
                    </Link>

                    <Link
                      to={`/product/${product.productId}`}
                      className="btn-action"
                    >
                      <IonIcon icon={eyeOutline} />
                    </Link>
                    <Link to="/somewhere" className="btn-action">
                      <IonIcon icon={repeatOutline} />
                    </Link>
                    <button
                      onClick={() => handleAddToCart(product)}
                      className="btn-action"
                    >
                      <IonIcon icon={bagAddOutline} />
                    </button>
                  </div>
                </div>
                <div className="showcase-content">
                  {/* Hiển thị tên thương hiệu (brandName) */}
                  <a href="#" className="showcase-category">
                    {`Thương hiệu ${brands[product.brandId] || "Không rõ"}`}
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
            <p>Không có sản phẩm mới.</p>
          )}
        </div>
        <div style={{ textAlign: "center" }}>
          <a
            href="/all-products"
            className="view-all-button transition duration-300 ease-in-out transform hover:-translate-y-1 hover:bg-blue-600 focus:ring-4 focus:ring-blue-300 focus:outline-none text-black bg-blue-500 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5 inline-block w-full"
          >
            Xem tất cả sản phẩm
          </a>
        </div>
      </div>
    </div>
  );
}

export default NewProducts;
