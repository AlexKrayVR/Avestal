package yelm.io.avestal.main.services.service.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.databinding.ItemOfferDetailsBinding
import yelm.io.avestal.main.services.OfferDetails
import java.util.*

class OfferDetailsAdapter(private var details: ArrayList<OfferDetails>, var context: Context) :
    RecyclerView.Adapter<OfferDetailsAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding =
            ItemOfferDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    fun addItems(details: ArrayList<OfferDetails>) {
        this.details = details
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.item.text = details[position].item
        holder.binding.value.text = details[position].value
    }

    override fun getItemCount(): Int {
        return details.size
    }

    class ImageViewHolder(var binding: ItemOfferDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}