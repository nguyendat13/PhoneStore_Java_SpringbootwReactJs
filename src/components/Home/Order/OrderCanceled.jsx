import OrderListByStatus from "../User/Orders";
export default function OrderCanceled() {
  return <OrderListByStatus statusFilter="Đã hủy" />;
}