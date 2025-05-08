import React, { useEffect, useState } from "react";
import axios from "axios";
import { useSearchParams, Link } from "react-router-dom";
import { IonIcon } from "@ionic/react";
import {
  heartOutline,
  eyeOutline,
  repeatOutline,
  bagAddOutline,
  star,
  starOutline,
} from "ionicons/icons";
import { handleAddToCart } from "../../../services/cartService";
import baseURL from "../../../api/BaseUrl";
function BrandPro() {
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(6);
  const [searchParams] = useSearchParams();
  const brandId = searchParams.get("brandId");

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const productRes = await axios.get(
          `${baseURL}/public/brands/${brandId}/products`
        );
        setProducts(productRes.data.content);

        const brandRes = await axios.get(
          `${baseURL}/public/brands`
        );
        const brandMap = brandRes.data.content.reduce((acc, brand) => {
          acc[brand.brandId] = brand.brandName;
          return acc;
        }, {});
        setBrands(brandMap);

        setLoading(false);
      } catch (error) {
        setError("Không thể tải sản phẩm.");
        setLoading(false);
      }
    };

    if (brandId) {
      fetchData();
    }
  }, [brandId]);



  if (loading) return <p>Đang tải sản phẩm...</p>;
  if (error) return <p>{error}</p>;

  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = products.slice(
    indexOfFirstProduct,
    indexOfLastProduct
  );
  const totalPages = Math.ceil(products.length / productsPerPage);

  return (
    <div className="product-box">
      <div className="product-main flex flex-col items-center">
        <h2 className="title">Sản phẩm theo thương hiệu</h2>
        <div className="product-grid">
          {currentProducts.map((product) => (
            <div key={product.productId} className="showcase">
              <div className="showcase-banner">
                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
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
                  {`Thương hiệu ${brands[product.brandId] || "Không rõ"}`}
                </a>
                <a href="#">
                  <h3 className="showcase-title">{product.productName}</h3>
                </a>
                <div className="showcase-rating">
                  <IonIcon icon={star} />
                  <IonIcon icon={star} />
                  <IonIcon icon={star} />
                  <IonIcon icon={starOutline} />
                  <IonIcon icon={starOutline} />
                </div>
                <div className="price-box">
                  {product.priceSale > 0 ? (
                    <>
                      <del>{product.price.toLocaleString("vi-VN")}₫</del>
                      <p className="price">
                        {product.priceSale.toLocaleString("vi-VN")}₫
                      </p>
                    </>
                  ) : (
                    <p className="price">
                      {product.price.toLocaleString("vi-VN")}₫
                    </p>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        <div className="flex justify-center mt-6 space-x-2">
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index + 1}
              className={`px-4 py-2 border rounded ${
                index + 1 === currentPage ? "bg-gray-300" : "bg-white"
              }`}
              onClick={() => setCurrentPage(index + 1)}
            >
              {index + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

export default BrandPro;
