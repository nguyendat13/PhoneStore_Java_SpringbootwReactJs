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
} from "react-admin";

// ✅ LIST - Lấy dữ liệu từ /public/categories
export const CategoryList = () => (
  <List resource="categories">
    <Datagrid>
      <TextField source="categoryId" label="ID" />
      <TextField source="categoryName" label="Tên danh mục" />
      <NumberField source="categoryQty" label="Số lượng sản phẩm" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

// ✅ CREATE - Gửi dữ liệu đến /admin/categories
export const CategoryCreate = () => (
  <Create resource="categories">
    <SimpleForm>
      <TextInput source="categoryName" label="Tên danh mục" required />
    </SimpleForm>
  </Create>
);

// ✅ EDIT - PUT /admin/categories/:id
export const CategoryEdit = () => (
  <Edit resource="categories">
    <SimpleForm>
      <TextInput source="categoryId" label="ID" disabled />
      <TextInput source="categoryName" label="Tên danh mục" />
    </SimpleForm>
  </Edit>
);
