package com.kotlin101.group2.grocerylist.data.api.models

 data class Item(
    var alternativeItemId: Int,
    var cartId: Int,
    var description: String,
    var id: Int,
    var img: Any?,
    var imgUrl: Any?,
    var isPrimary: Boolean,
    var name: String,
    var pricePerUnit: Double,
    var quantity: Int,
    var status: Int,
    var totalPrice: Double
)