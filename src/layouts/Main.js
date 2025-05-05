import React from "react";
import Home from "../pages/Home";
import Login from "../components/Login";
import Register from "../components/Register";
import AllProduct from "../components/Home/Product/AllProduct";
import BrandPro from "../components/Home/Product/BrandPro";
import ProductDetail from "../components/Home/Product/ProductDetail";
import Cart from '../components/Home/Cart';
import Favorites from '../components/Home/Favorites';
import Profile from "../components/Profile";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import CategoryProduct from "../components/Home/MenuCategory/CategoryProduct";
import Search from "../components/Home/Search";
import ConfirmPage from "../components/Home/User/ConfirmPage";
import OrderSuccess from "../components/Home/User/OrderSuccess";
import OrderProcessing from "../components/Home/Order/OrderProcessing";
import OrderPaid from "../components/Home/Order/OrderPaid";
import OrderCanceled from "../components/Home/Order/OrderCanceled";
import OrderPending from "../components/Home/Order/OrderPending";
import OrderList from "../components/Home/Order/OrderList";
import UpdateProfile from "../components/Home/User/UpdateProfile";
import OAuth2RedirectHandler from "../components/Home/User/OAuth2RedirectHandler";

function Main() {
  return (
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dang-nhap" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/all-products" element={<AllProduct />} />
        <Route path="/products" element={<BrandPro />} />
        <Route path="/product/:productId" element={<ProductDetail />} />{" "}
        {/* san pham theo danh mục */}
        <Route path="/category/:categoryId" element={<CategoryProduct />} />
        {/* giỏ hàng */}
        <Route path="/cart" element={<Cart />} />
        <Route path="/order-success" element={<OrderSuccess />} />


        <Route path="/orders" element={<OrderList />} />

        <Route path="/orders/processing" element={<OrderProcessing />} />
          <Route path="/orders/paid" element={<OrderPaid />} />
          <Route path="/orders/canceled" element={<OrderCanceled />} />
          <Route path="/orders/pending" element={<OrderPending />} />   


        <Route path="/confirm" element={<ConfirmPage />} />
        {/* yêu thích */}
        <Route path="/favorites" element={<Favorites />} />
        {/* trang cá nhân */}
        <Route path="/profile" element={<Profile />} />
        <Route path="/update-profile" element={<UpdateProfile />} />
        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />

        {/* trang tìm kiếm */}
        <Route path="/search" element={<Search />} />

      </Routes>

  );
}

export default Main;
