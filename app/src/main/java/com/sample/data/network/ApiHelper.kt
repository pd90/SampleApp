package com.sample.data.network

import com.sample.data.model.Login
import retrofit2.Response

interface ApiHelper {

    suspend fun login(loginApiRequest: LoginApiRequest): Response<Login>
}