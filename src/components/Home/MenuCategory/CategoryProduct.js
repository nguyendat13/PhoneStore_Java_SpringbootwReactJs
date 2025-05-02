import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { IonIcon } from "@ionic/react";
import { heartOutline, eyeOutline, bagAddOutline } from "ionicons/icons";
import baseURL from "../../../api/BaseUrl";

function CategoryProduct() {
  const { categoryId } = useParams();
  const [category, setCategory] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const productsPerPage = 8;
  const [brands, setBrands] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    if (!categoryId) return;

    setLoading(true);
    setError(null);

    // Lấy danh mục
    axios
      .get(`${baseURL}public/categories/${categoryId}`)
      .then((response) => setCategory(response.data))
      .catch((error) => {
        console.error("Lỗi khi lấy danh mục:", error);
        setError("Không thể tải danh mục.");
      });

    // Lấy sản phẩm
    axios
      .get(`${baseURL}public/categories/${categoryId}/products`)
      .then((response) => {
        setProducts(response.data.content);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Lỗi khi lấy sản phẩm:", error);
        setError("Không thể tải sản phẩm.");
        setLoading(false);
      });

    // Lấy thông tin thương hiệu
    axios
      .get(`${baseURL}public/brands`)
      .then((response) => {
        const brandMap = response.data.content.reduce((acc, brand) => {
          acc[brand.brandId] = brand.brandName;
          return acc;
        }, {});
        setBrands(brandMap);
      })
      .catch((error) => {
        console.error("Lỗi khi lấy thông tin thương hiệu:", error);
      });
  }, [categoryId]);

  if (loading) return <p>Đang tải...</p>;
  if (error) return <p>{error}</p>;

  const totalPages = Math.ceil(products.length / productsPerPage);
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = products.slice(
    indexOfFirstProduct,
    indexOfLastProduct
  );

  // Thêm sản phẩm vào giỏ hàng
  const handleAddToCart = async (product) => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.userId) {
      alert("Vui lòng đăng nhập để thêm vào giỏ hàng.");
      return;
    }

    try {
      // Gửi request đến API để thêm vào giỏ hàng server
      await axios.post("http://localhost:8080/api/public/cart/add", {
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
      navigate("/cart");
    } catch (error) {
      console.error("Lỗi khi thêm vào giỏ hàng:", error);
      alert("Thêm vào giỏ hàng thất bại!");
    }
  };

  return (
    <div className="product-box">
      <div className="product-main flex flex-col items-center">
        <h2 className="title">{category?.categoryName || "Danh mục"}</h2>
        <div className="product-grid">
          {currentProducts.map((product) => (
            <div key={product.productId} className="showcase">
              <div className="showcase-banner">
                <img
                  src={`http://localhost:8080/api/public/products/image/${product.image}`}
                  alt={product.productName}
                  className="product-image"
                  style={{
                    maxWidth: "100%",
                    maxHeight: "100%",
                    objectFit: "contain", // Giữ tỷ lệ hình ảnh và không phóng to
                  }}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "/default-image.jpg"; // ảnh fallback nếu ảnh bị lỗi
                  }}
                />
                <div className="showcase-actions">
                  <button
                    onClick={() => handleAddToCart(product)}
                    className="btn-action"
                  >
                    <IonIcon icon={bagAddOutline} />
                  </button>
                  <Link
                    to={`/product/${product.productId}`}
                    className="btn-action"
                  >
                    <IonIcon icon={eyeOutline} />
                  </Link>
                  <button className="btn-action">
                    <IonIcon icon={heartOutline} />
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
                <div className="price-box">
                  <p className="price">
                    {product.price.toLocaleString("vi-VN")}₫
                  </p>
                  {/* <del>{product.priceSale.toLocaleString("vi-VN")}₫</del> */}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div
            className="pagination"
            style={{
              display: "flex",
              justifyContent: "center",
              marginTop: "20px",
            }}
          >
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => prev - 1)}
              style={{
                padding: "10px 15px",
                margin: "0 5px",
                cursor: "pointer",
              }}
            >
              &laquo; Trước
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i + 1}
                onClick={() => setCurrentPage(i + 1)}
                style={{
                  padding: "10px 15px",
                  margin: "0 5px",
                  cursor: "pointer",
                  backgroundColor:
                    currentPage === i + 1 ? "#007bff" : "#f0f0f0",
                  color: currentPage === i + 1 ? "white" : "black",
                  border: "1px solid #ddd",
                  borderRadius: "5px",
                }}
              >
                {i + 1}
              </button>
            ))}
            <button
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((prev) => prev + 1)}
              style={{
                padding: "10px 15px",
                margin: "0 5px",
                cursor: "pointer",
              }}
            >
              Sau &raquo;
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default CategoryProduct;
