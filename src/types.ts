// types.ts
export interface Pagination {
    page: number;
    perPage: number;
  }
  
  export interface Sort {
    field: string;
    order: 'ASC' | 'DESC';
  }
  
  export interface Filter {
    search?: string;
    categoryId?: string;
    brandId?: string;
  }
  