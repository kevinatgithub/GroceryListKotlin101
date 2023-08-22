package com.kotlin101.group2.grocerylist.data.api.models

data class UpdateProfileRequest(val name: String?, val oldPassword: String?, val newPassword: String?, val avatar: String?)
