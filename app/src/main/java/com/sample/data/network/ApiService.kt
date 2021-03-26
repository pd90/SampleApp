package com.sample.data.network

import com.sample.data.model.Login
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// define API endpoints for GET and POST here

/**
 * Get the list of the pots from the API
 */
interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body loginApiRequest: LoginApiRequest): Response<Login>
}