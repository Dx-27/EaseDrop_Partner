package com.example.easedroppartner.easedroppartner.Model

class Package_output_Model {

    var status = false
    var message: String? = null
    var packages: ArrayList<Package_Model>? = null

    constructor(status: Boolean, message: String?, packages: ArrayList<Package_Model>?) {
        this.status = status
        this.message = message
        this.packages = packages
    }







}
