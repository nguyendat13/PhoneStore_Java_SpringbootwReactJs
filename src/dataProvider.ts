  import axios from 'axios';
  import {
    CreateParams, CreateResult, DataProvider, DeleteManyParams, DeleteManyResult, DeleteParams, DeleteResult,
    GetManyParams, GetManyReferenceParams, GetManyReferenceResult, GetManyResult, GetOneParams, GetOneResult, Identifier,
    QueryFunctionContext, RaRecord, UpdateManyParams, UpdateManyResult, UpdateParams, UpdateResult
  } from 'react-admin';
  import { Pagination, Sort, Filter } from './types';

  const apiUrl = 'http://localhost:8080/api';

  const httpClient = {
    get: async (url: string) => {
      const token = localStorage.getItem("jwt-token");

      if (!token) {
        console.error("JWT token is missing! Redirecting to login.");
        window.location.href = "/login";  // Chuyển hướng về trang đăng nhập
        return Promise.reject(new Error("JWT token missing"));
      }

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          withCredentials: true,
        });
        return { json: response.data };
      } catch (error: any) {
        if (error.response?.status === 401) {
          console.error("Unauthorized request! Clearing token...");
          localStorage.removeItem("jwt-token");
          window.location.href = "/login"; // Đăng xuất khi token hết hạn
        }
        throw error;
      }
    },
    
    post: async (url: string, data: any) => {
      const token = localStorage.getItem('jwt-token');
      if (!token) {
          throw new Error('Unauthorized: JWT token is missing');
      }

      try {
          const response = await axios.post(url, data, {
              headers: {
                  'Authorization': `Bearer ${token}`,
                  'Content-Type': 'application/json',
              },
              withCredentials: true,
          });
          return { json: response.data };
      } catch (error) {
          console.error('API request failed:', error);
          throw error;
      }
  },
    put: async (url: string, data: any) => {
      const token = localStorage.getItem('jwt-token');
      try {
        const response = await axios.put(url, data, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        });
        return ({ json: response.data });
      } catch (error) {
        console.error('API request failed:', error);
        throw error;
      }
    },
    delete: async (url: string) => {
      const token = localStorage.getItem('jwt-token');
      try {
        const response = await axios.delete(url, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        });
        return { json: response.data };
      } catch (error) {
        console.error('API DELETE request failed:', error);
        throw error;
      }
    },
    
  };

  export const dataProvider: DataProvider = {

  //   getList: (resource: string, { pagination = {}, sort = {}, filter = {} }) => {
  //     const { page = 0, perPage = 10 } = pagination || {};
  //     const { field = 'id', order = 'ASC' } = sort; 
  //     const userEmail = localStorage.getItem('username');
  //   const userRoles = JSON.parse(localStorage.getItem('roles') || '{}').roleIds || []; // Lấy roles từ localStorage

  //     const idFieldMapping: { [key: string]: string } = {
  //         products: 'productId',
  //         categories: 'categoryId',
  //         orders: 'orderId',
  //         brands: 'brandId',
  //         users: 'userId',
  //     };

  //     const idField = idFieldMapping[resource] || 'id';
  //     const query = {
  //         pageNumber: page.toString(),
  //         pageSize: perPage.toString(),
  //         sortBy: field,
  //         sortOrder: order,
  //         ...filter,
  //     };

  //     console.log('Request filter:', filter);

  //     let url: string;

  //     if (filter?.search) {
  //         const keyword = filter.search;
  //         delete query.search;
  //         url = `${apiUrl}/public/${resource}/keyword/${encodeURIComponent(keyword)}?${new URLSearchParams(query)}`;
  //     } else if (filter?.categoryId) {
  //         const categoryId = filter.categoryId;
  //         delete query.categoryId;
  //         url = `${apiUrl}/public/categories/${categoryId}/${resource}?${new URLSearchParams(query)}`;
  //     } else if (filter?.brandId) {
  //         const brandId = filter.brandId;
  //         delete query.brandId;
  //         url = `${apiUrl}/public/brands/${brandId}/${resource}?${new URLSearchParams(query)}`;
  //     } else if (resource === 'orders') {
  //         if (!userEmail) throw new Error("User email is missing!");
  //         url = `${apiUrl}/public/order/all`;
  //     } else if (resource === 'users') {
  //         url = `${apiUrl}/admin/users?${new URLSearchParams(query)}`;
  //     } else {
  //         url = `${apiUrl}/public/${resource}?${new URLSearchParams(query)}`;
  //     }

  //     console.log('Request URL:', url);

  //     return httpClient.get(url).then(({ json }) => {
  //         console.log('API Response:', json);

  //         const idField = idFieldMapping[resource] || 'id';

  //         if (Array.isArray(json)) {
  //             const data = json.map((item: any) => ({
  //                 id: item[idField],
  //                 ...item,
  //             }));
  //             return {
  //                 data,
  //                 total: data.length,
  //             };
  //         }

  //         const baseUrl = `${apiUrl}/public/products/image/`;

  //         const data = json.content.map((item: any) => ({
  //             id: item[idField],
  //             ...item,
  //             image: item.image ? `${baseUrl}${item.image}` : null,
  //         }));

  //         return {
  //             data,
  //             total: json.totalElements || 0,
  //         };
  //     });
  // },


  getList: (resource: string, { pagination = {}, sort = {}, filter = {} }) => {
    const { page = 0, perPage = 10 } = pagination ||{};
    const { field = 'id', order = 'ASC' } = sort;
    const userEmail = localStorage.getItem('username');
    const userRoles = JSON.parse(localStorage.getItem('roles') || '[]'); // Đảm bảo lấy đúng mảng roleIds
  
    const idFieldMapping: { [key: string]: string } = {
      products: 'productId',
      categories: 'categoryId',
      orders: 'orderId',
      brands: 'brandId',
      users: 'userId',
    };
  
    const idField = idFieldMapping[resource] || 'id';
    const query = {
      pageNumber: page.toString(),
      pageSize: perPage.toString(),
      sortBy: field,
      sortOrder: order,
      ...filter,
    };
  
    console.log('Request filter:', filter);
  
    let url: string;
  
    // Kiểm tra quyền và thiết lập URL tương ứng
    if (filter?.search) {
      const keyword = filter.search;
      delete query.search;
      url = `${apiUrl}/public/${resource}/keyword/${encodeURIComponent(keyword)}?${new URLSearchParams(query)}`;
    } else if (filter?.categoryId) {
      const categoryId = filter.categoryId;
      delete query.categoryId;
      url = `${apiUrl}/public/categories/${categoryId}/${resource}?${new URLSearchParams(query)}`;
    } else if (filter?.brandId) {
      const brandId = filter.brandId;
      delete query.brandId;
      url = `${apiUrl}/public/brands/${brandId}/${resource}?${new URLSearchParams(query)}`;
    } else if (resource === 'orders') {

      if (!userEmail) throw new Error("User email is missing!");
       url = `${apiUrl}/public/order/all`;    
      } else if (resource === 'users') {
      // Kiểm tra quyền để lấy danh sách người dùng
      if (!userRoles.includes("SUPER_ADMIN") && !userRoles.includes("ADMIN")) {
        throw new Error("Tài khoản không có quyền truy cập vào trang người dùng.");
      }
      url = `${apiUrl}/admin/users?${new URLSearchParams(query)}`; // Thay đổi URL cho users
    } else {
      // URL cho các resource khác
      url = `${apiUrl}/public/${resource}?${new URLSearchParams(query)}`;
    }
  
    console.log('Request URL:', url);
  
    return httpClient.get(url).then(({ json }) => {
      console.log('API Response:', json);
  
      const idField = idFieldMapping[resource] || 'id';
  
      if (Array.isArray(json)) {
        const data = json.map((item: any) => ({
          id: item[idField],
          ...item,
        }));
        return {
          data,
          total: data.length,
        };
      }
  
      const baseUrl = `${apiUrl}/public/products/image/`;
  
      const data = json.content.map((item: any) => ({
        id: item[idField],
        ...item,
        image: item.image ? `${baseUrl}${item.image}` : null,
      }));
  
      return {
        data,
        total: json.totalElements || 0,
      };
    });
  },
  
  

  delete: async <RecordType extends RaRecord = any>(
    resource: string,
    params: DeleteParams<RecordType>
  ): Promise<DeleteResult<RecordType>> => {
    try {
      const url = `${apiUrl}/admin/${resource}/${params.id}`;
      
      // Gọi API xóa
      await httpClient.delete(url);

      console.log(`Deleted ${resource} with ID:`, params.id);

      return { data: params.previousData as RecordType };
    } catch (error) {
      console.error(` Error deleting ${resource} with ID: ${params.id}`, error);
      throw new Error(`Không thể xóa ${resource}. Vui lòng thử lại!`);
    }
  },
  deleteMany: async <RecordType extends RaRecord = any>(
    resource: string,
    params: DeleteManyParams
  ): Promise<DeleteManyResult<RecordType>> => {
    try {
      const { ids } = params;

      // Chạy tất cả các yêu cầu xóa
      const deletePromises = ids.map(id => {
        const url = `${apiUrl}/admin/${resource}/${id}`;
        return httpClient.delete(url);
      });

      await Promise.all(deletePromises);

      console.log(`✅ Deleted ${resource} with IDs:`, ids);
      
      return { data: ids };
    } catch (error) {
      console.error(`❌ Error deleting ${resource} with IDs:`, params.ids, error);
      throw new Error(`Không thể xóa ${resource}. Vui lòng thử lại!`);
    }
  },

  
    getManyReference: function <RecordType extends RaRecord = any>(resource: string, params: GetManyReferenceParams &
      QueryFunctionContext): Promise<GetManyReferenceResult<RecordType>> {
      throw new Error('Function not implemented.');
    },
    updateMany: function <RecordType extends RaRecord = any>(resource: string, params: UpdateManyParams):
      Promise<UpdateManyResult<RecordType>> {
      throw new Error('Function not implemented.');
    },

    create: async (resource: string, params: CreateParams): Promise<CreateResult> => {
      let url = `${apiUrl}/admin/${resource}`;
      const { data } = params;
    
      // Xử lý riêng cho products (cần categoryId và brandId)
    if (resource === "products") {
      const { categoryId, brandId } = params.data;
      url = `${apiUrl}/admin/categories/${categoryId}/product/brands/${brandId}`;
      data.image = data.image || "default.png"; // Gán mặc định nếu chưa có
    }

      // Gửi yêu cầu POST
      const result = await httpClient.post(url, data);
    
      // Xác định tên trường ID cần gán cho react-admin
      let idKey = "id";
      if (["categories", "brands"].includes(resource)) idKey = `${resource.slice(0, -1)}Id`; // categoryId, brandId
      if (resource === "products") idKey = "productId";

      return { data: { ...data, id: result.json[idKey] || result.json.id } };
    },
    
    
    update: async (resource: string, params: UpdateParams): Promise<UpdateResult> => {
      if (resource === 'products') {
        const { categoryId, brandId, ...productData } = params.data;
        const url = `${apiUrl}/admin/categories/${categoryId}/product/${params.id}/brands/${brandId}`;
        const result = await httpClient.put(url, productData);
        return { data: { id: params.id, ...result.json } };
      }
    
      if (resource === 'orders') {
        const url = `${apiUrl}/public/order/${params.id}/update`;
      
        const payload = {
          orderStatus: params.data.orderStatus, // chỉ cần orderStatus thôi
        };
      
        const result = await httpClient.put(url, payload); // FIX: gửi thẳng payload
      
        return { data: { id: params.id, ...result.json } };
      }
      
      if (resource === 'users') {
        const url = `${apiUrl}/public/users/${params.id}`;
        const result = await httpClient.put(url, params.data);
        return { data: { id: params.id, ...result.json } };
      }
      // Default update cho resource khác
      const url = `${apiUrl}/admin/${resource}/${params.id}`;
      const result = await httpClient.put(url, params.data);
      return { data: { id: params.id, ...result.json } };
    },
    
    getOne: async (resource, params) => {
      if (resource === "orders") {
        const username = localStorage.getItem("username") || "";
        const userId = localStorage.getItem("userId");
    
        const response = await fetch(`${apiUrl}/public/order/user/${userId}`);
        const orders = await response.json();
    
        const order = orders.find((o: any) => o.orderId === Number(params.id));
    
        if (!order) throw new Error("Order not found");
    
        const data = {
          id: order.orderId,
          username, // dùng username từ localStorage
          orderStatus: order.orderStatus,
          totalAmount: order.totalAmount,
          orderDate: order.orderDate,
          fullname: order.fullname,
          address: order.address,
          phone: order.phone,
          userId: order.userId,
          orderItems: order.orderItems.map((item: any) => ({
            orderItemId: item.orderItemId,
            quantity: item.quantity,
            orderedProductPrice: item.orderedProductPrice,
            discount: item.discount,
            productId: item.productId,
            productName: item.productName,
            paymentMethod: item.paymentMethod,
            paymentStatus: item.paymentStatus,
            productImage: item.productImage
              ? `${apiUrl}/public/${item.productImage}`
              : null,
          })),
        };
    
        return { data };
      }
    
      // Default xử lý cho các resource khác
      const url = `${apiUrl}/public/${resource}/${params.id}`;
      const result = await httpClient.get(url);
      return { data: { id: result.json.id || params.id, ...result.json } };
    },
    


  getMany: async (resource: string, params: GetManyParams): Promise<GetManyResult> => {
    const idFieldMapping: { [key: string]: string } = {
        products: 'productId',
        categories: 'categoryId',
        brands:"brandId"
    // Add more mappings as needed
    };
    console.log('Request resource:', resource);
    console.log('Request params', params);
    const idField = idFieldMapping [resource] || 'id';
  // Construct the URL with the selected IDs
    const ids = params.ids.join(',');
  // const url = `${apiUrl}/public/${resource}?${idField}=${ids}`;
  let url: string;
  if (resource === "products") {
    url = `${apiUrl}/public/categories/${ids}/${resource}`;
    }
  else {
      url = `${apiUrl}/public/${resource}`;
  }
    console.log('Request URL getMany:', url);
    // Perform the GET requests
    const result = await httpClient.get(url);
    console.log('Request result:', result);
    console.log('Request result JSON:', result.json);
    // Map the results to include the correct 'id' field
    const data = result.json.content.map((item: any) => ({
        id: item[idField],
            ...item,
    }));
    return { data };
  },
    
  };
