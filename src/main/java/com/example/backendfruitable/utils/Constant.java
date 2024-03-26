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
    public static final String EMPTY_ALL_USER = "Không tồn tại người dùng nào";
    public static final String EMPTY_PASSWORD = "Mật kẩu không được để trống";
    public static final String ERORR_TO_GET_USER = "Lỗi trong quá trình lấy user: ";
    public static final String ERORR_TO_ADD_USER = "Lỗi trong quá trình thêm user:  ";
    public static final String ERORR_TO_DELETE_USER = "Lỗi trong quá trình xoá user:  ";
    public static final String ERORR_TO_UPDATE_USER = "Lỗi trong quá trình cập nhật user:  ";
    public static final String EXISTS_USER_USERNAME = "Đã tồn tại username trong hệ thống ";
    public static final String EXISTS_USER_EMAIL = "Đã tồn tại email trong hệ thống ";
    public static final String DELETE_SUCCESS_USER_BY_ID = "Xoá thành công user với id là: ";
    public static final String EMPTY_BASE64_IMAGE = "Chuỗi ảnh base 64 không được để trống hoặc rỗng";
    public static final String ERROR_USER_IMAGE_FOR_USER =  "Ảnh hiện tại bạn định sửa không đúng với người dùng hiện tại, vui lòng truyền đúng tên ảnh";

    // Constant với Authorize
    public static final String EMPTY_AUTHORIZE_BY_ID = "Không tìm thấy quyền hạn với id là: ";

    //constant với stock
    public static final String ERROR_STOCK_ID_COMPARE_PRODUCT_GET_STOCK_ID = "Id của số lượng không trùng với id số lượng của sản phẩm hiện tại nên không thể sửa";

    // Constant với categoryPost
    public static final String EMPTY_ALL_CATEGORY_POST = "Không tìm thấy danh mục bài viết nào";
    public static final String EMPTY_CATEGORY_POST_BY_ID = "Không tìm thấy danh mục bài viết với id là: ";
    public static final String ERROR_TO_GET_CATEGORY_POST = "Lỗi trong quá trình lấy danh mục bài viết: ";
    public static final String ERROR_TO_ADD_CATEGORY_POST = "Lỗi trong quá trình thêm danh mục bài viết: ";
    public static final String ERROR_TO_UPDATE_CATEGORY_POST = "Lỗi trong quá trình sửa danh mục bài viết: ";
    public static final String ERROR_TO_DELETE_CATEGORY_POST = "Lỗi trong quá trình xoá danh mục bài viết: ";

    // Constant với Post
    public static final String EMPTY_ALL_POST = "Không tìm thấy bài viết nào";
    public static final String EMPTY_POST_BY_ID = "Không tìm thấy bài viết với id là: ";
    public static final String ERROR_TO_GET_POST = "Lỗi trong quá trình lấy bài viết: ";
    public static final String ERROR_TO_ADD_POST = "Lỗi trong quá trình thêm bài viết: ";
    public static final String ERROR_TO_DELETE_POST = "Lỗi trong quá trình thêm bài viết: ";
    public static final String ERROR_TO_UPDATE_POST = "Lỗi trong quá trình sửa bài viết: ";
    public static final String DELETE_SUCCESS_POST_ID = "Xoá thành công bài viết với id là: ";
    public static final String EMPTY_POST_IMAGE_NAME = "Tên ảnh bị trống vui lòng truyền tên ảnh";

    //Constant với login
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String LOGIN_FAILED = "Đăng nhập không thành công, tài khoản hoặc mật khẩu không đúng";
    public static final String ERROR_TO_LOGIN = "Lỗi trong quá trình đăng nhập: ";

    // Các constant với các trạng thái code của HTTP STATUS
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;
    public static final int SUCCESS_CODE = 200;
    public static final int BAD_REQUEST_CODE = 400;
    public static final int NOT_FOUND_CODE = 404;
}
