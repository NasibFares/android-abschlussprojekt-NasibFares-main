package com.example.zuhauselernen.data.local.model

data class Subject(
    var subjectId: Int,
    var subjectName: String,
    var subjectImage: Int,
    var isChecked:Boolean,
    var monthlySubscriptionPrice: Double,
    var yearlySubscriptionPrice: Double,
    var isMonthlySubscribed: Boolean =false,
    var isYearlySubscribed: Boolean =false,
    var invoiceNumber:Int,
    var subscriptionPrice:Double,

)