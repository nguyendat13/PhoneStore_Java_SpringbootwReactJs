import OrderListByStatus from "../Order/OrderList";
export default function OrderCanceled() {
  return <OrderListByStatus statusFilter="Đã hủy" />;
}