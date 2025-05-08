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
  const [totalItems, setTotalItems] = useState(0);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  
    if (!categoryId) return;

    setLoading(true);
    setError(null);

    // Fetch category details
    axios
      .get(`${baseURL}/public/categories/${categoryId}`)
      .then((response) => setCategory(response.data))
      .catch((error) => {
        console.error("Error fetching category:", error);
        setError("Could not load category.");
      });

    // Fetch products of category with pagination
    axios
      .get(`${baseURL}/public/categories/${categoryId}/products`, {
        params: {
          page: currentPage - 1, // API uses 0-based index for pagination
          size: productsPerPage,
        },
      })
      .then((response) => {
        setProducts(response.data.content);
        setTotalItems(response.data.totalElements);  // Total items from API response
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching products:", error);
        setError("Could not load products.");
        setLoading(false);
      });

    // Fetch brand information
    axios
      .get(`${baseURL}/public/brands`)
      .then((response) => {
        const brandMap = response.data.content.reduce((acc, brand) => {
          acc[brand.brandId] = brand.brandName;
          return acc;
        }, {});
        setBrands(brandMap);
      })
      .catch((error) => {
        console.error("Error fetching brands:", error);
      });
  }, [categoryId, currentPage]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  const totalPages = Math.ceil(totalItems / productsPerPage);  // Total number of pages
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;

  // Add product to cart
  const handleAddToCart = async (product) => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.userId) {
      alert("Please log in to add to the cart.");
      return;
    }

    try {
      // Send request to add product to cart in server
      await axios.post(`${baseURL}/public/cart/add`, {
        userId: user.userId,
        productId: product.productId,
        quantity: 1,
      });

      // Retrieve the current cart from localStorage
      let localCart = JSON.parse(localStorage.getItem("cart")) || [];

      // Check if the product already exists in the cart
      const existingIndex = localCart.findIndex(
        (item) => item.productId === product.productId
      );

      if (existingIndex !== -1) {
        // If product exists, increase quantity
        localCart[existingIndex].quantity += 1;
      } else {
        // If not, add the product with quantity 1
        localCart.push({ ...product, quantity: 1 });
      }

      // Save updated cart to localStorage
      localStorage.setItem("cart", JSON.stringify(localCart));

      alert(`Product ${product.productName} has been added to your cart!`);
      navigate("/cart");
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Failed to add to cart!");
    }
  };

  return (
    <div className="product-box">
      <div className="product-main flex flex-col items-center">
        <h2 className="title">{category?.categoryName || "Category"}</h2>
        <div className="product-grid">
          {products.map((product) => (
            <div key={product.productId} className="showcase">
              <div className="showcase-banner">
                <img
                  src={`${baseURL}/public/products/image/${product.image}`}
                  alt={product.productName}
                  className="product-image"
                  style={{
                    maxWidth: "100%",
                    maxHeight: "100%",
                    objectFit: "contain",
                  }}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "/default-image.jpg"; // Fallback image
                  }}
                />
                <div className="showcase-actions">
                  <button
                    onClick={() => handleAddToCart(product)}
                    className="btn-action"
                  >
                    <IonIcon icon={bagAddOutline} />
                  </button>
                  <Link to={`/product/${product.productId}`} className="btn-action">
                    <IonIcon icon={eyeOutline} />
                  </Link>
                  <button className="btn-action">
                    <IonIcon icon={heartOutline} />
                  </button>
                </div>
              </div>
              <div className="showcase-content">
                <a href="#" className="showcase-category">
                  {`Brand: ${brands[product.brandId] || "Unknown"}`}
                </a>
                <a href="#">
                  <h3 className="showcase-title">{product.productName}</h3>
                </a>
                <div className="price-box">
                  <p className="price">
                    {product.price.toLocaleString("vi-VN")}₫
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="pagination" style={{ display: "flex", justifyContent: "center", marginTop: "20px" }}>
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => prev - 1)}
              style={{ padding: "10px 15px", margin: "0 5px", cursor: "pointer" }}
            >
              &laquo; Previous
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i + 1}
                onClick={() => setCurrentPage(i + 1)}
                style={{
                  padding: "10px 15px",
                  margin: "0 5px",
                  cursor: "pointer",
                  backgroundColor: currentPage === i + 1 ? "#007bff" : "#f0f0f0",
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
              style={{ padding: "10px 15px", margin: "0 5px", cursor: "pointer" }}
            >
              Next &raquo;
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default CategoryProduct;
