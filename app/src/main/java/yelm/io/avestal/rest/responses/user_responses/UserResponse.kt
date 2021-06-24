package yelm.io.avestal.rest.responses.user_responses

import com.google.gson.annotations.SerializedName

data class UserResponse (

    @SerializedName("id") var id : Int,
    @SerializedName("service_id") var serviceId : Int,
    @SerializedName("user_id") var userId : Int,
    @SerializedName("status") var status : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String,
    @SerializedName("service") var service : Service,
    @SerializedName("messages") var messages : List<Messages>

)