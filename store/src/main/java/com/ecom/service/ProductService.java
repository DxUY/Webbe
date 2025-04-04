package com.ecom.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Product;

public interface ProductService {

    /**
     * Lưu (hoặc tạo) sản phẩm mới.
     */
    Product saveProduct(Product product);

    /**
     * Lấy tất cả sản phẩm (không phân trang).
     */
    List<Product> getAllProducts();

    /**
     * Xoá sản phẩm theo id.
     */
    Boolean deleteProduct(Integer id);

    /**
     * Lấy sản phẩm theo id.
     */
    Product getProductById(Integer id);

    /**
     * Update product + upload file.
     * Bỏ logic 'isActive' nếu DB không có.
     */
    Product updateProduct(Product product, MultipartFile file);

    /**
     * Lấy tất cả sản phẩm “active” (thực chất chỉ trả về tất cả
     * hoặc lọc theo category, tuỳ code).
     */
    List<Product> getAllActiveProducts(String category);

    /**
     * Tìm kiếm theo từ khoá ch (không phân trang).
     */
    List<Product> searchProduct(String ch);

    /**
     * Lấy tất cả sản phẩm “active” (hoặc full) + phân trang, 
     * tuỳ logic (nếu DB không có isActive, có thể filter theo category).
     */
    Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    /**
     * Tìm kiếm phân trang theo từ khoá ch.
     */
    Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);

    /**
     * Tìm kiếm phân trang “active” (thực chất tuỳ logic),
     * có thể filter theo category + từ khoá ch.
     */
    Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch);

    /**
     * Lấy tất cả sản phẩm + phân trang (không filter).
     */
    Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

}
