package com.sample.data.network

import com.sample.data.model.Login
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService):ApiHelper  {
     override suspend fun login(loginApiRequest: LoginApiRequest): Response<Login> = apiService.login(loginApiRequest)
}