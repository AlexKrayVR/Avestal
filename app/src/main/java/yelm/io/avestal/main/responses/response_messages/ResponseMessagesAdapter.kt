package yelm.io.avestal.main.responses.response_messages

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.common.serverFormatterDate
import yelm.io.avestal.common.printedFormatterDate
import yelm.io.avestal.common.printedFormatterDateOfferActivity
import yelm.io.avestal.common.printedFormatterDateResponseMessage
import yelm.io.avestal.databinding.ItemOfferBinding
import yelm.io.avestal.databinding.ItemResponseBinding
import yelm.io.avestal.databinding.ItemResponseMessageBinding
import yelm.io.avestal.rest.responses.service.ServiceData
import yelm.io.avestal.rest.responses.user_responses.Messages
import yelm.io.avestal.rest.responses.user_responses.UserResponse
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

class ResponseMessagesAdapter(private var messages: List<Messages>, var context: Context) :
    RecyclerView.Adapter<ResponseMessagesAdapter.OfferItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferItemViewHolder {
        val binding =
            ItemResponseMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OfferItemViewHolder, position: Int) {
        holder.binding.message.text = messages[position].message

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(messages[position].updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.binding.data.text =  printedFormatterDateResponseMessage.format(currentCalendar.time)
    }


    override fun getItemCount(): Int {
        return messages.size
    }

    class OfferItemViewHolder(var binding: ItemResponseMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

}