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
import yelm.io.avestal.database.BasketItem
import yelm.io.avestal.databinding.ItemBasketBinding
import yelm.io.avestal.databinding.ItemStuffBinding
import yelm.io.avestal.main.offers.offer_materials.mode.Stuff
import java.text.DecimalFormat
import java.util.*

class StuffAdapter(private var items: List<Stuff>, var context: Context) :
    RecyclerView.Adapter<StuffAdapter.StuffItemViewHolder>() {

    private var listener: Listener? = null

    interface Listener {
        fun increase(itemID: String)
        fun reduce(itemID: String)
        fun deleteByID(itemID: String)
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

    fun setData(items: ArrayList<Stuff>) {
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

        holder.binding.count.text = current.count.toString()
        holder.binding.name.text = current.name

        val formattedPrice = priceFormat.format(current.price.toDouble())
        (context.getString(R.string.before) + " " + formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }


        holder.binding.reduce.setOnClickListener {
            if (current.count == 1) {
                //notifyItemRemoved(position);
                //notifyItemRangeChanged(position, favorites.size());
                listener?.deleteByID(current.itemID)
            } else {
                listener?.reduce(current.itemID)
            }
        }

        holder.binding.increase.setOnClickListener {
            listener?.increase(current.itemID)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getData(): List<Stuff> {
        return items
    }

    class StuffItemViewHolder(var binding: ItemStuffBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}