package com.kotlin101.group2.grocerylist.data.api.models

data class CreateItemRequest(
    val alternativeItemId: Int,
    val description: String,
    val img: Any,
    val imgUrl: Any,
    val name: String,
    val pricePerUnit: Double,
    val quantity: Int
)