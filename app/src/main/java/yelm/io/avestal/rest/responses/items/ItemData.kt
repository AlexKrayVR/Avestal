package yelm.io.avestal.rest.responses.items

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ItemData(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("specification") var specification: List<Specification>,
    @SerializedName("price") var price: Int,
    @SerializedName("images") var images: List<String>,
    @SerializedName("preview_image") var previewImage: String,
    @SerializedName("status") var status: String,
    @SerializedName("status_material") var statusMaterial: Int,
    @SerializedName("type") var type: String,
    @SerializedName("unit_type") var unitType: Int,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("discount") var discount: Int,
    @SerializedName("category_id") var categoryId: Int,
    @SerializedName("subcategory_id") var subcategoryId: Int,
    @SerializedName("created_at") var createdAt: String,
    @SerializedName("updated_at") var updatedAt: String,
    @SerializedName("category") var category: Category
) :Serializable