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
}

enum class ProductState(val number:Int, val str:String){
    // 정상
    PRODUCT_STATE_NORMAL(1, "정상"),
    // 삭제
    PRODUCT_STATE_DELETE(2, "삭제"),
}

// 카테고리 구분 값
enum class ProductType(val number:Int, val str:String){
    PRODUCT_TYPE_ALL(0, "전체"),
    PRODUCT_TYPE_WINE(1, "와인"),
    PRODUCT_TYPE_WHISKEY(2, "위스키"),
    PRODUCT_TYPE_VODKA(3, "보드카"),
    PRODUCT_TYPE_TEQUILA(4, "데낄라"),
    PRODUCT_TYPE_DOMESTIC(5, "우리술"),
    PRODUCT_TYPE_SAKE(6, "사케"),
    PRODUCT_TYPE_RUM(7, "럼"),
    PRODUCT_TYPE_LIQUEUR(8, "리큐르"),
    PRODUCT_TYPE_CHINESE(9, "중국술"),
    PRODUCT_TYPE_BRANDY(10, "브랜디"),
    PRODUCT_TYPE_BEER(11, "맥주"),
    PRODUCT_TYPE_NON_ALCOHOL(12, "논알콜")
}

// 로그인 결과
enum class LoginResult(val number:Int, val str:String){
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호"),
    LOGIN_RESULT_SIGNOUT_MEMBER(4, "탈퇴한 회원"),
}