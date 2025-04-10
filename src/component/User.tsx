import { List, Datagrid, TextField, EmailField, EditButton, DeleteButton, Create, SimpleForm, TextInput, SelectInput, Edit, FunctionField,required  } from 'react-admin';

const roleChoices = [
  { id: 'admin', name: 'Admin' },
  { id: 'user', name: 'User' },
];

export const UserList = () => (
  <List>
    <Datagrid>
      <TextField source="userId" label="User ID" />
      <TextField source="firstName" label="First Name" />
      <TextField source="lastName" label="Last Name" />
      <EmailField source="email" label="Email" />
      <FunctionField
        label="Role"
        render={record => record.roles.map((role: { roleName: any; }) => role.roleName).join(', ')}
      />      
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const UserCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="firstName" label="First Name" validate={required()} />
      <TextInput source="lastName" label="Last Name" validate={required()} />
      <TextInput source="mobileNumber" label="Mobile Number" />
      <TextInput source="address" label="Address" />
      <TextInput source="cart" label="Cart" />
      <TextInput source="email" label="Email" validate={required()} />
      <TextInput source="password" label="Password" type="password" validate={required()} />
      <SelectInput source="role" label="Role" choices={roleChoices} validate={required()} />
    </SimpleForm>
  </Create>
);

export const UserEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="userId" disabled />
      <TextInput source="firstName" label="First Name" validate={required()} />
      <TextInput source="lastName" label="Last Name" validate={required()} />
      <TextInput source="mobileNumber" label="Mobile Number" />
      <TextInput source="address" label="Address" />
      <TextInput source="cart" label="Cart" />
      <TextInput source="email" label="Email" validate={required()} />
      <SelectInput source="role" label="Role" choices={roleChoices} validate={required()} />
    </SimpleForm>
  </Edit>
);

