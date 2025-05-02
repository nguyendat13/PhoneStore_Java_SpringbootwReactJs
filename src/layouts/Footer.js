import React from 'react'
import { IonIcon } from '@ionic/react';
 import { locationOutline,callOutline,mailOutline,logoFacebook,logoTwitter,logoLinkedin,logoInstagram  } from 'ionicons/icons';

 function Footer() {
  const steps = [
    {
      id: 1,
      title: "Bước 1",
      description: "Kiểm tra đánh giá gọi điện trực tiếp",
      image: require("../assets/images/step1.webp"),
    },
    {
      id: 2,
      title: "Bước 2",
      description: "Mang máy đến cửa hàng hoặc nhận tại nhà",
      image: require("../assets/images/step2.webp"),
    },
    {
      id: 3,
      title: "Bước 3",
      description: "Nhận tiền hoặc nâng cấp máy mới",
      image: require("../assets/images/step3.webp"),
    },
  ];
  return (
   <footer>
      <div style={styles.tradeInContainer}>
      <div style={styles.tradeInHeader}>
        <div>
          <h2 style={styles.tradeInTitle}>THU CŨ ĐỔI MỚI</h2>
          <p style={styles.tradeInSubtitle}>Thẩm định đánh giá dễ dàng từ chuyên gia</p>
        </div>
        <a href="#" style={styles.tradeInDetail}>Xem chi tiết</a>
      </div>

      <div style={styles.tradeInSteps}>
        {steps.map((step) => (
          <div key={step.id} style={styles.tradeInStep}>
            <img src={step.image} alt={step.title} style={styles.tradeInIcon} />
            <h3 style={styles.tradeInStepTitle}>{step.title}</h3>
            <p style={styles.tradeInStepDesc}>{step.description}</p>
          </div>
        ))}
      </div>
    </div>
<div className="footer-nav">
  <div className="container">
  <ul className="footer-nav-list">
      <li className="footer-nav-item">
        <h2 className="nav-title">Liên hệ</h2>
      </li>
      <li className="footer-nav-item flex">
        <div className="icon-box">
          <IonIcon icon={locationOutline} />
        </div>
        <address className="content">
          60C Trương Văn Thành, Phường Hiệp Phú, Quận 9, TP. Hồ Chí Minh
        </address>
      </li>
      <li className="footer-nav-item flex">
        <div className="icon-box">
          <IonIcon icon={callOutline} />
        </div>
        <a href="tel:+84335235807" className="footer-nav-link">(+84) 335 235 807</a>
      </li>
      <li className="footer-nav-item flex">
        <div className="icon-box">
          <IonIcon icon={mailOutline} />
        </div>
        <a href="mailto:hamyduyenit@gmail.com" className="footer-nav-link">hamyduyenit@gmail.com</a>
      </li>
    </ul>
    <ul className="footer-nav-list">
      <li className="footer-nav-item">
        <h2 className="nav-title">Sản phẩm</h2>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Điện thoại mới</a>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Khuyến mãi</a>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Bán chạy nhất</a>
      </li>
    </ul>
    <ul className="footer-nav-list">
      <li className="footer-nav-item">
        <h2 className="nav-title">Công ty</h2>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Về chúng tôi</a>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Chính sách bảo hành</a>
      </li>
      <li className="footer-nav-item">
        <a href="#" className="footer-nav-link">Chính sách đổi trả</a>
      </li>
    </ul>
    <ul className="footer-nav-list">
  <li className="footer-nav-item">
    <h2 className="nav-title">Hỗ trợ khách hàng</h2>
  </li>
  <li className="footer-nav-item">
    <a href="#" className="footer-nav-link">Trung tâm trợ giúp</a>
  </li>
  <li className="footer-nav-item">
    <a href="#" className="footer-nav-link">Hướng dẫn mua hàng</a>
  </li>
  <li className="footer-nav-item">
    <a href="#" className="footer-nav-link">Phương thức thanh toán</a>
  </li>
</ul>
    <ul className="footer-nav-list">
      <li className="footer-nav-item">
        <h2 className="nav-title">Theo dõi chúng tôi</h2>
      </li>
      <li>
        <ul className="social-link">
          <li className="footer-nav-item">
            <a href="#" className="footer-nav-link">
              <IonIcon icon={logoFacebook} />
            </a>
          </li>
          <li className="footer-nav-item">
            <a href="#" className="footer-nav-link">
              <IonIcon icon={logoTwitter} />
            </a>
          </li>
          <li className="footer-nav-item">
            <a href="#" className="footer-nav-link">
              <IonIcon icon={logoLinkedin} />
            </a>
          </li>
          <li className="footer-nav-item">
            <a href="#" className="footer-nav-link">
              <IonIcon icon={logoInstagram} />
            </a>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</div>

  <div className="footer-bottom">
    <div className="container">
      <img src={require("../assets/images/payment.png")} alt="payment method" className="payment-img" />
      <p className="copyright">
        Copyright © <a href="#">Mỹ Duyên</a> all rights reserved.
      </p>
    </div>
  </div>
</footer>
  )
}
const styles = {
  tradeInContainer: {
    background: "linear-gradient(to bottom, #FF5A5A, #FFB05A)",
    padding: "20px",
    borderRadius: "10px",
    textAlign: "center",
  },
  tradeInHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "20px",
  },
  tradeInTitle: {
    color: "#fff",
    fontSize: "24px",
    fontWeight: "bold",
    margin: "0",
  },
  tradeInSubtitle: {
    color: "#fff",
    fontSize: "16px",
    margin: "1px 0",
  },
  tradeInDetail: {
    color: "#fff",
    textDecoration: "none",
    fontSize: "14px",
  },
  tradeInSteps: {
    display: "flex",
    justifyContent: "space-between",
    gap: "10px",
  },
  tradeInStep: {
    background: "#fff",
    padding: "15px",
    borderRadius: "10px",
    flex: "1",
    textAlign: "center",
    boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
  },
  tradeInIcon: {
    width: "60px",
    height: "60px",
    marginBottom: "10px",
  },
  tradeInStepTitle: {
    fontSize: "16px",
    fontWeight: "bold",
    margin: "5px 0",
  },
  tradeInStepDesc: {
    fontSize: "14px",
    color: "#555",
  },
};
export default Footer