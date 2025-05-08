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
import baseURL from "../../../api/BaseUrl"
 const API_BASE_URL = baseURL;

const AllProduct = () => {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState({});
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    selectedBrand: "",
    selectedCategory: "",
    minPrice: 0,
    maxPrice: 1000000,
    rating: 0,
  });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // Log the current filters to ensure they are correct
        console.log("Current filters:", filters);

        const productResponse = await axios.get(
          `${API_BASE_URL}/public/products`,
          { params: filters } // Ensure filters are being sent
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

        const categoryResponse = await axios.get(
          `${API_BASE_URL}/public/categories`
        );
        setCategories(categoryResponse.data.content);
      } catch (error) {
        setError("Lỗi khi lấy dữ liệu sản phẩm.");
        console.error("Lỗi khi lấy dữ liệu sản phẩm:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [filters]); // Re-run effect when filters change

  const handleAddToCart = async (product) => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.userId) {
      alert("Vui lòng đăng nhập để thêm vào giỏ hàng.");
      return;
    }

    try {
      await axios.post(`${baseURL}/public/cart/add`, {
        userId: user.userId,
        productId: product.productId,
        quantity: 1,
      });

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
    } catch (error) {
      console.error("Lỗi khi thêm vào giỏ hàng:", error);
      alert("Thêm vào giỏ hàng thất bại!");
    }
  };

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

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
  
    setFilters((prevFilters) => {
      let newFilters = {
        ...prevFilters,
        [name]: value,
      };
  
      // Nếu chọn tất cả thì reset các giá trị liên quan
      if (name === "selectedBrand" && value === "") {
        newFilters.selectedBrand = "";
      }
      if (name === "selectedCategory" && value === "") {
        newFilters.selectedCategory = "";
      }
  
      return newFilters;
    });
  };
  

  if (loading) {
    return <div className="text-center">Đang tải sản phẩm...</div>;
  }

  if (error) {
    return <div className="text-center text-danger">{error}</div>;
  }

  return (
    <div className="product-box">
      <div className="filters">
        <select name="selectedBrand" onChange={handleFilterChange}>
          <option value="">Tất cả thương hiệu</option> {/* <- sửa dòng này */}
          {Object.entries(brands).map(([brandId, brandName]) => (
            <option key={brandId} value={brandId}>
              {brandName}
            </option>
          ))}
        </select>

        <select name="selectedCategory" onChange={handleFilterChange}>
          <option value="">Tất cả danh mục</option> {/* <- sửa dòng này */}
          {categories.map((category) => (
            <option key={category.categoryId} value={category.categoryId}>
              {category.categoryName}
            </option>
          ))}
        </select>
      </div>

      <div className="product-main">
        <h2 className="text-center mb-4">Tất cả sản phẩm</h2>
        <div className="product-grid">
          {products.length > 0 ? (
            products.map((product) => (
              <div key={product.productId} className="showcase">
                <div className="showcase-banner">
                  <img
                    src={`${baseURL}/public/products/image/${encodeURIComponent(
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
                    {Array.from({ length: 5 }, (_, index) => (
                      <IonIcon
                        key={index}
                        icon={index < product.rating ? "star" : "star-outline"}
                      />
                    ))}
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
