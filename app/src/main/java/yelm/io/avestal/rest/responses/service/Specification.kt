package yelm.io.avestal.rest.responses.service
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Specification (
    @SerializedName("name") var name : String,
    @SerializedName("value") var value : String
): Serializable