package yelm.io.avestal.main.services.service.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import yelm.io.avestal.databinding.ItemOfferImagesBinding
import java.util.*

class OfferImagesAdapter(private var images: List<String>, var context: Context) :
    RecyclerView.Adapter<OfferImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = ItemOfferImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    fun addItems(images: ArrayList<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(images[position])
            .transform(
                CenterCrop()
            )
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImageViewHolder(var binding: ItemOfferImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}