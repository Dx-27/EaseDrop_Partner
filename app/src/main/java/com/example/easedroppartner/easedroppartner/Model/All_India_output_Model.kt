package com.example.easedroppartner.easedroppartner.Model

class All_India_output_Model {

    var status: Boolean = true
    var message: String? = null
    var packages: ArrayList<All_India_Delivery_Model>? = null

    constructor(status: Boolean, message: String?, packages: ArrayList<All_India_Delivery_Model>?) {
        this.status = status
        this.message = message
        this.packages = packages
    }

}