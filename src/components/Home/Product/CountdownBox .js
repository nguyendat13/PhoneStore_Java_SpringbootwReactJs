import React, { useEffect, useState } from "react";
import "../../../assets/css/CountdownBox.css"; // CSS của bạn nếu có

const CountdownBox = () => {
  // Set thời gian kết thúc (ví dụ: 1 ngày từ bây giờ)
  const endTime = new Date().getTime() + 1 * 24 * 60 * 60 * 1000; // 1 ngày

  const calculateTimeLeft = () => {
    const now = new Date().getTime();
    const difference = endTime - now;

    if (difference <= 0) return { days: 0, hours: 0, minutes: 0, seconds: 0 };

    const days = Math.floor(difference / (1000 * 60 * 60 * 24));
    const hours = Math.floor((difference / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((difference / 1000 / 60) % 60);
    const seconds = Math.floor((difference / 1000) % 60);

    return { days, hours, minutes, seconds };
  };

  const [timeLeft, setTimeLeft] = useState(calculateTimeLeft());

  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft(calculateTimeLeft());
    }, 1000);

    return () => clearInterval(timer); // Dọn timer khi unmount
  }, []);

  return (
    <div className="countdown-box">
      <p className="countdown-desc">Nhanh tay! Ưu đãi kết thúc sau:</p>
      <div className="countdown">
        <div className="countdown-content">
          <p className="display-number">{String(timeLeft.days).padStart(2, '0')}</p>
          <p className="display-text">Ngày</p>
        </div>
        <div className="countdown-content">
          <p className="display-number">{String(timeLeft.hours).padStart(2, '0')}</p>
          <p className="display-text">Giờ</p>
        </div>
        <div className="countdown-content">
          <p className="display-number">{String(timeLeft.minutes).padStart(2, '0')}</p>
          <p className="display-text">Phút</p>
        </div>
        <div className="countdown-content">
          <p className="display-number">{String(timeLeft.seconds).padStart(2, '0')}</p>
          <p className="display-text">Giây</p>
        </div>
      </div>
    </div>
  );
};

export default CountdownBox;
