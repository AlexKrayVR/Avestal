package yelm.io.avestal.main.offers

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentOffersBinding

class OffersFragment : Fragment() {

    private var binding: FragmentOffersBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "<font color=${context?.resources?.getColor(R.color.textColor121212)}>" +
                "${context?.resources?.getString(R.string.fromQuestionnaire)}" + ' ' +
                "</font><font color=${context?.resources?.getColor(R.color.textColorBDBDBD)}>${0}</font>"
        binding?.fromQuestionnaire?.text = Html.fromHtml(text)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
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