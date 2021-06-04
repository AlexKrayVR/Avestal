package yelm.io.avestal.main.offers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.common.currentFormatterDate
import yelm.io.avestal.common.printedFormatterDate
import yelm.io.avestal.databinding.ItemOfferBinding
import yelm.io.avestal.main.offers.respond.OfferActivity
import yelm.io.avestal.rest.responses.OfferData
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OffersAdapter(private var offers: List<OfferData>, var context: Context) :
    RecyclerView.Adapter<OffersAdapter.OfferItemViewHolder>() {



//    private var listener: Listener? = null
//
//    interface Listener {
//        fun increase(itemID: String)
//        fun reduce(itemID: String)
//        fun deleteByID(itemID: String)
//    }
//
//    fun setListener(listener: Listener?) {
//        this.listener = listener
//    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OffersAdapter.OfferItemViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferItemViewHolder(binding)
    }

    fun addItems(offers: ArrayList<OfferData>) {
        this.offers = offers
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OfferItemViewHolder, position: Int) {
        val current = offers[position]

        holder.binding.offerTitle.text = current.title
        holder.binding.city.text = current.address

        val formattedPrice = DecimalFormat("###,###").format(current.price.toInt())
        (context.getString(R.string.before) + " " + formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }
        fillStars(holder.binding.layoutStars, current.rating.toInt())
        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(currentFormatterDate.parse(current.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace();
        }

        holder.binding.date.text = printedFormatterDate.format(currentCalendar.time)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, OfferActivity::class.java)
            intent.putExtra(OfferData::class.java.name, current)
            context.startActivity(intent)
        }
//
//        holder.binding.increase.setOnClickListener {
//            listener?.increase(current.itemID)
//        }

    }

    private fun fillStars(layout: LinearLayout, count: Int) {
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
                1 -> {
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
        return offers.size
    }

    class OfferItemViewHolder(var binding: ItemOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}