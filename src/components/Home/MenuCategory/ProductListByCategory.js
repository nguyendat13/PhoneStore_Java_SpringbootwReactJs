import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import baseURL from '../../../api/BaseUrl';

function ProductListByCategory() {
  const { categoryId } = useParams();
  const [products, setProducts] = useState([]);
  const [categoryName, setCategoryName] = useState('');

  useEffect(() => {
    axios
      .get(`${baseURL}public/categories/${categoryId}`)
      .then(response => setCategoryName(response.data.categoryName))
      .catch(error => console.error('Lỗi khi lấy thông tin danh mục:', error));

    axios
      .get(`${baseURL}public/categories/${categoryId}/products`)
      .then(response => setProducts(response.data))
      .catch(error => console.error('Lỗi khi lấy sản phẩm:', error));
  }, [categoryId]);

  return (
    <div>
      <h1>{categoryName}</h1>
      <div>
        {products.length > 0 ? (
          products.map(product => (
            <div key={product.productId}>
              <h3>{product.productName}</h3>
              <p>Giá: ${product.regularPrice}</p>
            </div>
          ))
        ) : (
          <p>Không có sản phẩm nào trong danh mục này.</p>
        )}
      </div>
    </div>
  );
}

export default ProductListByCategory;
