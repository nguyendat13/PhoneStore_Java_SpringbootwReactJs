import React from "react";

import Slider from "../components/Home/Slider";
import MenuCategoryH from "../components/Home/MenuCategory/MenuBrandH";
import NewProducts from "../components/Home/Product/NewProducts";
import Testimonial from "../components/Home/Testimonial";
import MenuCategoryProductV from "../components/Home/MenuCategory/MenuCategoryProductV";

function Home() {
  
  return (
    <main>
      <Slider />
      <MenuCategoryH />
      <div className="product-container">
        <div className="container">
        
          <MenuCategoryProductV />
          <NewProducts />
        </div>
      </div>
      <Testimonial />
    </main>
  );
}

export default Home;
