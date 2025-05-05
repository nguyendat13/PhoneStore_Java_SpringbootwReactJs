import React, { useState, useEffect, useRef } from "react";
import { IonIcon } from "@ionic/react";
import {
  logoFacebook,
  logoTwitter,
  logoLinkedin,
  logoInstagram,
  bagHandleOutline,
  heartOutline,
  personOutline,
} from "ionicons/icons";
import Logo from "../assets/images/logo/logodt.jpg";
import { useNavigate } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google"; // Thêm vào để sử dụng Google Login

function Header() {
  const [keyword, setKeyword] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showMenu, setShowMenu] = useState(false);
  const [cartCount, setCartCount] = useState(0);
  const [favoriteCount, setFavoriteCount] = useState(0);
  const menuRef = useRef(null);
  const navigate = useNavigate();

  // Lấy thông tin người dùng và giỏ hàng từ localStorage
  useEffect(() => {
    const user = localStorage.getItem("user");
    if (user) {
      setIsLoggedIn(true);

    }

    // Cập nhật giỏ hàng và yêu thích
    const updateCounts = () => {
      const cart = JSON.parse(localStorage.getItem("cart")) || [];
      const favorites = JSON.parse(localStorage.getItem("favorites")) || [];
      setCartCount(cart.length);
      setFavoriteCount(favorites.length);
    };

    updateCounts();

    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setShowMenu(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    // Cập nhật khi localStorage thay đổi
    window.addEventListener("storageUpdate", updateCounts);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      window.removeEventListener("storageUpdate", updateCounts);
    };
  }, []);

  // Đăng xuất người dùng
  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    localStorage.removeItem("payments");

    setIsLoggedIn(false);
    navigate("/dang-nhap");
  };

 

  const toggleMenu = () => {
    setShowMenu(!showMenu);
  };

  return (
    <header>
      <style>{`
        .dropdown-container {
          position: relative;
        }

        .dropdown-menu {
          position: absolute;
          top: 45px;
          right: 0;
          background-color: white;
          border: 1px solid #ccc;
          border-radius: 5px;
          z-index: 1000;
          width: 150px;
          box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .dropdown-menu a, .dropdown-menu button {
          display: block;
          width: 100%;
          padding: 10px;
          text-align: left;
          background: none;
          border: none;
          cursor: pointer;
          font-size: 14px;
        }

        .dropdown-menu a:hover,
        .dropdown-menu button:hover {
          background-color: #f5f5f5;
        }

        .action-btn {
          background: none;
          border: none;
          cursor: pointer;
          font-size: 20px;
          margin-left: 10px;
          position: relative;
        }

        .count {
          background-color: red;
          color: white;
          border-radius: 50%;
          padding: 2px 6px;
          font-size: 12px;
          position: absolute;
          top: -8px;
          right: -8px;
        }
      `}</style>

      {/* Header top */}
      <div className="header-top">
        <div className="container">
          <ul className="header-social-container">
            <li>
              <a
                href="https://www.facebook.com/"
                target="_blank"
                className="social-link"
                rel="noopener noreferrer"
              >
                <IonIcon icon={logoFacebook} />
              </a>
            </li>
            <li>
              <a
                href="https://twitter.com/"
                target="_blank"
                className="social-link"
                rel="noopener noreferrer"
              >
                <IonIcon icon={logoTwitter} />
              </a>
            </li>
            <li>
              <a
                href="https://www.instagram.com/"
                target="_blank"
                className="social-link"
                rel="noopener noreferrer"
              >
                <IonIcon icon={logoInstagram} />
              </a>
            </li>
            <li>
              <a
                href="https://www.linkedin.com/"
                target="_blank"
                className="social-link"
                rel="noopener noreferrer"
              >
                <IonIcon icon={logoLinkedin} />
              </a>
            </li>
          </ul>
          <div className="header-alert-news">
            <p>
              <b>Miễn phí vận chuyển</b> Tuần này cho đơn hàng trên đơn hàng
              500.000đ
            </p>
          </div>
          <div className="header-top-actions">
            <select name="currency">
              <option value="usd">USD $</option>
              <option value="eur">EUR €</option>
            </select>
            <select name="language">
              <option value="vi">Tiếng Việt</option>
              <option value="en-US">English</option>
            </select>
          </div>
        </div>
      </div>

      {/* Header main */}
      <div className="header-main">
        <div className="container">
          <a href="/" className="header-logo">
            <img src={Logo} alt="Logo của cửa hàng" width={150} height={135} />
          </a>

          {/* Search */}
          <div className="header-search-container">
            <input
              type="search"
              name="search"
              className="search-field"
              placeholder="Nhập tên sản phẩm..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            <a
              href={`/search?keyword=${encodeURIComponent(keyword.trim())}`}
              className="search-btn"
              onClick={(e) => {
                if (keyword.trim() === "") e.preventDefault();
              }}
            >
              Tìm kiếm
            </a>
          </div>

          {/* User actions */}
          <div className="header-user-actions">
            <div className="dropdown-container" ref={menuRef}>
              <button className="action-btn" onClick={toggleMenu}>
                <IonIcon icon={personOutline} />
              </button>

              {showMenu && (
                <div className="dropdown-menu">
                  {isLoggedIn ? (
                    <>
                      <a href="/profile">Trang cá nhân</a>
                      <a href="/orders">Đơn hàng</a>

                      <button onClick={handleLogout}>Đăng xuất</button>
                    </>
                  ) : (
                    <a href="/dang-nhap">Đăng nhập</a>
                  )}
                </div>
              )}
            </div>

            <a href="/favorites" className="action-btn">
              <IonIcon icon={heartOutline} />
              {favoriteCount > 0 && (
                <span className="count">{favoriteCount}</span>
              )}
            </a>
            <a href="/cart" className="action-btn">
              <IonIcon icon={bagHandleOutline} />
              {cartCount > 0 && <span className="count">{cartCount}</span>}
            </a>
          </div>
        </div>
      </div>


    </header>
  );
}

export default Header;
