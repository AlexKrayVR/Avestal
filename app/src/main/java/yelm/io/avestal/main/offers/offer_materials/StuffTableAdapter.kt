package yelm.io.avestal.main.offers.offer_materials

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ItemStuffTableBinding
import yelm.io.avestal.databinding.ItemStuffTableTitleBinding
import yelm.io.avestal.rest.responses.service.ServiceItem

class StuffTableAdapter(private var items: List<ServiceItem>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM = 0
    private val TYPE_HEADER = 1

    fun getData(): List<ServiceItem> {
        return items
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val binding =
                ItemStuffTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding =
                ItemStuffTableTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            HeaderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ItemViewHolder) {
            val current = items[position - 1]
            holder.binding.count.text = current.quantity.toString()
            holder.binding.name.text = current.description

            val formattedPrice = priceFormat.format(current.price.toDouble())
            (formattedPrice + " " +
                    context.getString(R.string.ruble)).also { holder.binding.price.text = it }
        }
        if (holder is HeaderViewHolder) {

        }


    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    class ItemViewHolder(var binding: ItemStuffTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    class HeaderViewHolder(binding: ItemStuffTableTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }


}