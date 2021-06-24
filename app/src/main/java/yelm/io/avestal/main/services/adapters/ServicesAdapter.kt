package yelm.io.avestal.main.services.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.common.serverFormatterDate
import yelm.io.avestal.common.printedFormatterDate
import yelm.io.avestal.databinding.ItemOfferBinding
import yelm.io.avestal.rest.responses.service.ServiceData
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

class ServicesAdapter(private var services: MutableList<ServiceData>, var context: Context) :
    RecyclerView.Adapter<ServicesAdapter.OfferItemViewHolder>(), Filterable {

    var offersSort = services.toMutableList()

    private var listener: Listener? = null

    interface Listener {
        fun orderPressed(serviceData: ServiceData)
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun clear(){
        offersSort.clear()
        notifyDataSetChanged()
    }


    private var offersSizeListener: OffersSizeListener? = null

    interface OffersSizeListener {
        fun offersSize(offersSize: Int)
    }

    fun setOffersSizeListener(offersSizeListener: OffersSizeListener?) {
        this.offersSizeListener = offersSizeListener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferItemViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferItemViewHolder, position: Int) {
        initViews(holder, offersSort[position])
        initActions(holder, offersSort[position])
    }

    private fun initViews(holder: OfferItemViewHolder, serviceData: ServiceData) {
        holder.binding.offerTitle.text = serviceData.title
        holder.binding.city.text = serviceData.address
        val formattedPrice = DecimalFormat("###,###").format(serviceData.price.toDouble())
        (context.getString(R.string.before) + " " + formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }

        fillStars(holder.binding.layoutStars, serviceData.rating.toInt())

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(serviceData.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.binding.date.text = printedFormatterDate.format(currentCalendar.time)
    }

    private fun initActions(holder: OfferItemViewHolder, serviceData: ServiceData) {
        holder.binding.root.setOnClickListener {
            listener?.orderPressed(serviceData)
        }
    }

    private fun fillStars(layout: LinearLayout, count: Int) {
        layout.removeAllViews()
        for (i in 0 until count) {
            val iv = ImageView(context)
            when (count) {
                5 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star5
                        )
                    )
                }
                4 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star4
                        )
                    )
                }
                3 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star3
                        )
                    )
                }
                2 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star2
                        )
                    )
                }
                else -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star2
                        )
                    )
                }
            }
            layout.addView(iv)
        }
    }

    override fun getItemCount(): Int {
        return offersSort.size
    }

    class OfferItemViewHolder(var binding: ItemOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            //run back
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filtered: MutableList<ServiceData> = mutableListOf()
                if (charSequence.toString().isEmpty()) {
                    filtered.addAll(services)
                } else {
                    for (offer in services) {
                        val search = charSequence.toString().lowercase(Locale.ROOT)
                        if (offer.text.lowercase(Locale.ROOT)
                                .contains(search) ||
                            offer.title.lowercase(Locale.ROOT)
                                .contains(search) ||
                            offer.address.lowercase(Locale.ROOT)
                                .contains(search) ||
                            offer.price.contains(search)
                        ) {
                            filtered.add(offer)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            //run ui
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                offersSort = filterResults.values as MutableList<ServiceData>
                offersSizeListener?.offersSize(offersSort.size)
                notifyDataSetChanged()
            }
        }
    }
}