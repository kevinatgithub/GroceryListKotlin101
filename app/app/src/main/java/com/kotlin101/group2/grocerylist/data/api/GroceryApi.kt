package com.kotlin101.group2.grocerylist.data.api

import com.kotlin101.group2.grocerylist.data.api.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GroceryApi {

    @POST("user/signup")
    suspend fun signUp(@Body signup: SignUpRequest) : Response<TextResponse>;

    @POST("user/signin")
    suspend fun signIn(@Body signin: SignInRequest) : Response<TextResponse>;

    @POST("user/contactsignup")
    suspend fun contactSignUp(@Body contactSignUp: ContactSignUpRequest): Response<TextResponse>;

    @GET("user")
    suspend fun getCurrentUserInfo(@Header("Authorization") auth: String) : Response<UserResponse>;

    @PUT("user")
    suspend fun updateProfile(@Body updateProfile: UpdateProfileRequest, @Header("Authorization") auth: String) : Response<Any>

    @GET("cart/items")
    suspend fun getCartItems(@Header("Authorization") auth: String) : Response<ArrayList<Item>>;

    @POST("cart/sync")
    suspend fun syncCart(@Body syncRequest : SyncRequest, @Header("Authorization") auth: String)

    @GET("cart/users")
    suspend fun getCartUsers(@Header("Authorization") auth: String) : ArrayList<User>

    @DELETE("cart/users")
    suspend fun deleteUserFromCart(@Query("email") email:String, @Header("Authorization") auth: String)

    @DELETE("cart")
    suspend fun deleteCart(@Header("Authorization") auth: String)

    @POST("items")
    suspend fun addItem(@Body item: CreateItemRequest, @Header("Authorization") auth: String) : Response<Item>

    @PUT("items/{itemId}")
    suspend fun updateItem(@Path("itemId") itemId: Int, @Body item: UpdateItemRequest, @Header("Authorization") auth: String) : Response<Item>

    @GET("items/{itemId}")
    suspend fun getItem(@Path("itemId") itemId: Int, @Header("Authorization") auth: String) : Response<Item>

    @DELETE("items/{itemId}")
    suspend fun deleteItem(@Path("itemId") itemId: Int, @Header("Authorization") auth: String)

    @GET("items/{itemId}/alternatives")
    suspend fun getItemAlternatives(@Path("itemId") itemId: Int, @Header("Authorization") auth: String) : Response<ArrayList<Item>>

    @POST("items/{itemId}/primary")
    suspend fun markItemAsPrimary(@Path("itemId") itemId: Int, @Header("Authorization") auth: String)

    @POST("items/{itemId}/done")
    suspend fun markItemAsDone(@Path("itemId") itemId: Int, @Header("Authorization") auth: String)

    @POST("items/{itemId}/notavailable")
    suspend fun markItemAsNotAvailable(@Path("itemId") itemId: Int, @Header("Authorization") auth: String)
}