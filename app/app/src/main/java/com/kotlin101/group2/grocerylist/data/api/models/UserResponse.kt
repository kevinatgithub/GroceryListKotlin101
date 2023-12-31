package com.kotlin101.group2.grocerylist.data.api.models

data class UserResponse(
    val cartId: Int,
    val cartItems: List<Any>,
    val email: String,
    var name: String,
    var avatar: String?
)