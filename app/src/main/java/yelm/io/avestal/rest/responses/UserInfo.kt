package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName

class UserInfo (
    @SerializedName("id") var id : String,
    @SerializedName("phone") var phone : String,
    @SerializedName("permission") var permission : String,
    @SerializedName("fio") var userFIO : UserFIO,
    @SerializedName("data") var data : UserData
)