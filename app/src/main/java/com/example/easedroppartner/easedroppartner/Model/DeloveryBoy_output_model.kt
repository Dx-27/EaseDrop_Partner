package com.example.easedroppartner.easedroppartner.Model

class DeloveryBoy_output_model {

    var status:Boolean=true
    var message:String?=null
    var user: Login_Model?=null

    constructor(status: Boolean, message: String?, user: Login_Model?){

        this.status = status
        this.message = message
        this.user = user
    }


}