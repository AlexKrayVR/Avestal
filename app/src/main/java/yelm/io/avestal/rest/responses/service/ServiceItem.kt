package yelm.io.avestal.rest.responses.service

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServiceItem (
    @SerializedName("id") var id : String,
    @SerializedName("name") var name : String,
    @SerializedName("description") var description : String,
    @SerializedName("specification") var specification : List<Specification>,
    @SerializedName("price") var price : String,
    @SerializedName("images") var images : List<String>,
    @SerializedName("preview_image") var previewImage : String,
    @SerializedName("status") var status : String,
    @SerializedName("status_material") var statusMaterial : String,
    @SerializedName("type") var type : String,
    @SerializedName("unit_type") var unitType : String,
    @SerializedName("quantity") var quantity : String,
    @SerializedName("discount") var discount : String,
    @SerializedName("category_id") var categoryId : String,
    @SerializedName("subcategory_id") var subcategoryId : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String

): Serializable