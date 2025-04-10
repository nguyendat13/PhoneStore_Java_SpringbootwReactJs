import { List, useRedirect, Datagrid, TextField, NumberField, Identifier ,Show, SimpleShowLayout, useRecordContext,  ArrayField, ImageField, ReferenceField, DateField, useNotify, useRefresh} from 'react-admin';

    export const OrderList = () => {
        const redirect = useRedirect();
    
        // Xử lý khi click vào dòng
        const handleRowClick = (id: Identifier | undefined, record: any) => {
            if (id && record.email) {
                localStorage.setItem('globalOrderId', id.toString());
                localStorage.setItem("globalEmailOrder", record.email);
                redirect('show', 'orders', id);
            }
        };
    
        return (
            <List>
                <Datagrid rowClick={(id, resource, record) => handleRowClick(id, record)}>
                    <TextField source="orderId" label="Order ID" />
                    <TextField source="email" label="Email" />
                    <NumberField source="totalAmount" label="Total Amount" />
                </Datagrid>
            </List>
        );
    };
    
    export const OrderShow = () => {
       const notify = useNotify();
          const refresh = useRefresh();
          const redirect = useRedirect();
      
          const onError = (error: { message: any; }) => {
             notify(`Could not load cart: ${error.message}`, { type: 'error' });
               redirect('/orders');
             refresh();
          };
          if (!localStorage.getItem("globalEmailOrder")) {
             return <span>Error: Email is required</span>;
          }
    
        return (
            <Show  queryOptions={{
                meta: { email: localStorage.getItem("globalEmailOrder") },
                onError,
            }}>
                <SimpleShowLayout>
                    <TextField source="orderId" label="Order ID" />
                    <TextField source="email" label="Email" />
                    <NumberField source="totalAmount" label="Total Amount" />
                    <TextField source="orderStatus" label="Order Status" />
                    <DateField source="orderDate" label="Order Date" />
                    
                    <ReferenceField source="paymentId" reference="payments" label="Payment Method">
                        <TextField source="paymentMethod" />
                    </ReferenceField>
    
                    {/* Hiển thị danh sách sản phẩm đã đặt */}
                    <ArrayField source="orderItems" label="Ordered Products">
                        <Datagrid>
                            <TextField source="orderItemId" label="Order Item ID" />
                            <TextField source="product.productId" label="Product ID" />
                            <TextField source="product.productName" label="Product Name" />
                            <ImageField source="product.image" label="Image" />
                            <TextField source="product.description" label="Description" />
                            <NumberField source="quantity" label="Quantity Ordered" />
                            <NumberField source="product.price" label="Original Price" />
                            <NumberField source="discount" label="Discount (%)" />
                            <NumberField source="orderedProductPrice" label="Final Price" />
                            
                            <ReferenceField source="product.categoryId" reference="categories" label="Category">
                                <TextField source="categoryName" />
                            </ReferenceField>
                        </Datagrid>
                    </ArrayField>
                </SimpleShowLayout>
            </Show>
        );
    };
    