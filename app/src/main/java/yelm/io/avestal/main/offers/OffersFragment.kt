package yelm.io.avestal.main.offers

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentOffersBinding
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.Offer
import java.text.SimpleDateFormat
import java.util.*

class OffersFragment : Fragment() {

    private var binding: FragmentOffersBinding? = null
    var currentFormatterDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
//2021-06-03T07:38:35.000000Z
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

    var authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLmF2ZXN0YWwucnVcL2FwaVwvYXV0aCIsImlhdCI6MTYyMjgxMTA3NiwiZXhwIjoxNjIyODk3NDc2LCJuYmYiOjE2MjI4MTEwNzYsImp0aSI6ImpuRzhIdFo2MFpLbTdxSEoiLCJzdWIiOjEwLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.nQyvm-f71OvBLt3SMBzu3VHupdoFLe9jCKOq_9FDKX0"

    fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = "<font color=${context?.resources?.getColor(R.color.color121212)}>" +
                "${context?.resources?.getString(R.string.fromQuestionnaire)}" + ' ' +
                "</font><font color=${context?.resources?.getColor(R.color.colorBDBDBD)}>${0}</font>"
        binding?.fromQuestionnaire?.text = Html.fromHtml(text)

        showLoading()

        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getOrders("Bearer $authToken")
            .enqueue(object : Callback<Offer?> {
                override fun onResponse(
                    call: Call<Offer?>,
                    response: Response<Offer?>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        Logging.logDebug("onResponse${response.body()!!.data.size}");

                        val adapter = OffersAdapter(response.body()!!.data, requireContext())
                        binding?.recyclerOffers?.adapter = adapter

                    } else {
                        hideLoading()
                    }
                }

                override fun onFailure(call: Call<Offer?>, t: Throwable) {
                    Logging.logDebug("onResponse${t.message}");
                    hideLoading()
                }
            })
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