// src/components/Brand.tsx

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
    DeleteButton,
  } from 'react-admin';
  
  // ✅ BRAND LIST
  export const BrandList = () => (
    <List>
      <Datagrid>
        <TextField source="brandId" label="ID" />
        <TextField source="brandName" label="Tên thương hiệu" />
        <NumberField source="brandQty" label="Số lượng sản phẩm" />
        <EditButton />
        <DeleteButton />
      </Datagrid>
    </List>
  );
  
  // ✅ BRAND CREATE
  export const BrandCreate = () => (
    <Create>
      <SimpleForm>
        <TextInput source="brandName" label="Tên thương hiệu" required />
        <NumberInput source="brandQty" label="Số lượng sản phẩm" required />
      </SimpleForm>
    </Create>
  );
  
  // ✅ BRAND EDIT
  export const BrandEdit = () => (
    <Edit>
      <SimpleForm>
        <TextInput source="brandId" disabled />
        <TextInput source="brandName" label="Tên thương hiệu" />
        <NumberInput source="brandQty" label="Số lượng sản phẩm" />
      </SimpleForm>
    </Edit>
  );
  