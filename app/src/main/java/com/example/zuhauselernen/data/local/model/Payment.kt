package com.example.zuhauselernen.data.local.model

import java.sql.Date

data class Payment (
    var paymentId:Int,
    var paymentName:String,
    var paymentImage:Int,
    var isSelected:Boolean,
    var accountOwner:String,
    var cardNumber:String,
    var cardExpiryDate: Date?,
    var securityCode:String,
    var birthDate: Date?,
    var adress:String,
    var pLZ:String,
    var invoiceNumber:Int
)