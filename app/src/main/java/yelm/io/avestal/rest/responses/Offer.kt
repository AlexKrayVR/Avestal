package yelm.io.avestal.rest.responses

import com.google.gson.annotations.SerializedName

data class Offer (

    @SerializedName("current_page") var currentPage : String,
    @SerializedName("data") var data : List<OfferData>,
    @SerializedName("first_page_url") var firstPageUrl : String,
    @SerializedName("from") var from : String,
    @SerializedName("next_page_url") var nextPageUrl : String,
    @SerializedName("path") var path : String,
    @SerializedName("per_page") var perPage : String,
    @SerializedName("prev_page_url") var prevPageUrl : String,
    @SerializedName("to") var to : String

)