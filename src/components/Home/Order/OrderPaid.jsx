import OrderListByStatus from "../Order/OrderList";
export default function OrderPaid() {
  return <OrderListByStatus statusFilter="Đã thanh toán" />;
}