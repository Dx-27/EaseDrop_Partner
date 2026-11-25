package com.example.easedroppartner.easedroppartner.Model

object ConstantData
{

    val IS_LOGIN: String ="false"
    var KEY_LAT_LONG=""

    const val serveraddress="http://192.168.1.74:8000/"
    const val loginUrl="logindb"

    //instant delivery

    //instant get package
    const val instant_url="get/instant/delivery"

    //instant cancel package
    const val cancelUrl="instant_cancelPackage"
    const val get_delevered="delivered/package/boy/instant"
    const val get_cancel="cancelled/package/boy/instant"

    //instant delivery package
    const val deliveredpackage="db_doneinstant_pack"

    //instant accept package
    const val accept_ins_Package="accept_ins_Package"

//---------------------all india-----------------------

    //accept all india
    const val allindiapackageurl="acceptorder"
    const val  db_doneindia_pack="db_doneindia_pack"
    //cancel all india
    const val allindiacancel="cancelPackage"

    //delivered all india
    const val allindiadelivery="DeliveryBoydeliveredPackage"
    const val get_cancelallindia="delivery/cancel/package"

  //  /delivered/package/boy

    //get package all india
    const val allindiagetpackagedelivery="get/Package/delevery"





    const val deliveredUrl="delivery/done/package"
    const val acceptUrl="acceptorder"
    const val instant_cancelledPackages="instant_cancelledPackages"
    const val instant_DeliveredPackages="instant_DeliveredPackages"




    const val SP="sp_login"
    const val KEY_ID="key_id"
    const val KEY_EMAIL="key_email"
    const val KEY_BIRTHDATE="key_birthday"
    const val KEY_PASSWORD="key_password"
    const val KEY_NAME="key_name"
    const val KEY_PIC="key_pic"
    const val KEY_ADDRESS="key_address"
    const val KEY_ADHAR_NO="key_adhar_no"
    const val KEY_PHONE="key_phone"
    const val KEY_GENDER="key_gender"
    const val KEY_LIC_NO="key_lic_no"

}