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
import baseURL from "../../../api/BaseUrl";
function BestSellerProduct() {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState({});

  useEffect(() => {
    const fetchBestSellers = async () => {
      try {
        const res = await axios.get(
          `${baseURL}/public/products/best-sellers?pageNumber=1&pageSize=4`
        );
        if (res.data?.content) setProducts(res.data.content);

        const brandRes = await axios.get(
          `${baseURL}/public/brands`
        );
        if (brandRes.data?.content) {
          const brandMap = brandRes.data.content.reduce((acc, brand) => {
            acc[brand.brandId] = brand.brandName;
            return acc;
          }, {});
          setBrands(brandMap);
        }
      } catch (err) {
        console.error("Lỗi khi lấy sản phẩm bán chạy:", err);
      }
    };

    fetchBestSellers();
  }, []);

  // if (loading) return <p>Đang tải sản phẩm...</p>;
  // if (error) return <p>{error}</p>;

  // const indexOfLastProduct = currentPage * productsPerPage;
  // const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  // const currentProducts = products.slice(
  //   indexOfFirstProduct,
  //   indexOfLastProduct
  // );
  // const totalPages = Math.ceil(products.length / productsPerPage);
  return (
    <div className="product-main flex flex-col items-center">
      <h2 className="title">Sản phẩm bán chạy</h2>
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
                    {product.priceSale > 0
                      ? `${product.priceSale.toLocaleString()}₫`
                      : `${product.price.toLocaleString()}₫`}
                  </p>
                  {product.priceSale > 0 && (
                    <del>{product.price.toLocaleString()}₫</del>
                  )}
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>Không có sản phẩm bán chạy.</p>
        )}
      </div>
    </div>
  );
}

export default BestSellerProduct;
