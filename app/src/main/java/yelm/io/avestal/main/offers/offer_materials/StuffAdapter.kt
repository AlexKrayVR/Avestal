package yelm.io.avestal.main.offers.offer_materials

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ItemStuffBinding
import yelm.io.avestal.rest.responses.service.ServiceItem
import java.math.BigDecimal
import java.util.*

class StuffAdapter(private var items: MutableList<ServiceItem>, var context: Context) :
    RecyclerView.Adapter<StuffAdapter.StuffItemViewHolder>() {

    private var listener: Listener? = null

    interface Listener {
        fun changed()
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StuffItemViewHolder {
        val binding = ItemStuffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StuffItemViewHolder(binding)
    }

    fun setData(items: ArrayList<ServiceItem>) {
        this.items = items
        //notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StuffItemViewHolder, position: Int) {

        val current = items[position]

        Glide.with(context)
            .load("https://img3.goodfon.ru/original/1152x864/f/dc/cvet-pyatno-relef-kraski-4336.jpg")
            .transform(
                CenterCrop(), RoundedCorners(
                    context.resources.getDimension(R.dimen.dimens_16dp)
                        .toInt()
                )
            )
            .into(holder.binding.image)

        holder.binding.count.text = current.quantity
        holder.binding.name.text = current.description

        val formattedPrice = priceFormat.format(current.price.toDouble())
        (formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }


        holder.binding.reduce.setOnClickListener {
            reduceItemQuantity(position)
        }

        holder.binding.increase.setOnClickListener {
            addItemQuantity(position)
        }
    }

    private fun addItemQuantity(position: Int) {
        items[position].quantity = BigDecimal(items[position].quantity).add(BigDecimal("1")).toString()
        //notifyItemChanged(position)
        notifyDataSetChanged()
        listener?.changed()
    }

    private fun reduceItemQuantity(position: Int) {

        if (items[position].quantity == "1") {
            items.removeAt(position)
            //notifyItemRemoved(position);
            //notifyItemRangeChanged(position, items.size);
        }else{
            items[position].quantity = BigDecimal(items[position].quantity).subtract(BigDecimal("1")).toString()
            //notifyItemChanged(position)
        }
        notifyDataSetChanged()

        listener?.changed()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getData(): List<ServiceItem> {
        return items
    }

    class StuffItemViewHolder(var binding: ItemStuffBinding) :
        RecyclerView.ViewHolder(binding.root)
}