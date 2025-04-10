import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        host: true, // Chạy trên tất cả các mạng LAN (nếu cần)
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // Địa chỉ backend của bạn
                changeOrigin: true, // Thay đổi nguồn gốc để phù hợp với CORS
                secure: false, // Nếu backend của bạn không có chứng chỉ SSL
            },
        },
    },
    base: './', // Nếu bạn đang sử dụng relative path trong production
});
