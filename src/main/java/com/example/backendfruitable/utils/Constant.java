package com.example.backendfruitable.utils;

public class Constant {

    //Constant các trạng thái của message
    public static final String SUCCESS_MESSAGE = "Thành công";
    public static final String SUCCESS_ADD_MESSAGE= "Thêm thành công";
    public static final String SUCCESS_UPDATE_MESSAGE = "Sửa thành công";
    public static final String SUCCESS_DELETE_MESSAGE = "Xoá thành công";
    public static final String VALIDATION_FAILED_MESSAGE = "Xác thực không thành công";

    // Constant với sản phẩm
    public static final String EMPTY_ALL_PRODUCT = "Không tồn tại sản phẩm nào";
    public static final String EMPTY_PRODUCT_BY_ID = "Không tìm thấy sản phẩm với id là: ";
    public static final String DELETE_SUCCESS_PRODUCT_BY_ID = "Xoá thành công sản phẩm với id là: ";
    public static final String ERROR_TO_DELETE_PRODUCT= "Lỗi khi xoá sản phẩm ";
    public static final String ERROR_TO_GET_PRODUCT = "Lỗi khi lấy sản phẩm: ";
    public static final String ERROR_TO_ADD_PRODUCT = "Lỗi khi thêm sản phẩm: ";
    public static final String ERROR_TO_UPDATE_PRODUCT = "Lỗi khi sửa sản phẩm: ";

    // Constant với danh mục sản phẩm
    public static final String EMPTY_CATEGORY_PRODUCT_BY_ID = "Không tìm thấy danh mục sản phẩm với id là: ";
    public static final String EMPTY_ALL_CATEGORY_PRODUCT = "Không tồn tại danh mục sản phẩm nào";
    public static final String ERROR_TO_GET_CATEGORY_PRODUCT = "Gặp lỗi trong quá trình lấy danh mục sản phẩm: ";
    public static final String ERROR_TO_ADD_CATEGORY_PRODUCT = "Gặp lỗi trong quá trình thêm danh mục sản phẩm";
    public static final String ERROR_TO_UPDATE_CATEGORY_PRODUCT = "Gặp lỗi trong quá trình sửa danh mục sản phẩm";
    public static final String ERROR_TO_DELETE_CATEGORY_PRODUCT = "Gặp lỗi trong quá trình xoá danh mục sản phẩm";

    // Constant với user
    public static final String EMPTY_USER_BY_ID = "Không tìm thấy user với id là: ";
    public static final String ERORR_TO_GET_USER = "Lỗi trong quá trình lấy user: ";
    public static final String EMPTY_ALL_USER = "Không tồn tại người dùng nào";

    // Các constant với các trạng thái code của HTTP STATUS
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;
    public static final int SUCCESS_CODE = 200;
    public static final int BAD_REQUEST_CODE = 400;
    public static final int NOT_FOUND_CODE = 404;
}
