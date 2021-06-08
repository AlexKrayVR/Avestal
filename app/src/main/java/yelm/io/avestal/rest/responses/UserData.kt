package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName

class UserData (
    @SerializedName("face_image") var faceImage : String,
    @SerializedName("job_status") var jobStatus : String,
    @SerializedName("region_name") var regionName : String,
    @SerializedName("profile_image") var profileImage : String,
    @SerializedName("passport_image") var passportImage : String,
    @SerializedName("job_description") var jobDescription : String
)