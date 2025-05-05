import { 
  List, Datagrid, TextField, EmailField, EditButton, DeleteButton,
  SimpleForm, TextInput, SelectInput, Edit, FunctionField, required, SelectArrayInput,
  ArrayInput, SimpleFormIterator, usePermissions
} from 'react-admin';

const genderChoices = [
  { id: 'Nam', name: 'Nam' },
  { id: 'Nữ', name: 'Nữ' },
];

const genderMap: Record<string, string> = {
  Nam: 'Nam',
  Nữ: 'Nữ',
};

const roleMap: Record<number, string> = {
  1: 'SUPER_ADMIN',
  2: 'ADMIN',
  3: 'USER',
};

export const UserList = () => (
  <List>
    <Datagrid>
      <TextField source="userId" label="ID" />
      <TextField source="fullname" label="Họ tên" />
      <TextField source="username" label="Tài khoản" />
      <TextField source="phone" label="Số điện thoại" />
      <FunctionField
        label="Giới tính"
        render={(record: any) => genderMap[record.gender] || 'Không rõ'}
      />
      <EmailField source="email" label="Email" />
      <FunctionField
        label="Vai trò"
        render={(record: any) =>
          (record.roleIds || [])
            .map((id: number) => roleMap[id] || `#${id}`)
            .join(', ')
        }
      />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const roleChoices = [
  { id: 1, name: 'SUPER_ADMIN' },
  { id: 2, name: 'ADMIN' },
  { id: 3, name: 'USER' },
];

export const UserEdit = () => {
  const { permissions } = usePermissions(); // trả về ['SUPER_ADMIN'] hoặc ['ADMIN']

  // Lọc vai trò có thể gán được
  const getAssignableRoles = () => {
    if (permissions?.includes('SUPER_ADMIN')) {
      return roleChoices; // SUPER_ADMIN được gán tất cả
    } else if (permissions?.includes('ADMIN')) {
      return roleChoices.filter(role => role.name === 'USER'); // ADMIN chỉ gán USER
    }
    return []; // USER không được gán ai cả
  };

  return (
    <Edit>
      <SimpleForm>
        <TextInput source="id" label="ID" disabled />
        <TextInput source="fullname" label="Họ tên" validate={required()} />
        <TextInput source="username" label="Tài khoản" validate={required()} />
        <TextInput source="email" label="Email" validate={required()} />
        <TextInput source="phone" label="Số điện thoại" />
        <SelectInput source="gender" label="Giới tính" choices={genderChoices} />
        <TextInput source="password" label="Mật khẩu" type="password" />

        {/* Chỉ hiển thị SelectArrayInput nếu có quyền */}
        {permissions?.includes('SUPER_ADMIN') || permissions?.includes('ADMIN') ? (
          <SelectArrayInput
            source="roleIds"
            label="Danh sách Vai trò"
            choices={getAssignableRoles()}
          />
        ) : null}

        <ArrayInput source="addresses" label="Danh sách Địa chỉ">
          <SimpleFormIterator>
            <TextInput source="street" label="Đường" />
            <TextInput source="buildingName" label="Tòa nhà" />
            <TextInput source="city" label="Thành phố" />
            <TextInput source="state" label="Tỉnh/Bang" />
            <TextInput source="country" label="Quốc gia" />
            <TextInput source="pincode" label="Mã bưu điện" />
          </SimpleFormIterator>
        </ArrayInput>
      </SimpleForm>
    </Edit>
  );
};
