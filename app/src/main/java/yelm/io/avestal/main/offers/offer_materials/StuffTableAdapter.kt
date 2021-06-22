package yelm.io.avestal.main.offers.offer_materials

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ItemStuffTableBinding
import yelm.io.avestal.main.offers.offer_materials.mode.Stuff

class StuffTableAdapter(private var items: List<Stuff>, var context: Context) :
    RecyclerView.Adapter<StuffTableAdapter.StuffTableItemViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StuffTableItemViewHolder {
        val binding = ItemStuffTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StuffTableItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: StuffTableItemViewHolder, position: Int) {
        val current = items[position]
        holder.binding.count.text = current.count.toString()
        holder.binding.name.text = current.name

        val formattedPrice = priceFormat.format(current.price.toDouble())
        (formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class StuffTableItemViewHolder(var binding: ItemStuffTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}