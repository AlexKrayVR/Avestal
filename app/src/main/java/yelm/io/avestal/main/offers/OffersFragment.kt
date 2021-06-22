package yelm.io.avestal.main.offers

import android.app.AlertDialog
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
import yelm.io.avestal.main.offers.offer.controller.OfferActivity
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.Offer
import yelm.io.avestal.rest.responses.OfferData

class OffersFragment : Fragment() {

    private var binding: FragmentOffersBinding? = null
    private var adapter: OffersAdapter? = null

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
        setOffersSize(0)
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getOrders("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<Offer?> {
                override fun onResponse(
                    call: Call<Offer?>,
                    response: Response<Offer?>
                ) {
                    hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        initAdapter(response.body()!!.data)
                        setOffersSize(response.body()!!.data.size)
                    }
                }

                override fun onFailure(call: Call<Offer?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    hideLoading()
                }
            })

        binding?.searchOffer?.addTextChangedListener {
            adapter?.filter?.filter(it)
            binding?.recyclerOffers?.scrollToPosition(0)
        }
    }

    private fun initAdapter(offers: List<OfferData>) {
        Logging.logDebug("offers size: ${offers.size}")
        adapter = OffersAdapter(offers, requireContext())
        adapter?.setListener(object : OffersAdapter.Listener {
            override fun orderPressed(offerData: OfferData) {
                //TODO make variants: showDialogNewOrder() or start Intent
                val intent = Intent(requireContext(), OfferActivity::class.java)
                intent.putExtra(OfferData::class.java.name, offerData)
                requireContext().startActivity(intent)
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
}