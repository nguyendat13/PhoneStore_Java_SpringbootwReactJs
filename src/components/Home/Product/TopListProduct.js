import React, { useEffect, useState } from 'react';
import ImageProduct from './ImageProduct';
import { Link } from 'react-router-dom';
import { handleAddToCart } from "../../../services/cartService";

function TopListProduct({ categoryName, limit }) {
    const [products, setProducts] = useState([]);

    // Dữ liệu sản phẩm tĩnh
    const staticProducts = [
        {
            id: 1,
            productName: 'Áo thun nam',
            categories: [{ categoryName: 'Quần áo' }],
            regularPrice: 199,
            discountPrice: 149,
        },
        {
            id: 2,
            productName: 'Laptop Dell XPS 13',
            categories: [{ categoryName: 'Điện tử' }],
            regularPrice: 1200,
            discountPrice: 1000,
        },
        {
            id: 3,
            productName: 'Tai nghe Bluetooth',
            categories: [{ categoryName: 'Phụ kiện điện thoại' }],
            regularPrice: 100,
            discountPrice: 80,
        },
        {
            id: 4,
            productName: 'Đồng hồ thông minh',
            categories: [{ categoryName: 'Thiết bị đeo thông minh' }],
            regularPrice: 89,
            discountPrice: 70,
        },
        {
            id: 5,
            productName: 'iPhone 12 256GB',
            categories: [{ categoryName: 'Điện thoại' }],
            regularPrice: 1769000,
            discountPrice: 0,
        },
    ];

    useEffect(() => {
        // Lọc sản phẩm theo categoryName được truyền vào
        const filteredProducts = staticProducts.filter(product =>
            product.categories[0]?.categoryName === categoryName
        );
        setProducts(filteredProducts.slice(0, limit || 3)); // Giới hạn số lượng sản phẩm hiển thị
    }, [categoryName, limit]);

    const renderProduct = (product) => (
        <div className="showcase" key={product.id} style={styles.showcase}>
            <a href="#" className="showcase-img-box">
                <ImageProduct id={product.id} name={product.productName} tagName="toplist" />
            </a>
            <div className="showcase-content" style={styles.showcaseContent}>
                <a href="#">
                    <h4 className="showcase-title" style={styles.showcaseTitle}>{product.productName}</h4>
                </a>
                <div className="price-box" style={styles.priceBox}>
                    <p className="price" style={styles.price}>${product.regularPrice}.00</p>
                    {product.discountPrice > 0 && (
                        <del className="del" style={styles.del}>${product.discountPrice}.00</del>
                    )}
                </div>
            </div>
        </div>
    );

    const styles = {
        productShowcase: {
            padding: '20px 0',
            fontFamily: 'Arial, sans-serif',
        },
        mainTitle: {
            fontSize: '24px',
            fontWeight: 'bold',
            marginBottom: '20px',
            color: '#333',
        },
        categoryTitle: {
            fontSize: '18px',
            fontWeight: 'bold',
            marginBottom: '10px',
            color: '#333',
        },
        showcaseWrapper: {
            display: 'block', // Hiển thị dạng dọc
        },
        showcaseContainer: {
            marginBottom: '30px', // Khoảng cách giữa các danh mục
            borderRadius: '8px',
            boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)',
            backgroundColor: '#fff',
            padding: '10px',
        },
        showcase: {
            marginBottom: '15px',
            position: 'relative',
            overflow: 'hidden',
            borderRadius: '8px',
            boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)',
        },
        showcaseContent: {
            textAlign: 'center',
            padding: '10px 0',
        },
        showcaseTitle: {
            fontSize: '16px',
            fontWeight: '500',
            color: '#333',
            marginBottom: '5px',
        },
        priceBox: {
            marginTop: '10px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '10px',
        },
        price: {
            fontSize: '16px',
            fontWeight: 'bold',
            color: '#28a745',
        },
        del: {
            fontSize: '14px',
            color: '#dc3545',
            textDecoration: 'line-through',
        },
        viewMoreButton: {
            display: 'inline-block',
            padding: '10px 20px',
            backgroundColor: '#007bff',
            color: '#fff',
            textDecoration: 'none',
            borderRadius: '5px',
            textAlign: 'center',
            marginTop: '20px',
        },
    };

    return (
      
        <div className="product-showcase" style={styles.productShowcase}>
            {/* Tiêu đề chung "Danh mục nổi bật" với thẻ h1 */}
            <div className="showcase-wrapper" style={styles.showcaseWrapper}>
                <div className="showcase-container" style={styles.showcaseContainer}>
                    <h2 style={styles.categoryTitle}>{categoryName}</h2>
                    {/* Hiển thị sản phẩm theo danh mục */}
                    {products.length > 0 ? (
                        products.map(product => renderProduct(product))
                    ) : (
                        <p>Không có sản phẩm nào trong danh mục này.</p>
                    )}
                    {/* Thêm nút "Xem thêm" */}
                    <div className="view-more">
                        <Link to={`/products/category/${categoryName}`} style={styles.viewMoreButton}>
                            Xem thêm
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default TopListProduct;