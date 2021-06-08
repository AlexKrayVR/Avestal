package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName

data class ImageLink(
    @SerializedName("link") var link: String
)