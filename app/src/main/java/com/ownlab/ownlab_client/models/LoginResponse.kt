package com.ownlab.ownlab_client.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("Authorization")
    val token: String
)
