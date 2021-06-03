package yelm.io.avestal.main.user.settings.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentMailBinding
import yelm.io.avestal.main.user.settings.HostSettings
import java.util.regex.Matcher
import java.util.regex.Pattern

class MailFragment : Fragment() {
    private var hostSettings: HostSettings? = null
    private var binding: FragmentMailBinding? = null
    private val VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.back?.setOnClickListener {
            hostSettings?.back()
        }

        binding?.save?.setOnClickListener {
            val userMail = binding?.mail?.text.toString()
            if (validateEmail(userMail)) {
                hostSettings?.showToast(R.string.settingsMailSaved)
            } else {
                hostSettings?.showToast(R.string.settingsMailDoesNotExist)
            }
        }
    }

    private fun validateEmail(emailStr: String?): Boolean {
        val matcher: Matcher =
            VALID_EMAIL_ADDRESS_REGEX.matcher(
                emailStr.toString()
            )
        return matcher.find()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MailFragment().apply {
                arguments = Bundle().apply {

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