import React, { useEffect, useState } from 'react';
import axios from 'axios';
import baseURL from '../../../api/BaseUrl';

function ImageProduct({ id, name, tagName }) {
    const [images, setImages] = useState([]);
    console.log("Product ID:", id);

    useEffect(() => {
        // Gọi API để lấy danh sách hình ảnh
        axios.get(`${baseURL}galleries/product/${id}`)
            .then(response => {
                console.log("API Response:", response.data);
                
                const fetchedImages = Array.isArray(response.data) ? response.data : [];
                if (fetchedImages.length > 0) {
                    setImages(fetchedImages);
                } else {
                    console.error('No images found for this product.');
                }
            })
            .catch(error => {
                console.error('Error fetching images:', error);
            });
    }, [id]);

    // Nếu không có ảnh thì trả về một placeholder hoặc thông báo lỗi
    if (images.length === 0) {
        return <div>No images available</div>;
    }

    // Render ảnh dựa trên tagName
    if (tagName === "best sellers") {
        return (
            <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} width={75} height={75} className="showcase-img" />
        );
    }
    if (tagName === "toplist") {
        return (
            <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} className="showcase-img" width={70} />
        );
    }
    if (tagName === "deal") {
        return (
            <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} className="showcase-img" />
        );
    }
    if (tagName === "new") {
        return (
            <div>
                {images.length > 1 ? (
                    <>
                        <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} width={300} className="product-img default" />
                        <img src={`http://localhost:8080/upload/${images[1].imagePath}`} alt={name} width={300} className="product-img hover" />
                    </>
                ) : (
                    <>
                        <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} width={300} className="product-img default" />
                        <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} width={300} className="product-img hover" />
                    </>
                )}
            </div>
        );
    }

    // Nếu không có tagName nào khớp, trả về một hình ảnh mặc định
    return (
        <img src={`http://localhost:8080/upload/${images[0].imagePath}`} alt={name} className="showcase-img" />
    );
}

export default ImageProduct;
