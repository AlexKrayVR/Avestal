package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OfferData (

    @SerializedName("id") var id : String,
    @SerializedName("title") var title : String,
    @SerializedName("text") var text : String,
    @SerializedName("price") var price : String,
    @SerializedName("address") var address : String,
    @SerializedName("geolocation") var geolocation : List<Double>,
    @SerializedName("images") var images : List<String>,
    @SerializedName("files") var files : List<String>,
    @SerializedName("items") var items : List<String>,
    @SerializedName("user_id") var userId : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String,
    @SerializedName("rating") var rating : String

): Serializable