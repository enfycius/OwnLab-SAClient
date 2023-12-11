package com.ownlab.ownlab_client.models

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("name")
    val name: String,
    @SerializedName("pwd")
    val password: String
)
