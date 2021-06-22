package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentFullNameBinding
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.host.HostAuth

class FullNameFragment : Fragment() {

    companion object {
        fun newInstance() = FullNameFragment()
    }

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentFullNameBinding? = null
    private var hostAuth: HostAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullNameBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val text = "<font color=${ContextCompat.getColor(requireContext(), R.color.color828282)}>" +
                "${context?.resources?.getString(R.string.acceptPublicOfferStart)}" +
                "</font> <u><font color=${
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBlue
                    )
                }>${
                    context?.resources?.getString(R.string.acceptPublicOfferEnd)
                }</font></u>"
        binding?.checkBox?.text = Html.fromHtml(text)

        viewModel.user.observe(requireActivity(), {
            Logging.logDebug("FullNameFragment: $it")
            binding?.name?.setText(it.name)
            binding?.surname?.setText(it.surname)
            binding?.lastName?.setText(it.lastName)
        })

        binding?.further?.setOnClickListener {
            inputValidation()
        }

        binding?.back?.setOnClickListener {
            hostAuth?.back()
        }
    }

    private fun inputValidation() {
        if (binding?.name?.text.toString().trim().isEmpty()) {
            hostAuth?.showToast(R.string.nameMustBeFilled)
            return
        }

        if (binding?.surname?.text.toString().trim().isEmpty()) {
            hostAuth?.showToast(R.string.surnameMustBeFilled)
            return
        }

        if (binding?.lastName?.text.toString().trim().isEmpty()) {
            hostAuth?.showToast(R.string.lastNameMustBeFilled)
            return
        }

        if (binding?.checkBox?.isChecked == false) {
            hostAuth?.showToast(R.string.acceptPublicOffer)
            return
        }

        viewModel.setFullName(
            binding?.name?.text.toString().trim(),
            binding?.surname?.text.toString().trim(),
            binding?.lastName?.text.toString().trim()
        )
        hostAuth?.openRegionFragment()
    }

    override fun onDetach() {
        super.onDetach()
        hostAuth = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            hostAuth = activity as HostAuth
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }
}