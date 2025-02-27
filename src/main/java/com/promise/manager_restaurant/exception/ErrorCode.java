package com.promise.manager_restaurant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception error!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key!", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User Existed!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003, "User Not Existed!", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1004, "Username Must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Password Must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated Error!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "you do not have permission!", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1009, "Invalid token!", HttpStatus.UNAUTHORIZED),
    PERMISSION_EXISTED(10010, "Permission already existed!", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(10011, "Permission not existed!", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(10012, "Role already existed!", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(10013, "Role not existed!", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSON_EXISTED(10014, "Role-permission already existed!", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSION_NOT_EXISTED(10015, "Role-permission not existed!", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(10016, "Email already existed!", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(10017, "Phone already existed!", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(10018, "Wrong password!", HttpStatus.BAD_REQUEST),
    USER_DONT_HAVE_ROLE(10019, "User don't have role!", HttpStatus.BAD_REQUEST),
    USER_LOCKED(10020, "User is locked!", HttpStatus.UNAUTHORIZED),
    //Category
    CATEGORY_EXISTED(10021, "Category already existed!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(10022, "Category not existed!", HttpStatus.BAD_REQUEST),
    //Restaurant
    RESTAURANT_EXISTED(10023, "Restaurant already existed!", HttpStatus.BAD_REQUEST),
    RESTAURANT_NOT_EXISTED(10024, "Restaurant not existed!", HttpStatus.BAD_REQUEST),
    TITLE_EXISTED(10025, "Title already existed!", HttpStatus.BAD_REQUEST),
    //Gallery
    GALLERY_EXISTED(10026, "Gallery already existed!", HttpStatus.BAD_REQUEST),
    GALLERY_NOT_EXISTED(10027, "Gallery not existed!", HttpStatus.BAD_REQUEST),
    GALLERY_NOT_BELONG_RESTAURANT(10028, "Gallery not belong to restaurant!", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED_IN_RESTAURANT(10029, "Category existed in restaurant!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED_IN_RESTAURANT(10030, "Category not existed in restaurant!", HttpStatus.BAD_REQUEST),
    FOOD_EXISTED(10031, "Food already existed!", HttpStatus.BAD_REQUEST),
    FOOD_NOT_EXISTED(10032, "Food not existed!", HttpStatus.BAD_REQUEST),
    FOOD_NOT_IN_RESTAURANT(10033, "Food not in restaurant!", HttpStatus.BAD_REQUEST),
    FOOD_IN_CATEGORY(10034, "Food already existed!", HttpStatus.BAD_REQUEST),
    //
    INVALID_PRICE(10033, "Invalid price!", HttpStatus.BAD_REQUEST),
    NOT_VERIFY_EMAIL(10034, "Not verify email!", HttpStatus.FORBIDDEN),
    EMAIL_NOT_FOND(10035, "Email not fonded!", HttpStatus.BAD_REQUEST),
    OTP_IS_USED(10036, "OTP is used!", HttpStatus.BAD_REQUEST),
    //Order
    STATUS_NOT_EXISTED(10037, "Status not existed!", HttpStatus.BAD_REQUEST),
    QUANTITY_EQUAL_OR_MORE_1(10038, "Quantity equal or more than 1!", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(10039, "Order not existed!", HttpStatus.BAD_REQUEST),
    ORDER_EXISTED(10040, "Order already existed!", HttpStatus.BAD_REQUEST),
    DELIVERY_NOT_EXISTED(10041, "Delivery not existed!", HttpStatus.BAD_REQUEST),
    CAN_NOT_UPDATE_ORDER(10042, "Can not update order!", HttpStatus.BAD_REQUEST),
    LIST_FOOD_IS_EMPTY(10043, "List food is empty!", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_NOT_EXISTED(10044, "Order status not existed!", HttpStatus.BAD_REQUEST),
    //RATING FOOD
    RATINGFOOD_NOT_EXISTED(10045, "Rating food not existed!", HttpStatus.BAD_REQUEST),
    YOU_MUST_BUY_PRODUCT(10046, "You must buy a product!", HttpStatus.BAD_REQUEST),
    RATINGRESTAURENT_NOT_EXISTED(10047, "Ratingrestaurent not existed!", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_NOT_EXISTED(10048, "Order item not existed!", HttpStatus.BAD_REQUEST),
    YOU_RATED_FOOD(10049, "You rated food!", HttpStatus.BAD_REQUEST),
    PROMO_NOT_EXISTED(10050, "Promo not existed!", HttpStatus.BAD_REQUEST),
    PROMO_INVALID(10051, "Promo percent invalid!", HttpStatus.BAD_REQUEST),
    DATE_PROMO_INVALID(10052, "Date promo invalid!", HttpStatus.BAD_REQUEST),
    PROMO_EXISTED(10053, "Promo already existed!", HttpStatus.BAD_REQUEST),
    ;
    private int errorCode;
    private String errorMsg;
    private HttpStatus httpStatus;

    ErrorCode(int errorCode, String errorMsg, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.httpStatus = httpStatus;
    }


}