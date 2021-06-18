package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserFIO(
    @SerializedName("surname") var surname: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("first_name") var firstName: String
) : Serializable