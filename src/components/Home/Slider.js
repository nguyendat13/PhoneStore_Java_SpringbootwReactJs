import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Navigation, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import styled from "styled-components";

const Banner = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
`;

const SliderContainer = styled.div`
  width: 100%;
  max-width: 1200px; /* Tăng kích thước tối đa */
  margin: auto;

  .swiper-pagination-bullet {
    background: #fff;
  }

  .swiper-button-next,
  .swiper-button-prev {
    color: #fff;
  }
`;
const BannerImg = styled.img`
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 10px;
`;

function Slider() {
  return (
    <Banner>
      <SliderContainer>
        <Swiper
          spaceBetween={10}
          slidesPerView={1}
          loop={true}
          autoplay={{ delay: 3000, disableOnInteraction: false }}
          navigation
          pagination={{ clickable: true }}
          modules={[Autoplay, Navigation, Pagination]}
        >
          <SwiperSlide>
            <BannerImg src={require("../../assets/images/slider_1.webp")} alt="women's latest fashion sale" />
          </SwiperSlide>
          <SwiperSlide>
            <BannerImg src={require("../../assets/images/slider_2.webp")} alt="modern sunglasses" />
          </SwiperSlide>
          <SwiperSlide>
            <BannerImg src={require("../../assets/images/slider_3.webp")} alt="new fashion summer sale" />
          </SwiperSlide>
        </Swiper>
      </SliderContainer>
    </Banner>
  );
}

export default Slider;
