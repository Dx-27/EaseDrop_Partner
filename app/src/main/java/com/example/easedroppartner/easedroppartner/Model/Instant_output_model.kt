package com.example.easedroppartner.easedroppartner.Model

class Instant_output_model {

    var status = false
    var message:String?=null
    var packages: ArrayList<Instant_model>? = null

    constructor(status: Boolean, message: String?, packages: ArrayList<Instant_model>?) {
        this.status = status
        this.message = message
        this.packages = packages
    }
}