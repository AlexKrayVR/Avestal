package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token") var accessToken: String
)