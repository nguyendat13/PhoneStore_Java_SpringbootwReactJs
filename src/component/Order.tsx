import { List, useRedirect, Datagrid, TextField, NumberField, Identifier ,Show, SimpleShowLayout, useRecordContext,  ArrayField, ImageField, ReferenceField, DateField, useNotify, useRefresh, Edit, SimpleForm, TextInput, EditButton, SelectInput, ArrayInput, SimpleFormIterator} from 'react-admin';
import { useLocation, useParams } from 'react-router-dom';

    // OrderList.tsx


    export const OrderList = () => {
      const redirect = useRedirect();
    
      const handleRowClick = (id: Identifier | undefined) => {
        if (id) {
          redirect('edit', 'orders', id);
        }
      };
    
      return (
        <List>
          <Datagrid rowClick={(id, resource, record) => handleRowClick(id)}>
            <TextField source="orderId" label="Order ID" />
            <TextField source="fullname" label="Full Name" />
            <TextField source="orderStatus" label="Order Status" />
            <NumberField source="totalAmount" label="Total Amount" />
            <TextField source="orderDate" label="Order Date" />
            
            <EditButton label="Chỉnh sửa" />
          </Datagrid>
        </List>
      );
    };
    
 // OrderShow.tsx

 export const OrderShow = () => {
    const notify = useNotify();
    const redirect = useRedirect();
    const refresh = useRefresh();
    const username = localStorage.getItem('username');
  
    if (!username) {
      notify("Không tìm thấy tên người dùng trong localStorage", { type: 'error' });
      redirect('/orders');
      return null;
    }
  
    const onError = (error: any) => {
      notify(`Lỗi khi tải đơn hàng: ${error.message}`, { type: 'error' });
      redirect('/orders');
      refresh();
    };
  
    return (
      <Show queryOptions={{ meta: { username }, onError }}>
        <SimpleShowLayout>
          <TextField source="id" label="Order ID" />
          <TextField source="username" label="Username" />
          <TextField source="fullname" label="Full Name" />
          <TextField source="phone" label="Phone" />
          <TextField source="address" label="Address" />
          <NumberField source="totalAmount" label="Total Amount" />
          <TextField source="orderStatus" label="Order Status" />
          <DateField source="orderDate" label="Order Date" />
  
          <ArrayField source="orderItems" label="Ordered Products">
            <Datagrid>
              <TextField source="orderItemId" label="Order Item ID" />
              <TextField source="productId" label="Product ID" />
              <TextField source="productName" label="Product Name" />
              <ImageField source="productImage" label="Image" />
              <NumberField source="quantity" label="Quantity" />
              <NumberField source="orderedProductPrice" label="Final Price" />
              <NumberField source="discount" label="Discount (%)" />
              <TextField source="paymentMethod" label="Payment Method" />
            </Datagrid>
          </ArrayField>
        </SimpleShowLayout>
      </Show>
    );
  };
  
  export const OrderEdit = () => {
    return (
      <Edit>
        <SimpleForm>
          <TextInput disabled source="orderId" label="Order ID" />
          <TextInput disabled source="fullname" label="Tên khách hàng" />
          <TextInput disabled source="phone" label="Số điện thoại" />
          <TextInput disabled source="address" label="Địa chỉ" />
          <TextInput disabled source="totalAmount" label="Tổng tiền" />
          <TextInput disabled source="orderDate" label="Ngày đặt hàng" />
  
          <SelectInput
            source="orderStatus"
            label="Trạng thái đơn hàng"
            choices={[
              { id: 'Đang xử lý', name: 'Đang xử lý' },
              { id: 'Chờ xác nhận', name: 'Chờ xác nhận' },
              { id: 'Đã thanh toán', name: 'Đã thanh toán' },
              { id: 'Đã hủy', name: 'Đã hủy' },
            ]}
          />
        </SimpleForm>
      </Edit>
    );
  };