package yelm.io.avestal.rest.responses.user_responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Service (

    @SerializedName("title") var title : String,
    @SerializedName("address") var address : String,
    @SerializedName("price") var price : Int

): Serializable