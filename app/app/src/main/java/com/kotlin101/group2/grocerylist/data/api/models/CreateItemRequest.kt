package com.group2.kotlinlab.data.models

data class CreateItemRequest(
    val alternativeItemId: Int,
    val description: String,
    val name: String,
    val price: Int
)