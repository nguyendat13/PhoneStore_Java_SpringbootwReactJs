import axios from 'axios';
import {
  CreateParams, CreateResult, DataProvider, DeleteManyParams, DeleteManyResult, DeleteParams, DeleteResult,
  GetManyParams, GetManyReferenceParams, GetManyReferenceResult, GetManyResult, GetOneParams, GetOneResult, Identifier,
  QueryFunctionContext, RaRecord, UpdateManyParams, UpdateManyResult, UpdateParams, UpdateResult
} from 'react-admin';

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
  getList: (resource: string, { pagination = {}, sort = {}, filter = {} }) => {
    const { page = 0, perPage = 10 } = pagination;
    const { field = 'id', order = 'ASC' } = sort;
    const userEmail = localStorage.getItem('username');

    const idFieldMapping: { [key: string]: string } = {
        products: 'productId',
        categories: 'categoryId',
        carts: 'cartId',
        orders: 'orderId',
        brands:'brandId'
        // Add more mappings as needed
    };

    const idField = idFieldMapping[resource] || 'id'; // Lấy trường ID phù hợp cho từng resource
    const query = {
        pageNumber: page.toString(),
        pageSize: perPage.toString(),
        sortBy: field,
        sortOrder: order,
        ...filter,
    };

    console.log('Request filter:', filter);
    console.log('User email:', localStorage.getItem('username'));

    let url: string;
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


    } else if (resource === "carts") {
      url = `${apiUrl}/admin/${resource}`;

  } else if (resource === "orders") {
      if (!userEmail) throw new Error("User email is missing!");
      url = `${apiUrl}/public/users/${userEmail}/orders?${new URLSearchParams(query)}`;
  } else {
      url = `${apiUrl}/public/${resource}?${new URLSearchParams(query)}`;
  }

    console.log('Request URL:', url);

    return httpClient.get(url).then(({ json }) => {
        console.log('API Response:', json);

        // Trường hợp trả về là mảng
        if (Array.isArray(json)) {
            const data = json.map((item: any) => ({
                id: item[idField], // Map trường ID từ API
                ...item,
            }));
            return {
                data,
                total: data.length, // Tổng số phần tử
            };
        }

        // Trường hợp trả về dạng object với `content`
        const baseUrl = 'http://localhost:8080/api/public/products/image/';
        const data = json.content.map((item: any) => ({
            id: item[idField], // Map trường ID từ API
            ...item,
            image: item.image ? `${baseUrl}${item.image}` : null, // Xử lý ảnh
        }));

        console.log('Mapped data:', data);
        return {
            data,
            total: json.totalElements || 0, // Tổng số phần tử từ API
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
  
    // Default update cho resource khác
    const url = `${apiUrl}/admin/${resource}/${params.id}`;
    const result = await httpClient.put(url, params.data);
    return { data: { id: params.id, ...result.json } };
  },
  
getOne: async (resource: string, params: GetOneParams): Promise<GetOneResult> => {
  console.log('getOne called for resource:', resource, 'with params:', params);
  const username = localStorage.getItem('username');
  if (!username) {
      console.error("Error: No username found in localStorage!");
      return Promise.reject({ message: "No username found. Please log in again." });
  }
  let url: string;
  if (resource === "carts") {
      url = `${apiUrl}/public/users/${username}/carts/${params.id}`;
  } else if (resource === "orders") {
      url = `${apiUrl}/public/users/${username}/orders/${params.id}`;
  } else {
      url = `${apiUrl}/public/${resource}/${params.id}`;
  }

  const result = await httpClient.get(url);
  console.log('API Response:', result.json);

  const idFieldMapping: { [key: string]: string } = {
      products: 'productId',
      categories: 'categoryId',
      carts: 'cartId',
      orders: 'orderId',
  };
  const idField = idFieldMapping[resource] || 'id';
  const baseUrl = 'http://localhost:8080/api/public/products/image/'; // Base URL for product images

  let data;
  
  if (resource === "carts") {
      data = {
          id: result.json[idField], // Correctly mapping the ID field
          totalPrice: result.json.totalPrice,
          email: result.json.email, // Thêm email vào object
          products: result.json.products.map((product: any) => ({
              id: product.productId,
              productName: product.productName,
              image: product.image ? `${baseUrl}${product.image}` : null,
              description: product.description,
              quantity: product.quantity,
              price: product.price,
              discount: product.discount,
              specialPrice: product.specialPrice,
              category: product.category ? {
                  id: product.category.categoryId,
                  name: product.category.categoryName
              } : null,
          }))
      };
  } 
  else if (resource === "orders") {
      data = {
          id: result.json[idField],
          email: result.json.email,
          orderStatus: result.json.orderStatus,
          totalAmount: result.json.totalAmount,
          orderItems: Array.isArray(result.json.orderItems)
          ? result.json.orderItems.map((item: any) => ({
              orderItemId: item.orderItemId,
              quantity: item.quantity,
              orderedProductPrice: item.orderedProductPrice,
              discount: item.discount,
              product: item.product ? {
                  id: item.product.productId,
                  productName: item.product.productName,
                  image: item.product.image ? `${baseUrl}${item.product.image}` : null,
                  description: item.product.description,
                  categoryId: item.product.categoryId
              } : null
          }))
          : []
      
      };
  }
  else {
      data = {
          id: result.json[idField],
          ...result.json
      };
  }

  console.log('Formatted data:', data);
  return { data };
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
