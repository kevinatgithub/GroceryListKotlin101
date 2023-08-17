package com.group2.kotlinlab.data.models

data class UpdateItemRequest(
    val description: String,
    val name: String,
    val price: Int
)