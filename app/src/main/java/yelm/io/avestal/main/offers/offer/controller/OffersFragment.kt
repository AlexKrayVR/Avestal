package yelm.io.avestal.main.offers.offer.controller

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.FragmentOffersBinding
import yelm.io.avestal.main.host.AppActivity
import yelm.io.avestal.main.host.AppHost
import yelm.io.avestal.main.offers.offer.adapter.OffersAdapter
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.service.Service
import yelm.io.avestal.rest.responses.service.ServiceData
import java.lang.RuntimeException

class OffersFragment : Fragment() {

    private var binding: FragmentOffersBinding? = null
    private var adapter: OffersAdapter? = null
    private var appHost: AppHost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.searchOffer?.addTextChangedListener {
            adapter?.filter?.filter(it)
            binding?.recyclerOffers?.scrollToPosition(0)
        }
        getOffers()

    }


    private fun getOffers() {
        setOffersSize(0)
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getServices("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<Service?> {
                override fun onResponse(
                    call: Call<Service?>,
                    response: Response<Service?>
                ) {
                    hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        initAdapter(response.body()!!.data)
                        setOffersSize(response.body()!!.data.size)
                    }
                }

                override fun onFailure(call: Call<Service?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    hideLoading()
                }
            })
    }


    override fun onResume() {
        super.onResume()

    }

    private fun initAdapter(services: List<ServiceData>) {
        Logging.logDebug("offers size: ${services.size}")
        adapter = OffersAdapter(services, requireContext())
        adapter?.setListener(object : OffersAdapter.Listener {
            override fun orderPressed(serviceData: ServiceData) {

                //TODO return check
                //if (appHost?.isVerified() == true){
                val intent = Intent(requireContext(), OfferActivity::class.java)
                intent.putExtra(ServiceData::class.java.name, serviceData)
                requireContext().startActivity(intent)
//                }else{
//                    showDialogNewOrder()
//                }
            }
        })
        adapter?.setOffersSizeListener(object : OffersAdapter.OffersSizeListener {
            override fun offersSize(offersSize: Int) {
                setOffersSize(offersSize)
            }
        })
        binding?.recyclerOffers?.adapter = adapter
    }

    private fun setOffersSize(offersSize: Int) {
        val text = "<font color=${ContextCompat.getColor(requireContext(), R.color.color121212)}>" +
                "${context?.resources?.getString(R.string.fromQuestionnaire)}" + ' ' +
                "</font><font color=${
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBDBDBD
                    )
                }>${offersSize}</font>"
        binding?.fromQuestionnaire?.text =
            HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun showDialogNewOrder() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.layout_dialog_confirm_order,
                (context as AppActivity).findViewById(R.id.layoutDialogContainer)
            )
        builder.setView(view)
        val alertDialog = builder.create()
        view.findViewById<TextView>(R.id.continueSearch).setOnClickListener {
            alertDialog.dismiss()
        }
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OffersFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is AppHost) {
            appHost = activity as AppHost
        } else {
            throw RuntimeException(activity.toString() + " must implement AppHost interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        appHost = null
    }
}