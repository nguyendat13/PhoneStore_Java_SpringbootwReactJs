import {
  List,
  Datagrid,
  TextField,
  DeleteButton,
  EditButton,
  Create,
  Edit,
  SimpleForm,
  TextInput,
  NumberField,
  useNotify,
  useRedirect,
  useRefresh,
} from "react-admin";

// ✅ LIST - Lấy dữ liệu từ /public/categories
export const CategoryList = () => (
  <List resource="categories">
    <Datagrid>
      <TextField source="categoryId" label="ID" />
      <TextField source="categoryName" label="Tên danh mục" />
      <NumberField source="categoryQty" label="Số lượng sản phẩm trong danh mục" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

// ✅ CREATE - Gửi dữ liệu đến /admin/categories
export const CategoryCreate = () => {
  const notify = useNotify();
  const redirect = useRedirect();
  const refresh = useRefresh();

  const onSuccess = () => {
    notify('Thêm danh mục thành công');
    redirect('list', 'categories');
    refresh();
  };

  return (
    <Create resource="categories" mutationOptions={{ onSuccess }}>
      <SimpleForm>
        <TextInput source="categoryName" label="Tên danh mục" required />
      </SimpleForm>
    </Create>
  );
};


// ✅ EDIT - PUT /admin/categories/:id
export const CategoryEdit = () => (
  <Edit resource="categories">
    <SimpleForm>
      <TextInput source="categoryId" label="ID" disabled />
      <TextInput source="categoryName" label="Tên danh mục" />
    </SimpleForm>
  </Edit>
);
