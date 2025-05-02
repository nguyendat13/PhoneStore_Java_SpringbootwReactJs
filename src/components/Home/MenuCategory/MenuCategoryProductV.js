import React, { useEffect, useState } from 'react';
import axios from 'axios';
import baseURL from '../../../api/BaseUrl';
import { IonIcon } from '@ionic/react';
import { closeOutline } from 'ionicons/icons';
import { Link } from 'react-router-dom';

function MenuCategoryProductV() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Gọi API lấy danh mục
    axios.get(baseURL + 'public/categories')
      .then(response => {
        setCategories(response.data.content);
        setLoading(false);
      })
      .catch(error => {
        console.error('Lỗi khi lấy danh mục:', error);
        setError('Không thể tải danh mục sản phẩm.');
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Đang tải danh mục...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="sidebar" style={styles.sidebar}>
      <div className="sidebar-top" style={styles.sidebarTop}>
        <h2 className="sidebar-title" style={styles.sidebarTitle}>Danh mục</h2>
        <button className="sidebar-close-btn">
          <IonIcon icon={closeOutline} />
        </button>
      </div>

      <div className="category-list">
        {categories.map(category => (
          <div key={category.categoryId} style={styles.categoryItem}>
            <Link to={`/category/${category.categoryId}`} style={styles.categoryLink}>
              {category.categoryName}
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
}

const styles = {
  sidebar: {
    padding: '20px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    backgroundColor: '#fff',
  },
  sidebarTop: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '20px',
  },
  sidebarTitle: {
    fontSize: '20px',
    fontWeight: 'bold',
    color: '#333',
  },
  categoryItem: {
    marginBottom: '15px',
  },
  categoryLink: {
    fontSize: '16px',
    color: '#333',
    textDecoration: 'none',
    fontWeight: '500',
  },
};

export default MenuCategoryProductV;
