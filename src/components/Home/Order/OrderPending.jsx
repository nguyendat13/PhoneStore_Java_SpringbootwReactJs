import OrderListByStatus from "../User/Orders";
export default function OrderPending() {
  return <OrderListByStatus statusFilter="Chờ xác nhận" />;
}