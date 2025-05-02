import OrderListByStatus from "../User/Orders";
export default function OrderPaid() {
  return <OrderListByStatus statusFilter="Đã thanh toán" />;
}