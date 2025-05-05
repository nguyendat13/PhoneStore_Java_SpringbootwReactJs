import OrderListByStatus from "../Order/OrderList";
export default function OrderPending() {
  return <OrderListByStatus statusFilter="Chờ xác nhận" />;
}