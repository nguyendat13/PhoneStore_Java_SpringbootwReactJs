import React from 'react';
import ReactDOM from 'react-dom/client';
import './assets/scss/app.scss';  // Đảm bảo rằng bạn đã import file SCSS
import App from './App';
import { GoogleOAuthProvider } from '@react-oauth/google'; // Thêm GoogleOAuthProvider

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    {/* Bọc App vào GoogleOAuthProvider để sử dụng Google Login */}
    <GoogleOAuthProvider clientId="456507624829-le3bkn9e2f9ro8ioecghvi0pm3aruqrl.apps.googleusercontent.com">
      <App />
    </GoogleOAuthProvider>
  </React.StrictMode>
);

// Nếu bạn muốn đo hiệu suất của ứng dụng, hãy gọi hàm reportWebVitals
// hoặc gửi kết quả đến một endpoint phân tích
// Bạn có thể tìm thêm thông tin tại: https://bit.ly/CRA-vitals
