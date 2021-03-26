package com.sample.data.network

import androidx.annotation.Keep

@Keep
data class LoginApiRequest(
    var username: String?,
    var password: String?,
) {
    constructor() : this(null, null)
}