package yelm.io.avestal.rest.responses.items

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ItemsObject (

    @SerializedName("current_page") var currentPage : Int,
    @SerializedName("data") var data : List<ItemData>,
    @SerializedName("first_page_url") var firstPageUrl : String,
    @SerializedName("from") var from : Int,
    @SerializedName("next_page_url") var nextPageUrl : String,
    @SerializedName("path") var path : String,
    @SerializedName("per_page") var perPage : Int,
    @SerializedName("prev_page_url") var prevPageUrl : String,
    @SerializedName("to") var to : Int

): Serializable