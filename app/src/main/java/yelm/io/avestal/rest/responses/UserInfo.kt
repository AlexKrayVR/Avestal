package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfo  (
    @SerializedName("id") var id : String,
    @SerializedName("phone") var phone : String,
    @SerializedName("permission") var permission : String,
    @SerializedName("is_verified") var isVerified : Boolean,
    @SerializedName("rating") var rating : String,
    @SerializedName("fio") var userFIO : UserFIO,
    @SerializedName("data") var data : UserData
) :Serializable