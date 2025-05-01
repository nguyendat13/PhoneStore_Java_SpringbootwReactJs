import {
  Admin,
  Resource,
  CustomRoutes
} from "react-admin"; 
import { Layout } from "./Layout";
import { Route } from "react-router-dom";
import { dataProvider } from "./dataProvider";
import { authProvider } from "./component/authProvider";
import { Dashboard } from "./component/Dashboard";
import CategoryIcon from '@mui/icons-material/Category';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import ReceiptIcon from '@mui/icons-material/Receipt';
import PersonIcon from '@mui/icons-material/Person';

import { CategoryList, CategoryCreate, CategoryEdit } from "./component/Category";
import { ProductList, ProductCreate, ProductEdit } from "./component/Product";
import ProductImageUpdate from "./component/ProductImageUpdate";
import { OrderEdit, OrderList, OrderShow } from "./component/Order";
import { UserList, UserEdit } from "./component/User";
import { BrandList, BrandCreate, BrandEdit } from './component/Brand';
export const App = () => (
  <Admin authProvider={authProvider} layout={Layout} dataProvider={dataProvider} dashboard={Dashboard}>
    <CustomRoutes>
      <Route path="/products/:id/update-image" element={<ProductImageUpdate />} />
    </CustomRoutes>
    <Resource name="categories" list={CategoryList} create={CategoryCreate} edit={CategoryEdit} icon={CategoryIcon} />
    <Resource name="brands" list={BrandList} create={BrandCreate} edit={BrandEdit} />
    <Resource name="products" list={ProductList} create={ProductCreate} edit={ProductEdit} icon={Inventory2Icon} />
    <Resource name="orders" list={OrderList} show={OrderShow} edit={OrderEdit} icon={ReceiptIcon} />
    <Resource name="users" list={UserList} edit={UserEdit} icon={PersonIcon} />
  
  </Admin>
);
