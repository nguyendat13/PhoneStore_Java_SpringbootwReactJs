import { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get("token");
    const user = params.get("user");

    if (token) {
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user)); // nếu có
      // Có thể fetch thêm thông tin user nếu muốn
      navigate("/profile");
      window.location.reload()  
    } else {
      navigate("/dang-nhap");
    }
  }, [navigate, location]);

  return <p>Đang xử lý đăng nhập...</p>;
};

export default OAuth2RedirectHandler;
