package yelm.io.avestal.reg_ver.verification

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.FragmentVerificationBinding
import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponseKotlin
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class VerificationFragment : Fragment(), OnBackPressedListener {

    private var binding: FragmentVerificationBinding? = null
    var array = arrayOf(' ', ' ', ' ', ' ')
    private var hostRegistration: HostRegistration? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUI()
        bindAction()
    }

    @Suppress("DEPRECATION")
    private fun fillUI() {
        val phone = arguments?.getString(PHONE)
        val description = context?.getString(R.string.enterVerificationCode) + " " + phone
        binding?.description?.text = description
        val text = "<font color=${context?.resources?.getColor(R.color.color828282)}>" +
                "${context?.resources?.getString(R.string.codeDidNotCome)}" +
                "</font> <u><font color=${context?.resources?.getColor(R.color.colorBlue)}>${
                    context?.resources?.getString(R.string.resend)
                }</font></u>"
        binding?.resend?.text = Html.fromHtml(text)
    }

    private fun bindAction() {
        binding?.first?.let { setFocusChangeListener(it) }
        binding?.second?.let { setFocusChangeListener(it) }
        binding?.third?.let { setFocusChangeListener(it) }
        binding?.fourth?.let { setFocusChangeListener(it) }

        binding?.first?.let { addTextChangedListener(it, 0) }
        binding?.second?.let { addTextChangedListener(it, 1) }
        binding?.third?.let { addTextChangedListener(it, 2) }
        binding?.fourth?.let { addTextChangedListener(it, 3) }

        binding?.resend?.setOnClickListener {
        }

        binding?.back?.setOnClickListener {
            hostRegistration?.openLoginFragment()
        }

        binding?.enter?.setOnClickListener {
            if (isCodeFulled()) {
                compareSHA2()
            } else {
                hostRegistration?.showToast(R.string.codeIncorrect)
            }
        }
    }

    private fun setFocusChangeListener(editText: EditText) {
        editText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                editText.background = context?.let {
                    ContextCompat.getDrawable(it, R.drawable.back_enter_15dp)
                }
            } else {
                editText.background = context?.let {
                    ContextCompat.getDrawable(it, R.drawable.back_enter_under_focus)
                }
            }
        }
    }

    private fun addTextChangedListener(editText: EditText, index: Int) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    array[index] = ' '
                } else {
                    array[index] = s.toString()[0]
                }
                if (isCodeFulled()) {
                    compareSHA2()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })
    }

    private fun compareSHA2() {
        val sha2 = getSHA2(array.joinToString(""))
        Logging.logDebug("sha2: $sha2")
        val auth = arguments?.getSerializable(RESPONSE) as AuthResponseKotlin
        Logging.logDebug("sha2: ${auth.hash}")
        if (auth.hash == sha2) {
            SharedPreferencesSetting.setData(
                SharedPreferencesSetting.USER_NAME,
                "USER"
            )
            hostRegistration?.openWhatIsYourWorkFragment()
        } else {
            hostRegistration?.showToast(R.string.codeIncorrect)
        }
    }

    private fun isCodeFulled(): Boolean {
        for (i in array) {
            if (i == ' ') {
                return false
            }
        }
        return true
    }

    private fun getSHA2(s: String): String {
        try {
            // Create SHA2 Hash
            val digest = MessageDigest
                .getInstance("SHA-256")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Logging.logDebug("NoSuchAlgorithmException")
        }
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String, response: AuthResponseKotlin) =
            VerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)
                    putSerializable(RESPONSE, response)
                }
            }

        private const val PHONE = "PHONE"
        private const val RESPONSE = "RESPONSE"
    }

    override fun doBack() {
        hostRegistration?.openLoginFragment()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }
}