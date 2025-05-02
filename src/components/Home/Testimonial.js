import quotes from "../../assets/images/icons/ip16.jpg";
import React from "react";

function Testimonial() {
  return (
    <div>
      <div className="container">
        <div className="testimonials-box">
          {/* Lời chứng thực */}
          <div className="testimonial">
            <h2 className="title">Lời chứng thực</h2>
            <div className="testimonial-card">
              <img
                src={require("../../assets/images/tim-cook.jpg")}
                alt="Tim Cook"
                className="testimonial-banner"
                width={80}
                height={80}
              />
              <p className="testimonial-name">Tim Cook</p>
              <p className="testimonial-title">CEO của Apple</p>
              <img
                src={quotes}
                alt="Trích dẫn"
                className="quotation-img"
                width={26}
              />
              <p className="testimonial-desc">
                "iPhone 16 là một bước nhảy vọt về công nghệ, mang lại trải
                nghiệm chưa từng có cho người dùng trên toàn thế giới."
              </p>
            </div>
          </div>

          {/* Bộ sưu tập điện thoại iPhone 16 */}
          <div className="cta-container">
            <img
              src={require("../../assets/images/screenshot-2024-09-10-at-013605-2300.webp")}
              alt="Bộ sưu tập iPhone 16"
              className="cta-banner"
            />
            <a href="#" className="cta-content">
              <p className="discount">Ưu đãi đặc biệt</p>
              <h2 className="cta-title">Bộ sưu tập iPhone 16 mới ra mắt</h2>
              <p className="cta-text">Trải nghiệm công nghệ đỉnh cao</p>
              <button className="cta-btn">Mua ngay</button>
            </a>
          </div>

          {/* Dịch vụ */}
          <div className="service">
            <h2 className="title">Dịch vụ của chúng tôi</h2>
            <div className="service-container">
              <a href="#" className="service-item">
                <div className="service-content">
                  <h3 className="service-title">
                    <ion-icon
                      name="boat-outline"
                      style={{ marginRight: "8px" }}
                    />
                    Giao hàng toàn cầu
                  </h3>
                  <p className="service-desc">Cho đơn hàng trên $100</p>
                </div>
              </a>
              <a href="#" className="service-item">
                <div className="service-content">
                  <h3 className="service-title">
                    <ion-icon
                      name="rocket-outline"
                      style={{ marginRight: "8px" }}
                    />
                    Giao hàng ngay hôm sau
                  </h3>
                  <p className="service-desc">Chỉ áp dụng tại Anh</p>
                </div>
              </a>
              <a href="#" className="service-item">
                <div className="service-content">
                  <h3 className="service-title">
                    <ion-icon
                      name="call-outline"
                      style={{ marginRight: "8px" }}
                    />
                    Hỗ trợ trực tuyến tốt nhất
                  </h3>
                  <p className="service-desc">Giờ làm việc: 8h - 23h</p>
                </div>
              </a>
              <a href="#" className="service-item">
                <div className="service-content">
                  <h3 className="service-title">
                    <ion-icon
                      name="arrow-undo-outline"
                      style={{ marginRight: "8px" }}
                    />
                    Chính sách hoàn trả
                  </h3>
                  <p className="service-desc">
                    Hoàn trả dễ dàng &amp; miễn phí
                  </p>
                </div>
              </a>
              <a href="#" className="service-item">
                <div className="service-content">
                  <h3 className="service-title">
                    <ion-icon
                      name="ticket-outline"
                      style={{ marginRight: "8px" }}
                    />
                    Hoàn tiền 30%
                  </h3>
                  <p className="service-desc">Cho đơn hàng trên $100</p>
                </div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Testimonial;
