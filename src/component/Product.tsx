import {
  List,
  useRecordContext,
  Datagrid,
  TextField,
  NumberField,
  Create,
  Edit,
  SimpleForm,
  TextInput,
  NumberInput,
  ReferenceInput,
  SelectInput,
  EditButton,
  DeleteButton,
  ReferenceField,
  useNotify,
  useRedirect,
  useRefresh,
} from 'react-admin';
import { Link as RouterLink } from 'react-router-dom';

// ✅ Custom image component
const CustomImageField = ({ source }: { source: string }) => {
  const record = useRecordContext();
  if (!record || !record[source]) {
    return <span>No Image</span>;
  }
  return (
    <RouterLink to={`/products/${record.productId}/update-image`}>
      <img src={record[source]} alt="Product" style={{ width: '100px', height: 'auto' }} />
    </RouterLink>
  );
};

// ✅ Filters
const postFilters = [
  <TextInput source="search" label="Search" alwaysOn />,
  <ReferenceInput source="categoryId" reference="categories" label="Category">
    <SelectInput optionText="categoryName" />
  </ReferenceInput>,
];

// ✅ LIST
export const ProductList = () => (
  <List filters={postFilters}>
    <Datagrid rowClick={false}>
      <TextField source="productId" label="ID" />
      <TextField source="productName" label="Tên sản phẩm" />
      <ReferenceField source="categoryId" reference="categories" label="Danh mục">
        <TextField source="categoryName" />
      </ReferenceField>
      <ReferenceField source="brandId" reference="brands" label="Thương hiệu">
        <TextField source="brandName" />
      </ReferenceField>
      <CustomImageField source="image" />
      <TextField source="description" label="Mô tả" />
      <TextField source="color" label="Màu sắc" />
      <NumberField source="quantity" label="Tồn kho" />
      <NumberField source="price" label="Giá gốc" />
      <NumberField source="discount" label="Giảm giá (%)" />
      <NumberField source="priceSale" label="Giá khuyến mãi" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

// ✅ CREATE
export const ProductCreate = () => {
  const notify = useNotify();
  const redirect = useRedirect();
  const refresh = useRefresh();

  const onSuccess = () => {
    notify('Thêm sản phẩm thành công');
    redirect('list', 'products'); // Chuyển sang danh sách sản phẩm
    refresh(); // Làm mới
  };

  return (
    <Create mutationOptions={{ onSuccess }}>
      <SimpleForm>
        <TextInput source="productName" label="Tên sản phẩm" required />
        <TextInput source="description" label="Mô tả" required />
        <TextInput source="color" label="Màu sắc" />
        <NumberInput source="quantity" label="Số lượng" required />
        <NumberInput source="price" label="Giá" required />
        <NumberInput source="discount" label="Giảm giá (%)" required />
        <NumberInput source="priceSale" label="Giá khuyến mãi" required />
        <ReferenceInput source="categoryId" reference="categories" label="Danh mục" required>
          <SelectInput optionText="categoryName" />
        </ReferenceInput>
        <ReferenceInput source="brandId" reference="brands" label="Thương hiệu" required>
          <SelectInput optionText="brandName" />
        </ReferenceInput>
      </SimpleForm>
    </Create>
  );
};

export const ProductEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="productId" disabled label="ID" />
      <TextInput source="productName" label="Tên sản phẩm" />
      <TextInput source="image" label="Link ảnh" />
      <TextInput source="description" label="Mô tả" />
      <NumberInput source="quantity" label="Số lượng" />
      <NumberInput source="price" label="Giá" />
      <NumberInput source="discount" label="Giảm giá (%)" />
      <NumberInput source="priceSale" label="Giá khuyến mãi" />
      <TextInput source="color" label="Màu sắc" />

      <ReferenceInput source="categoryId" reference="categories" label="Danh mục">
        <SelectInput optionText="categoryName" />
      </ReferenceInput>

      <ReferenceInput source="brandId" reference="brands" label="Thương hiệu">
        <SelectInput optionText="brandName" />
      </ReferenceInput>
    </SimpleForm>
  </Edit>
);
