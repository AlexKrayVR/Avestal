package yelm.io.avestal.rest.responses.items

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Specification(
    @SerializedName("name") var name: String,
    @SerializedName("value") var value: String
) : Serializable