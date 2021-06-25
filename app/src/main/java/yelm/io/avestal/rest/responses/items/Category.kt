package yelm.io.avestal.rest.responses.items

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("image") var image : String,
    @SerializedName("status") var status : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String

):Serializable