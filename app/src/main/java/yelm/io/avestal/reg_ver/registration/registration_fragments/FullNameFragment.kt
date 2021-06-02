package yelm.io.avestal.reg_ver.registration.registration_fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FullNameFragmentBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration

class FullNameFragment : Fragment() {

    companion object {
        fun newInstance() = FullNameFragment()
    }

    private lateinit var viewModel: UserViewModel
    private var binding: FullNameFragmentBinding? = null
    private var hostRegistration: HostRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FullNameFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        val text = "<font color=${context?.resources?.getColor(R.color.color828282)}>" +
                "${context?.resources?.getString(R.string.acceptPublicOfferStart)}" +
                "</font> <u><font color=${context?.resources?.getColor(R.color.colorBlue)}>${
                    context?.resources?.getString(R.string.acceptPublicOfferEnd)
                }</font></u>"

        binding?.checkBox?.text = Html.fromHtml(text)

        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
        })


        binding?.further?.setOnClickListener {
            if (binding?.name?.text.toString().trim().isEmpty()) {
                hostRegistration?.showToast(R.string.nameMustBeFilled)
                return@setOnClickListener
            }

            if (binding?.surname?.text.toString().trim().isEmpty()) {
                hostRegistration?.showToast(R.string.surnameMustBeFilled)
                return@setOnClickListener
            }

            if (binding?.lastName?.text.toString().trim().isEmpty()) {
                hostRegistration?.showToast(R.string.lastNameMustBeFilled)
                return@setOnClickListener
            }

            if (binding?.checkBox?.isChecked == false) {
                hostRegistration?.showToast(R.string.acceptPublicOffer)
                return@setOnClickListener
            }

            viewModel.setFullName(
                binding?.name?.text.toString().trim(),
                binding?.surname?.text.toString().trim(),
                binding?.lastName?.text.toString().trim()
            )
            hostRegistration?.openRegionFragment()

        }
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }
}