import React, { useState, useEffect } from "react";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
import { IonIcon } from "@ionic/react";
import {
  heartOutline,
  eyeOutline,
  repeatOutline,
  bagAddOutline,
} from "ionicons/icons";
import { Link } from "react-router-dom"; // Import Link từ react-router-dom
import baseURL from "../../api/BaseUrl";
const Search = () => {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [searchParams] = useSearchParams();

  const keyword = searchParams.get("keyword") || "";

  const fetchProducts = async (keywordParam, pageNumber = 1) => {
    if (!keywordParam.trim()) return;

    try {
      setLoading(true);
      setError("");

      const response = await axios.get(
        `${baseURL}/public/products/keyword/${keywordParam}`,
        {
          params: {
            pageNumber,
            pageSize: 10,
            sortBy: "productId",
            sortOrder: "desc",
            categoryId: 0,
          },
        }
      );

      setProducts(response.data.content);
      setTotalPages(response.data.totalPages);
      setPage(pageNumber);
    } catch (err) {
      console.error(err);
      setError("Không tìm thấy sản phẩm phù hợp.");
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  // Gọi API mỗi khi keyword hoặc page thay đổi
  useEffect(() => {
    if (keyword) {
      fetchProducts(keyword, 1);
    }
  }, [keyword]);

  const handlePageChange = (newPage) => {
    fetchProducts(keyword, newPage);
  };
  const handleAddToCart = async (product) => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.userId) {
      alert("Vui lòng đăng nhập để thêm vào giỏ hàng.");
      return;
    }

    try {
      // Gửi request đến API để thêm vào giỏ hàng server
      await axios.post(`${baseURL}/public/cart/add`, {
        userId: user.userId,
        productId: product.productId,
        quantity: 1,
      });

      // Lấy giỏ hàng hiện tại từ localStorage
      let localCart = JSON.parse(localStorage.getItem("cart")) || [];

      // Kiểm tra sản phẩm đã tồn tại chưa
      const existingIndex = localCart.findIndex(
        (item) => item.productId === product.productId
      );

      if (existingIndex !== -1) {
        // Nếu sản phẩm đã tồn tại, tăng số lượng
        localCart[existingIndex].quantity += 1;
      } else {
        // Nếu chưa, thêm sản phẩm mới
        localCart.push({ ...product, quantity: 1 });
      }

      // Lưu lại vào localStorage
      localStorage.setItem("cart", JSON.stringify(localCart));

      alert(`Đã thêm sản phẩm ${product.productName} vào giỏ hàng!`);
    } catch (error) {
      console.error("Lỗi khi thêm vào giỏ hàng:", error);
      alert("Thêm vào giỏ hàng thất bại!");
    }
  };

  return (
    <div className="product-box">
      <h2 className="title">Kết quả tìm kiếm: "{keyword}"</h2>

      {loading && <p>Đang tải...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div className="product-main flex flex-col items-center">
        <div className="product-grid">
          {products.length > 0 ? (
            products.map((product) => (
              <div key={product.productId} className="showcase">
                <div
                  className="showcase-banner"
                  style={{
                    display: "flex",
                    justifyContent: "center", // Căn giữa theo chiều ngang
                    alignItems: "center", // Căn giữa theo chiều dọc
                    position: "relative",
                  }}
                >
                  <div
                    className="image-container"
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      alignItems: "center",
                      width: "200px", // Đặt kích thước cố định cho vùng chứa ảnh
                      height: "200px", // Đặt kích thước cố định cho vùng chứa ảnh
                    }}
                  >
                    <img
                      src={`${baseURL}/public/products/image/${product.image}`}
                      alt={product.productName}
                      className="product-image"
                      style={{
                        width: "100%",
                        height: "100%",
                        objectFit: "cover", // Đảm bảo ảnh không bị méo và phủ kín vùng chứa
                        borderRadius: "10px",
                      }}
                    />
                  </div>
                  <div className="showcase-actions">
                    <Link to="/favorites" className="btn-action">
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
                  <a href="#" className="showcase-category">
                    {`Thương hiệu ${product.brandId || "Không rõ"}`}
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
                    <p className="price">{product.price.toLocaleString()}đ</p>
                    <del>
                      {product.priceSale
                        ? product.priceSale.toLocaleString() + "đ"
                        : ""}
                    </del>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p>Không có sản phẩm tìm thấy.</p>
          )}
        </div>

        {/* Phân trang */}
        {totalPages > 1 && (
          <div style={{ marginTop: 20 }}>
            <button
              disabled={page === 1}
              onClick={() => handlePageChange(page - 1)}
            >
              ← Trước
            </button>
            <span style={{ margin: "0 10px" }}>
              Trang {page} / {totalPages}
            </span>
            <button
              disabled={page === totalPages}
              onClick={() => handlePageChange(page + 1)}
            >
              Sau →
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Search;
