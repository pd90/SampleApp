package com.sample.data.model

import com.google.gson.annotations.SerializedName

data class Login (

    @SerializedName("errorCode") val errorCode : Int,
    @SerializedName("errorMessage") val errorMessage : String,
    @SerializedName("user") val user : User
)
