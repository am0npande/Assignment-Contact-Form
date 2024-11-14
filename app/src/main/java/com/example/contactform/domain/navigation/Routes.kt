package com.example.contactform.domain.navigation

sealed class Routes(val routes:String) {

    object Page1 : Routes("page1")
    object Page2 : Routes("page2")
    object Page3 : Routes("page3")
    object Submit : Routes("submit")
    object Result : Routes("result")
}