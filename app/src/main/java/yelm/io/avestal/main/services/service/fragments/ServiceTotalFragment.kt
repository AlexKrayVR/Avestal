package yelm.io.avestal.main.services.service.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import yelm.io.avestal.databinding.FragmentServiceTotalBinding
import yelm.io.avestal.main.services.service.common.DataWrapper
import yelm.io.avestal.main.services.service.host.HostService
import yelm.io.avestal.main.services.service.adapters.StuffTableAdapter
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.service.ServiceItem
import java.math.BigDecimal


class ServiceTotalFragment : Fragment() {
    lateinit var items: MutableList<ServiceItem>
    lateinit var id: String
    private var hostService: HostService? = null
    lateinit var adapter: StuffTableAdapter

    private var _binding: FragmentServiceTotalBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID).toString()
            items = (it.get(ITEMS) as DataWrapper).getServiceItems()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceTotalBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        adapter = StuffTableAdapter(items, requireContext())
        binding.recyclerStuff.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_stuff_table)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }

        binding.recyclerStuff.addItemDecoration(dividerItemDecoration)

        getTotalPrice()
        binding.back.setOnClickListener {
            hostService?.back()
        }

        binding.totalPrice.setOnClickListener {
            respond()
        }
    }

    private fun setMaterialsCost(materialsCost: String) {
        val text = "<font color=${ContextCompat.getColor(requireContext(), R.color.colorBDBDBD)}>" +
                "${context?.resources?.getString(R.string.materialsCost)}" +
                "</font> <font color=${
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color121212
                    )
                }>${
                    priceFormat.format(materialsCost.toDouble()) + " " + context?.resources?.getString(
                        R.string.ruble
                    )
                }</font>"
        binding.materialsCost.text = Html.fromHtml(text)
    }


    private fun respond() {
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .response(
                "Bearer ${
                    SharedPreferencesSetting
                        .getDataString(SharedPreferencesSetting.BEARER_TOKEN)
                }",
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
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.layout_dialog_confirm_order,
                requireActivity().findViewById(R.id.container),
                false
            )
        builder.setView(view)
        val alertDialog = builder.create()
        view.findViewById<TextView>(R.id.description).text = getText(R.string.responseSent)
        view.findViewById<TextView>(R.id.continueSearch).setOnClickListener {
            alertDialog.dismiss()
            hostService?.closeActivity()
        }
        alertDialog.setCancelable(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }


    private fun getTotalPrice() {
        CoroutineScope(Dispatchers.IO).launch {
            var materialsCost = BigDecimal("0")
            val items = adapter.getData()
            for (item in items) {
                materialsCost =
                    materialsCost.add(BigDecimal(item.quantity).multiply(BigDecimal(item.price)))
            }
            val formattedPrice = priceFormat.format(materialsCost.toDouble())
            val text = getString(R.string.applyFor) + " " + formattedPrice + " " +
                    getString(R.string.ruble)
            launch(Dispatchers.Main) {
                binding.totalPrice.text = text
                setMaterialsCost(materialsCost.toString())

            }
        }
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }


    companion object {
        @JvmStatic
        fun newInstance(id: String, dataWrapper: DataWrapper) =
            ServiceTotalFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ITEMS, dataWrapper)
                    putString(ID, id)
                }
            }

        private const val ITEMS = "ITEMS"
        private const val ID = "ID"
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostService) {
            hostService = activity as HostService
        } else {
            throw RuntimeException(activity.toString() + " must implement HostService interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostService = null
    }


}