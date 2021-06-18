package yelm.io.avestal.main.user.settings.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentPhoneBinding
import yelm.io.avestal.main.user.settings.HostSettings
import yelm.io.avestal.auth.common.PhoneTextFormatter

private const val PHONE = "phone"

class PhoneFragment : Fragment() {
    private var phone: String? = null
    private var binding: FragmentPhoneBinding? = null
    private var hostSettings: HostSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phone = it.getString(PHONE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.back?.setOnClickListener {
            hostSettings?.back()
        }

        binding?.phone?.addTextChangedListener(
            PhoneTextFormatter(
                binding?.phone,
                "+7 (###) ###-##-##"
            )
        )
        binding?.save?.setOnClickListener {
            val userPhone = binding?.phone?.text.toString()
            val phoneFinal = userPhone.replace("\\D".toRegex(), "")
            if (phoneFinal.length != 11) {
                hostSettings?.showToast(R.string.enterCorrectPhoneNumber)
            } else {
                hostSettings?.showToast(R.string.settingsPhoneSaved)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String) =
            PhoneFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)
                }
            }
    }

    override fun onDetach() {
        super.onDetach()
        hostSettings = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostSettings) {
            hostSettings = activity as HostSettings
        } else {
            throw RuntimeException(activity.toString() + " must implement HostSettings interface")
        }
    }


}