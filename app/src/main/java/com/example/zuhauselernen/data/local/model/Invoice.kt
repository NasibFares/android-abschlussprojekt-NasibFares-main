package com.example.zuhauselernen.data.local.model

import java.sql.Date

data class Invoice(
    var invoiceNumber:Int,
    var invoiceDate: Date = Date(System.currentTimeMillis()),
    var subjects:MutableList<Subject>,
    var payment:Payment,
    var invoiceSubtotal:Double,
    var isExpanded: Boolean = true
    )