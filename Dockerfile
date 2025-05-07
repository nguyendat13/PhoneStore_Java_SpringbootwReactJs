# Sử dụng Node.js image để build ứng dụng React
FROM node:16

# Set thư mục làm việc
WORKDIR /app

# Copy package.json và package-lock.json để cài đặt các dependency
COPY package*.json ./

# Cài đặt các dependency
RUN npm install

# Copy toàn bộ mã nguồn frontend vào container
COPY . .

# Build ứng dụng React
RUN npm run build

# Cài đặt nginx để phục vụ ứng dụng React
FROM nginx:alpine

# Copy ứng dụng build vào nginx container
COPY --from=0 /app/build /usr/share/nginx/html

# Mở cổng 80
EXPOSE 80

# Chạy nginx khi container khởi động
CMD ["nginx", "-g", "daemon off;"]
