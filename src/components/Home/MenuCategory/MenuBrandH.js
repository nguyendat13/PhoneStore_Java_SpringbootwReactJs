import React, { useEffect, useState } from 'react';
import axios from 'axios';
import iphone from '../../../assets/images/icons/iphone.webp';
import samsung from '../../../assets/images/icons/samsung.webp';
import oppo from '../../../assets/images/icons/oppo.webp';
import xiaomi from '../../../assets/images/icons/xiaomi.webp';
import realme from '../../../assets/images/icons/redmi.webp';
import lenovo from '../../../assets/images/icons/lenovo.webp';
import vivo from '../../../assets/images/icons/vivo.webp';
import baseURL from '../../../api/BaseUrl';
const styles = {
  category: {
    padding: '10px 0',
    textAlign: 'center'
  },
  container: {
    width: '100%',
    maxWidth: '1200px',
    margin: '0 auto'
  },
  title: {
    fontSize: '18px',
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: '15px'
  },
  categoryItemContainer: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(100px, 1fr))',
    gap: '10px',
    justifyContent: 'center'
  },
  categoryItem: {
    textAlign: 'center',
    padding: '10px',
    borderRadius: '8px',
    backgroundColor: '#f9f9f9',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    maxWidth: '120px',
    boxShadow: '0px 2px 5px rgba(0, 0, 0, 0.1)',
    transition: 'transform 0.2s ease-in-out'
  },
  categoryImgBox: {
    width: '30px',
    height: 'auto'
  },
  categoryContentBox: {
    marginTop: '5px'
  },
  categoryItemTitle: {
    fontSize: '12px',
    fontWeight: 'bold',
    margin: '5px 0'
  },
  categoryItemAmount: {
    fontSize: '10px',
    color: '#555'
  },
  categoryBtn: {
    display: 'inline-block',
    fontSize: '10px',
    color: '#007bff',
    textDecoration: 'none',
    marginTop: '5px'
  }
};

function MenuCategoryH() {
  const [brands, setBrands] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const iconMap = {
    'Iphone': iphone,
    'Samsung': samsung,
    'Oppo': oppo,
    'Xiaomi': xiaomi,
    'Realme': realme,
    'Levono': lenovo,
    'Vivo': vivo
  };

  useEffect(() => {
    const fetchBrands = async () => {
      try {
        const response = await axios.get(`${baseURL}/public/brands`);
        setBrands(response.data.content); // Dữ liệu trả về từ API chứa danh sách thương hiệu
        setLoading(false);
      } catch (error) {
        setError('Không thể tải dữ liệu thương hiệu.');
        setLoading(false);
      }
    };

    fetchBrands();
  }, []);

  if (loading) return <p>Đang tải dữ liệu...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div style={styles.category}>
      <div style={styles.container}>
        <h2 style={styles.title}>Thương hiệu nổi bật</h2>
        <div style={styles.categoryItemContainer}>
          {brands.map((brand, index) => (
            <div key={index} style={styles.categoryItem}>
              <div>
                <img 
                  src={iconMap[brand.brandName] || iphone} 
                  alt={brand.brandName} 
                  style={styles.categoryImgBox} 
                />
              </div>
              <div style={styles.categoryContentBox}>
                <div>
                  <h3 style={styles.categoryItemTitle}>{brand.brandName}</h3>
                  <p style={styles.categoryItemAmount}>({brand.brandQty || 0})</p>
                </div>
                <a 
                  href={`/products?brandId=${brand.brandId}`} 
                  style={styles.categoryBtn}
                >
                  Xem tất cả
                </a>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default MenuCategoryH;
