import { 
  List, Datagrid, TextField, EmailField, EditButton, DeleteButton,
  Create, SimpleForm, TextInput, SelectInput, Edit, FunctionField, required, SelectArrayInput
} from 'react-admin';

const genderChoices = [
  { id: 'Nam', name: 'Nam' },
  { id: 'Nữ', name: 'Nữ' },
];

const genderMap: Record<string, string> = {
  Nam: 'Nam',
  Nữ: 'Nữ',
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
        render={(record: any) => record.roleIds?.join(', ') ?? ''}
      />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const UserCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="fullname" label="Họ tên" validate={required()} />
      <TextInput source="username" label="Tài khoản" validate={required()} />
      <TextInput source="email" label="Email" validate={required()} />
      <TextInput source="password" label="Mật khẩu" validate={required()} type="password" />
      <TextInput source="phone" label="Số điện thoại" />
      <SelectInput source="gender" label="Giới tính" choices={genderChoices} validate={required()} />
      <SelectArrayInput 
        source="roleIds" 
        label="Danh sách Vai trò" 
        choices={[
          { id: 1, name: 'Admin' },
          { id: 2, name: 'User' },
        ]}
        validate={required()}
      />
    </SimpleForm>
  </Create>
);

export const UserEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="userId" label="ID" disabled />
      <TextInput source="fullname" label="Họ tên" validate={required()} />
      <TextInput source="username" label="Tài khoản" validate={required()} />
      <TextInput source="email" label="Email" validate={required()} />
      <TextInput source="phone" label="Số điện thoại" />
      <SelectInput source="gender" label="Giới tính" choices={genderChoices} />
      <SelectArrayInput 
        source="roleIds" 
        label="Danh sách Vai trò" 
        choices={[
          { id: 1, name: 'Admin' },
          { id: 2, name: 'User' },
        ]}
      />
    </SimpleForm>
  </Edit>
);
