package yelm.io.avestal.main.responses

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
import yelm.io.avestal.databinding.ItemResponseBinding
import yelm.io.avestal.rest.responses.service.ServiceData
import yelm.io.avestal.rest.responses.user_responses.UserResponse
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

class ResponsesAdapter(private var responses: List<UserResponse>, var context: Context) :
    RecyclerView.Adapter<ResponsesAdapter.OfferItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferItemViewHolder {
        val binding = ItemResponseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferItemViewHolder, position: Int) {
        initViews(holder, responses[position])
        initActions(holder, responses[position])
    }

    private fun initViews(holder: OfferItemViewHolder, response: UserResponse) {
        holder.binding.responseTitle.text = response.service.title
        holder.binding.city.text = response.service.address
        holder.binding.responseStatus.text = response.status
        val formattedPrice = DecimalFormat("###,###").format(response.service.price.toDouble())
        (context.getString(R.string.before) + " " + formattedPrice + " " +
                context.getString(R.string.ruble)).also { holder.binding.price.text = it }


        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(response.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.binding.date.text = printedFormatterDate.format(currentCalendar.time)
    }

    private fun initActions(holder: OfferItemViewHolder, response: UserResponse) {
        holder.binding.root.setOnClickListener {

        }
    }


    override fun getItemCount(): Int {
        return responses.size
    }

    class OfferItemViewHolder(var binding: ItemResponseBinding) :
        RecyclerView.ViewHolder(binding.root)

}