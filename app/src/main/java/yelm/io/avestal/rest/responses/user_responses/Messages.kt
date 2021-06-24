package yelm.io.avestal.rest.responses.user_responses

import com.google.gson.annotations.SerializedName

data class Messages (

    @SerializedName("id") var id : Int,
    @SerializedName("message") var message : String,
    @SerializedName("response_id") var responseId : Int,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String

)