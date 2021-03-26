package com.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sample.BuildConfig

@Entity
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("userId") val userId : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("token") val x_acc : String
){
    constructor() : this(0,"null","null",BuildConfig.Token)
}