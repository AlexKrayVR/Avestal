package yelm.io.avestal.main.offers.offer_materials

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ActivityTotalBinding
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.service.ServiceItem
import java.math.BigDecimal

class TotalActivity : AppCompatActivity() {

    lateinit var adapter: StuffTableAdapter

    lateinit var binding: ActivityTotalBinding
    lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTotalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataWrapper = intent.getSerializableExtra("items") as DataWrapper
        val list: MutableList<ServiceItem> = dataWrapper.getServiceItems()
        id = intent.extras?.get("id") as String

        adapter = StuffTableAdapter(list, this)
        binding.recyclerStuff.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_stuff_table)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }

        binding.recyclerStuff.addItemDecoration(dividerItemDecoration)

        getTotalPrice()

        binding.back.setOnClickListener {
            finish()
        }

        binding.totalPrice.setOnClickListener {
            respond()
        }

    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun respond (){
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .response("Bearer ${SharedPreferencesSetting
                .getDataString(SharedPreferencesSetting.BEARER_TOKEN)}",
                id
            )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        showDialogNewOrder()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    hideLoading()
                }
            })
    }


    private fun showDialogNewOrder() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        val view = LayoutInflater.from(this)
            .inflate(
                R.layout.layout_dialog_confirm_order,
                this.findViewById(R.id.root),
                false
            )
        builder.setView(view)
        val alertDialog = builder.create()
        view.findViewById<TextView>(R.id.description).text = getText(R.string.responseSent)
        view.findViewById<TextView>(R.id.continueSearch).setOnClickListener {
            alertDialog.dismiss()
        }
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun getTotalPrice() {
        CoroutineScope(Dispatchers.IO).launch {
            var totalPrice = BigDecimal("0")
            val items = adapter.getData()
            for (item in items) {
                totalPrice =
                    totalPrice.add(BigDecimal(item.quantity).multiply(BigDecimal(item.price)))
            }

            val formattedPrice = priceFormat.format(totalPrice.toDouble())
            val text = getString(R.string.applyFor) + " " + formattedPrice + " " +
                    getString(R.string.ruble)

            launch(Dispatchers.Main) {
                binding.totalPrice.text = text
            }
        }
    }
}