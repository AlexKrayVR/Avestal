package yelm.io.avestal.main.offers.offer.adapter

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
import yelm.io.avestal.rest.responses.OfferData
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

class OffersAdapter(private var offers: List<OfferData>, var context: Context) :
    RecyclerView.Adapter<OffersAdapter.OfferItemViewHolder>(), Filterable {

    var offersSort = offers.toMutableList()

    private var listener: Listener? = null

    interface Listener {
        fun orderPressed(offerData: OfferData)
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
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

    private fun initViews(holder: OfferItemViewHolder, offerData: OfferData) {
        holder.binding.offerTitle.text = offerData.title
        holder.binding.city.text = offerData.address
        val formattedPrice = DecimalFormat("###,###").format(offerData.price.toDouble())
        (context.getString(R.string.before) + " " + formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }

        fillStars(holder.binding.layoutStars, offerData.rating.toInt())

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(offerData.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.binding.date.text = printedFormatterDate.format(currentCalendar.time)
    }

    private fun initActions(holder: OfferItemViewHolder, offerData: OfferData) {
        holder.binding.root.setOnClickListener {
            listener?.orderPressed(offerData)
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
                val filtered: MutableList<OfferData> = mutableListOf()
                if (charSequence.toString().isEmpty()) {
                    filtered.addAll(offers)
                } else {
                    for (offer in offers) {
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
                offersSort = filterResults.values as MutableList<OfferData>
                offersSizeListener?.offersSize(offersSort.size)
                notifyDataSetChanged()
            }
        }
    }
}