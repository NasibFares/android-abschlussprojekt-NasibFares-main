package com.example.zuhauselernen.data.local.model

import com.example.zuhauselernen.data.remote.model.Game

data class UserData(
    var firstName: String,
    var lastName: String,
    var emailAdress: String,
    var password: String,
    var land: String,
    var city: String,
    var reason: String,
    var classLevel: String,
    var schoolType: String,
    var subjects: MutableList<Subject>,
    var invoices: MutableList<Invoice>,
    var userPhoto: String,
    var favourites:MutableList<Game>

)