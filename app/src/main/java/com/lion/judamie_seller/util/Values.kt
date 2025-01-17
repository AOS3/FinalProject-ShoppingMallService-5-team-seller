package com.lion.judamie_seller.util

// 사용자 상태
enum class UserState(val number:Int, val str:String){
    // 정상
    USER_STATE_NORMAL(1, "정상"),
    // 탈퇴
    USER_STATE_SIGNOUT(2, "탈퇴")
}

// 게시판 구분 값
enum class SellerFragmentType(val number:Int, val str:String){
    // 메인 화면
    SELLER_TYPE_MAIN(0, "메인 화면"),
    
    SELLER_TYPE_PRODUCT_MANAGEMENT(1, "상품 관리"),
    
    SELLER_TYPE_ORDER_LIST(2, "주문 관리"),

    SELLER_TYPE_SALES_LIST(3, "매출 내역"),

    SELLER_TYPE_INFO(4, "내 정보 관리"),

    SELLER_TYPE_ADD_PRODUCT(5, "상품 추가"),

    SELLER_TYPE_MODIFY_PRODUCT(6, "상품 수정"),

    SELLER_TYPE_DETAIL_PRODUCT(7, "상품 상세"),

    SELLER_TYPE_DETAIL_ORDER(8, "주문 상세 정보"),

    SELLER_TYPE_DETAIL_SALES(9, "매출 상세"),

    SELLER_TYPE_SHOW_ONE_PRODUCT_DETAIL(10, "제품 상세")
}
