package com.sample.constant

import com.sample.BuildConfig

/** The base URL of the API */
object Constants {
    const val BASE_URL: String = BuildConfig.BASE_URL

    enum class Status {
        //validation errors
        EMPTY_USERNAME,
        EMPTY_PASSWORD,
    }
}


