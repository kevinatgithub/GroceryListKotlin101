package com.group2.kotlinlab.data.models

data class UserResponse(
    val cartId: Int,
    val cartItems: List<Any>,
    val email: String,
    val name: String
)