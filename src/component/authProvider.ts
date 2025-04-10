import { AuthProvider } from "react-admin";
import axios from "axios";

interface LoginParams {
  username: string;
  password: string;
}
interface CheckParamsErr {
  status: number;
}

export const authProvider: AuthProvider = {
  login: async ({ username, password }: LoginParams) => {
    try {
      const response = await axios.post(
        'http://localhost:8080/api/login',
        { email: username, password },
        {
          headers: { 'Content-Type': 'application/json' },
          withCredentials: true,
        }
      );
  
      const token = response.data.token;
      const roles: string[] = response.data.roles;
  
      if (!token) {
        throw new Error("Token không tồn tại trong phản hồi từ server.");
      }
  
      // Kiểm tra role trước khi lưu token
      if (!roles.includes("ADMIN") && !roles.includes("SUPER_ADMIN")) {
        throw new Error("Tài khoản không có quyền truy cập trang quản trị.");
      }
  
      // Lưu token và username
      localStorage.setItem("jwt-token", token);
      localStorage.setItem("username", username);
      localStorage.setItem("roles", JSON.stringify(roles));
  
      return Promise.resolve();
    } catch (error: any) {
      console.error("Lỗi khi đăng nhập:", error.response?.data || error.message);
      return Promise.reject(
        new Error(error.message || "Sai tài khoản hoặc mật khẩu.")
      );
    }
  },
  

  logout: () => {
    localStorage.removeItem("jwt-token");
    localStorage.removeItem("username");
    localStorage.removeItem("roles");
    return Promise.resolve();
  },

  checkError: ({ status }: CheckParamsErr) => {
    if (status === 401 || status === 403) {
      localStorage.removeItem("jwt-token");
      localStorage.removeItem("username");
      localStorage.removeItem("roles");
      return Promise.reject();
    }
    return Promise.resolve();
  },

  checkAuth: () => {
    return localStorage.getItem("jwt-token") ? Promise.resolve() : Promise.reject();
  },

  getPermissions: () => {
    const roles = localStorage.getItem("roles");
    return roles ? Promise.resolve(JSON.parse(roles)) : Promise.resolve([]);
  },
};
