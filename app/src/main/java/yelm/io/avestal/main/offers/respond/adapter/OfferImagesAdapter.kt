package yelm.io.avestal.main.offers.respond.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.R
import yelm.io.avestal.databinding.ItemOfferImagesBinding
import java.util.*

class OfferImagesAdapter(private var images: ArrayList<String>, var context: Context) :
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
                CenterCrop(), RoundedCorners(
                    context.resources.getDimension(R.dimen.dimens_16dp)
                        .toInt()
                )
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