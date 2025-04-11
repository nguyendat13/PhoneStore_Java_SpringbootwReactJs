
import {
    List,
    Datagrid,
    TextField,
    NumberField,
    Create,
    Edit,
    SimpleForm,
    TextInput,
    NumberInput,
    EditButton,
    DeleteButton,useNotify, useRedirect, useRefresh 
  } from 'react-admin';
  
  // ✅ BRAND LIST
  // ✅ BRAND LIST - giống category
export const BrandList = () => (
  <List resource="brands">
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="brandName" label="Tên thương hiệu" />
      <NumberField source="brandQty" label="Số lượng sản phẩm trong thương hiệu" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

// ✅ BRAND CREATE
export const BrandCreate = () => {
  const notify = useNotify();
  const redirect = useRedirect();
  const refresh = useRefresh();

  const onSuccess = () => {
    notify('Thêm thương hiệu thành công');
    redirect('list', 'brands');
    refresh();
  };

  return (
    <Create resource="brands" mutationOptions={{ onSuccess }}>
      <SimpleForm>
        <TextInput source="brandName" label="Tên thương hiệu" required />
      </SimpleForm>
    </Create>
  );
};


// ✅ BRAND EDIT
export const BrandEdit = () => (
  
  <Edit resource="brands">
    <SimpleForm>
      <TextInput source="brandId" label="ID" disabled />
      <TextInput source="brandName" label="Tên danh mục" />
    </SimpleForm>
  </Edit>
);