package com.kotlin101.group2.grocerylist.data.api.models

data class User(
    val cartId: Int,
    val email: String,
    val name: String,
    val avatar: String?
)