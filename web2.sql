-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 16, 2025 at 03:58 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `web2`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `address_id` bigint NOT NULL,
  `building_name` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `pincode` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`address_id`, `building_name`, `city`, `country`, `pincode`, `state`, `street`) VALUES
(11, 'Building A', 'ssssssssssss', 'Vietnam', '1000000', 'ssssssssssss', 'sadsss'),
(12, 'ssssssss', 'ssssssssssss', 'Vietnam', '1000000', 'ssssssssssss', 'ssssssss'),
(13, 'ssssssssssss', 'ssssssssssss', 'Vietnam', '1000000', 'ssssssssssss', 'sssssssssss');

-- --------------------------------------------------------

--
-- Table structure for table `banners`
--

CREATE TABLE `banners` (
  `banner_id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `sort_order` int NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

CREATE TABLE `brands` (
  `brand_id` bigint NOT NULL,
  `brand_name` varchar(255) NOT NULL,
  `brand_qty` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `brands`
--

INSERT INTO `brands` (`brand_id`, `brand_name`, `brand_qty`) VALUES
(1, 'Iphone', 0),
(2, 'SamSung', 1),
(3, 'Oppo', 0),
(4, 'Xiaomi', 0),
(5, 'Realme', 1),
(7, 'Vivo', 0);

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `cart_id` bigint NOT NULL,
  `total_price` double DEFAULT NULL,
  `update_at` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`cart_id`, `total_price`, `update_at`, `user_id`, `email`) VALUES
(10, 0, NULL, 1, NULL),
(11, 0, NULL, 15, NULL),
(12, 0, NULL, 16, 'dat@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `cart_item_id` bigint NOT NULL,
  `discount` double DEFAULT NULL,
  `product_price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `cart_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `final_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`cart_item_id`, `discount`, `product_price`, `quantity`, `cart_id`, `product_id`, `total_price`, `final_price`) VALUES
(54, 8.33, 36000000, 1, 10, 3, NULL, 33001200),
(59, 0, 16490000, 1, 11, 6, NULL, 16490000);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` bigint NOT NULL,
  `category_name` varchar(255) NOT NULL,
  `category_qty` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `category_qty`) VALUES
(1, 'Điện thoại', 0),
(2, 'Phụ kiện', 1),
(3, 'Thiết bị thông minh', 0);

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

CREATE TABLE `contacts` (
  `contact_id` bigint NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `favorite_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` bigint NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `order_date` varchar(255) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `address`, `email`, `fullname`, `order_date`, `order_status`, `payment_method`, `phone`, `total_amount`, `user_id`) VALUES
(3, 'ssssssssssss, sssssssssss, ssssssssssss, ssssssssssss, Vietnam - 1000000', '2sound.ag@gmail.com', 'Đạt Nguyễn', '2025-05-09', 'Đã hủy', 'Thanh toán khi nhận hàng', '0845553994', 51980300, 15),
(4, 'ssssssssssss, sssssssssss, ssssssssssss, ssssssssssss, Vietnam - 1000000', '2sound.ag@gmail.com', 'Đạt Nguyễn', '2025-05-09', 'Chờ xác nhận', 'Chuyển khoản ngân hàng', '0845553994', 16490000, 15);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` bigint NOT NULL,
  `discount` double DEFAULT NULL,
  `ordered_product_price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `discount`, `ordered_product_price`, `quantity`, `order_id`, `product_id`) VALUES
(2, 0, 16490000, 2, 3, 6),
(3, 17.39, 23000000, 1, 3, 1),
(4, 0, 16490000, 1, 4, 6);

-- --------------------------------------------------------

--
-- Table structure for table `password_reset_token`
--

CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment_method`
--

CREATE TABLE `payment_method` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `payment_method`
--

INSERT INTO `payment_method` (`id`, `name`) VALUES
(2, 'Thanh toán khi nhận hàng'),
(3, 'Chuyển khoản ngân hàng');

-- --------------------------------------------------------

--
-- Table structure for table `payment_status`
--

CREATE TABLE `payment_status` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `payment_status`
--

INSERT INTO `payment_status` (`id`, `name`) VALUES
(1, 'Đang xử lý'),
(2, 'Đã thanh toán'),
(3, 'Đã hủy'),
(4, 'Chờ xác nhận');

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `post_id` bigint NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `topic_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` bigint NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `discount` double NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `price_sale` double NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `brand_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `color`, `description`, `discount`, `image`, `price`, `price_sale`, `product_name`, `quantity`, `category_id`, `brand_id`, `created_at`) VALUES
(1, 'Silver', 'iPhone 14 Pro Max đem đến những trải nghiệm không thể tìm thấy trên mọi thế hệ iPhone trước đó với màu Tím Deep Purple sang trọng, camera 48MP lần đầu xuất hiện, chip A16 Bionic và màn hình “viên thuốc” Dynamic Island linh hoạt, nịnh mắt.', 17.39, 'efd3f5b2-550e-47b0-a6d5-a4a3c3d3383c.png', 23000000, 19000300, 'iPhone 14 Pro Max 128GB', 43, 1, 1, NULL),
(2, 'Trắng', 'Thật dễ dàng để thể hiện cá tính riêng của mình trên iPhone 11 128GB. Bạn sẽ có 6 lựa chọn màu sắc Tím, Vàng, Xanh lục, Đen, Trắng và Đỏ đầy phong cách. Vẻ đẹp của iPhone 11 đến từ thiết kế cao cấp khi được làm từ khung nhôm nguyên khối và mặt lưng liền l', 10, 'bc235693-c2fa-4d59-ac7a-8e50e13be585.jpg', 12000000, 10800000, 'iPhone 11 64GB', 8, 1, 1, NULL),
(3, 'Titan đen', 'iPhone 16 series với 4 phiên bản: iPhone 16, iPhone 16 Plus, iPhone 16 Pro và iPhone 16 Pro Max đều có sự nâng cấp đáng kể, trong đó đáng chú ý là nút điều khiển camera mới, hệ thống camera tiên tiến, hiệu suất và thời lượng pin vượt trội. Trong đó iPhone', 8.33, '05e0be49-f44c-423e-ae62-6ce97e28b75f.png', 36000000, 33001200, 'iPhone 16 Pro Max 256GB', 14, 1, 1, NULL),
(4, 'Xanh dương', 'Samsung Galaxy S25 Ultra là chiếc điện thoại cao cấp nhất của nhà Samsung với những tính năng tiên phong dẫn đầu. Smartphone sở hữu thiết kế sang trọng, bền bỉ bởi khung Titan đẳng cấp kết hợp đó là trọn bộ công cụ AI thế hệ mới và Snapdragon 8 Elite for ', 0, '202bb032-5509-4375-8e7e-e9dfacf9ed43.png', 29000000, 29000000, 'Samsung Galaxy S25 Ultra 5G 12GB 256GB', 10, 1, 2, NULL),
(5, 'Trắng', 'OPPO A3 sở hữu giá bán hợp lý với thiết kế hiện đại cùng hiệu suất vượt trội bởi chipset mượt mà Snapdragon 6s 4G Gen 1, pin 5.100mAh, hỗ trợ sạc nhanh 45W và bộ nhớ 256GB, rất phù hợp dành cho người dùng trẻ năng động. Đặc biệt, thiết bị còn bền bỉ khi đ', 10, '8635fe2e-ccb0-4178-ad6e-c17ab3c015fe.jpg', 6990000, 6291000, 'OPPO A3 8GB 256GB', 23, 1, 3, NULL),
(6, 'Xám', 'Xiaomi 14T Pro không chỉ nổi bật với sự hợp tác giữa thương hiệu và Leica tạo nên một chiếc smartphone hàng đầu về nhiếp ảnh mà còn có hiệu suất mạnh mẽ bậc nhất trong phân khúc. Điện thoại thể hiện sự toàn diện khi cung cấp trải nghiệm nghe nhìn ấn tượng', 0, '35168edb-65bf-4695-b523-38218f580aab.png', 16490000, 16490000, 'Xiaomi 14T Pro 5G 12GB 512GB', 34, 1, 4, NULL),
(7, 'Tím đen', 'Là cỗ máy chiến game không lag, realme 13+ 5G là một làn gió mới mà mọi game thủ nên sở hữu cho riêng mình. Điện thoại mang đến hiệu năng khủng 3 trong 1, hệ thống nghe nhìn chất lượng cùng cụm camera với cảm biến SONY đỉnh cao sẽ giúp cho bạn có những gi', 6.25, 'c6a4a8b0-430d-4f24-9486-78aebf477815.png', 7989999, 7490624.0625, 'Realme 13+ 5G 8GB 256GB', 19, 1, 5, NULL),
(8, 'Xanh', 'Sau một thời gian dài vắng bóng, vivo đã trở lại đường đua với vivo Y03. Thuộc phân khúc giá rẻ, chiếc điện thoại này gây ấn tượng với thiết nhỏ gọn, màu sắc trẻ trung, dung lượng pin lớn, hứa hẹn sẽ mang tới cho người dùng những trải nghiệm ấn tượng tron', 0, '9a4cd6e4-344e-4698-89fe-f77ee3d1c306.jpg', 2690000, 2690000, 'Vivo Y03 4GB 64GB', 5, 1, 7, NULL),
(9, 'Đen', 'Samsung Galaxy A36 5G sẽ khiến bạn bất ngờ với thiết kế mỏng gọn hiện đại, pin lớn và những tính năng AI tiện lợi, giúp người dùng tận hưởng công nghệ thông minh một cách dễ dàng. Đây là một trong những dòng Galaxy giá phải chăng đầu tiên được tích hợp AI', 0, '3d683893-b76b-4b24-9873-c7d6192092a6.jpg', 7990000, 7990000, 'Samsung Galaxy A36 5G 8GB 128GB', 34, 1, 2, NULL),
(11, 'Tím', 'Innostyle PowerMag Slim là pin sạc dự phòng Magsafe, cho phép bạn sạc lại dễ dàng các mẫu iPhone mới nhất của Apple một cách tự do thực sự nhờ công nghệ sạc không dây tích hợp.\r\n\r\n', 6, '5fd7a794-773f-4d67-a6b1-988067242c51.jpg', 899000, 845060, 'Pin sạc dự phòng Magsafe Innostyle PowerMag Slim QC 3.0/PD 20W 10000mAh', 13, 2, 2, NULL),
(12, 'Đen', 'ProOne PWH3510 là tai nghe choảng đầu lý tưởng cho những game thủ đang tìm kiếm một sản phẩm giá cả phải chăng để chơi game. Chiếc tai nghe giá rẻ này có kiểu dáng trẻ trung, khả năng chống ồn tốt và thiết kế thoải mái.', 88, 'Tai nghe ProOne-02.jpg', 339, 0, 'Tai nghe gaming chụp tai có mic ProOne PWH3510', 21, 3, 1, NULL),
(14, 'den', 'sssssssssssssssssss', 1, '1a74f596-0964-41fe-8754-b5b4e6019f67.png', 12, 11.88, 'bàn phím cơ', 12, 2, 2, '2025-05-09 18:21:58.005000');

-- --------------------------------------------------------

--
-- Table structure for table `reply_contacts`
--

CREATE TABLE `reply_contacts` (
  `reply_id` bigint NOT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  `reply_content` varchar(255) DEFAULT NULL,
  `contact_id` bigint NOT NULL,
  `user_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` bigint NOT NULL,
  `role_name` varchar(255) NOT NULL,
  `role_status` int NOT NULL,
  `version` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `role_name`, `role_status`, `version`) VALUES
(1, 'SUPER_ADMIN', 0, NULL),
(2, 'ADMIN', 1, NULL),
(3, 'USER', 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `topics`
--

CREATE TABLE `topics` (
  `topic_id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint NOT NULL,
  `email` varchar(255) NOT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `auth_provider` enum('GOOGLE','LOCAL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `fullname`, `gender`, `password`, `phone`, `username`, `role_id`, `auth_provider`) VALUES
(1, 'dat48421@gmail.com', 'Nguyễn Phát Đạt', 'Nam', '$2a$10$7q0v7bTyBgOp8COC0v0XZOTbf8CU2xMKzDTRVdJQ.gQmZjsBjI83O', '0845553994', 'fatdat041', NULL, NULL),
(15, '2sound.ag@gmail.com', 'Đạt Nguyễn', 'Nam', '$2a$10$t9Q2JaEDkAjsRgEeL.gsg.wUQz6GVUqtLvfLNVpJdVvb0qVuKN8IO', '0845553994', '2sound.ag@gmail.com', NULL, 'GOOGLE'),
(16, 'dat@gmail.com', 'Đạt Nguyễn', 'Nam', '$2a$10$4dHobmVN5IbAJig/lRDlGuQQXWN1wnXF.nKU85JgCC85cbgejUgFO', '0781651616', 'ssssss', NULL, 'LOCAL');

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

CREATE TABLE `user_address` (
  `user_id` bigint NOT NULL,
  `address_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user_address`
--

INSERT INTO `user_address` (`user_id`, `address_id`) VALUES
(1, 11),
(16, 12),
(15, 13);

-- --------------------------------------------------------

--
-- Table structure for table `user_cart`
--

CREATE TABLE `user_cart` (
  `user_id` bigint NOT NULL,
  `cart_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_payments`
--

CREATE TABLE `user_payments` (
  `id` bigint NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `payment_amount` double DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `transaction_id` bigint DEFAULT NULL,
  `payment_method_id` bigint DEFAULT NULL,
  `payment_status_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(15, 3),
(16, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `banners`
--
ALTER TABLE `banners`
  ADD PRIMARY KEY (`banner_id`);

--
-- Indexes for table `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`brand_id`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `FKb5o626f86h46m4s7ms6ginnop` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`cart_item_id`),
  ADD KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  ADD KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`contact_id`),
  ADD KEY `FKna8bddygr3l3kq1imghgcskt8` (`user_id`);

--
-- Indexes for table `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`favorite_id`),
  ADD KEY `FK6sgu5npe8ug4o42bf9j71x20c` (`product_id`),
  ADD KEY `FKk7du8b8ewipawnnpg76d55fus` (`user_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  ADD KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`);

--
-- Indexes for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg0guo4k8krgpwuagos61oc06j` (`token`),
  ADD UNIQUE KEY `UKf90ivichjaokvmovxpnlm5nin` (`user_id`);

--
-- Indexes for table `payment_method`
--
ALTER TABLE `payment_method`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payment_status`
--
ALTER TABLE `payment_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`post_id`),
  ADD KEY `FKrfchr8dax0kfngvvkbteh5n7h` (`topic_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  ADD KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`);

--
-- Indexes for table `reply_contacts`
--
ALTER TABLE `reply_contacts`
  ADD PRIMARY KEY (`reply_id`),
  ADD KEY `FK1knctxwwtc1fogn67qjsliv7g` (`contact_id`),
  ADD KEY `FK6sg6ggyiwgy0j9o4hx70satlf` (`user_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `UK716hgxp60ym1lifrdgp67xt5k` (`role_name`);

--
-- Indexes for table `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`topic_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`);

--
-- Indexes for table `user_address`
--
ALTER TABLE `user_address`
  ADD KEY `FKpv7y2l6mvly37lngi3doarqhd` (`address_id`),
  ADD KEY `FKrmincuqpi8m660j1c57xj7twr` (`user_id`);

--
-- Indexes for table `user_cart`
--
ALTER TABLE `user_cart`
  ADD KEY `FKaew09hgh8cxl9fi9fr9m9tlx0` (`cart_id`),
  ADD KEY `FKjnc3hqv2aeg4rb38ghsrp561` (`user_id`);

--
-- Indexes for table `user_payments`
--
ALTER TABLE `user_payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpwn8pvionpkthsh8xggbvtbm` (`payment_method_id`),
  ADD KEY `FK5sci2qm0f83tk7tq0xhu3e0c` (`payment_status_id`),
  ADD KEY `FK48m9vjig8w6q9fglp2w7urppv` (`user_id`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `address_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `banners`
--
ALTER TABLE `banners`
  MODIFY `banner_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `brands`
--
ALTER TABLE `brands`
  MODIFY `brand_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `cart_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `cart_item_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `contacts`
--
ALTER TABLE `contacts`
  MODIFY `contact_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `favorites`
--
ALTER TABLE `favorites`
  MODIFY `favorite_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payment_method`
--
ALTER TABLE `payment_method`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `payment_status`
--
ALTER TABLE `payment_status`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `post_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `reply_contacts`
--
ALTER TABLE `reply_contacts`
  MODIFY `reply_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `topics`
--
ALTER TABLE `topics`
  MODIFY `topic_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `user_payments`
--
ALTER TABLE `user_payments`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`);

--
-- Constraints for table `contacts`
--
ALTER TABLE `contacts`
  ADD CONSTRAINT `FKna8bddygr3l3kq1imghgcskt8` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `favorites`
--
ALTER TABLE `favorites`
  ADD CONSTRAINT `FK6sgu5npe8ug4o42bf9j71x20c` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKk7du8b8ewipawnnpg76d55fus` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Constraints for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD CONSTRAINT `FK83nsrttkwkb6ym0anu051mtxn` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `FKrfchr8dax0kfngvvkbteh5n7h` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`topic_id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`),
  ADD CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `reply_contacts`
--
ALTER TABLE `reply_contacts`
  ADD CONSTRAINT `FK1knctxwwtc1fogn67qjsliv7g` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`),
  ADD CONSTRAINT `FK6sg6ggyiwgy0j9o4hx70satlf` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`);

--
-- Constraints for table `user_address`
--
ALTER TABLE `user_address`
  ADD CONSTRAINT `FKpv7y2l6mvly37lngi3doarqhd` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`address_id`),
  ADD CONSTRAINT `FKrmincuqpi8m660j1c57xj7twr` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `user_cart`
--
ALTER TABLE `user_cart`
  ADD CONSTRAINT `FKaew09hgh8cxl9fi9fr9m9tlx0` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`),
  ADD CONSTRAINT `FKjnc3hqv2aeg4rb38ghsrp561` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `user_payments`
--
ALTER TABLE `user_payments`
  ADD CONSTRAINT `FK48m9vjig8w6q9fglp2w7urppv` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FK5sci2qm0f83tk7tq0xhu3e0c` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`id`),
  ADD CONSTRAINT `FKpwn8pvionpkthsh8xggbvtbm` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`);

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
