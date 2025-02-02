package com.lion.judamie_seller.vo

import com.lion.judamie_seller.model.PickupLocationModel
import com.lion.judamie_seller.util.PickupStateType

class PickupLocationVO {
    // 픽업지 이름
    var pickupLocName = ""
    // 픽업지 도로명 주소
    var pickupLocStreetAddress = ""
    // 픽업지 상세 주소
    var pickupLocAddressDetail = ""
    // 픽업지 전화번호
    var pickupLocPhoneNumber = ""
    // 픽업지 추가 사항
    var pickupLocInformation = ""
    // 픽업지 상태
    var pickupLocState = 1
    // 시간
    var pickupLocTimeStamp = 0L

    fun toPickupLocationModel(pickupLocDocumentID:String) : PickupLocationModel {
        val pickupLocationModel = PickupLocationModel()

        pickupLocationModel.pickupLocDocumentID = pickupLocDocumentID
        pickupLocationModel.pickupLocName = pickupLocName
        pickupLocationModel.pickupLocStreetAddress = pickupLocStreetAddress
        pickupLocationModel.pickupLocAddressDetail = pickupLocAddressDetail
        pickupLocationModel.pickupLocPhoneNumber = pickupLocPhoneNumber
        pickupLocationModel.pickupLocInformation = pickupLocInformation
        pickupLocationModel.pickupLocTimeStamp = pickupLocTimeStamp


        when(pickupLocState){
            PickupStateType.PICKUP_STATE_NORMAL.num -> pickupLocationModel.pickupLocState = PickupStateType.PICKUP_STATE_NORMAL.num
            PickupStateType.PICKUP_STATE_DELETE.num -> pickupLocationModel.pickupLocState = PickupStateType.PICKUP_STATE_DELETE.num
        }

        return pickupLocationModel
    }
}
