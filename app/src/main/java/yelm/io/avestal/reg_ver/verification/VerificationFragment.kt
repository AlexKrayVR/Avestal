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
import yelm.io.avestal.reg_ver.registration.view.Communicator


class VerificationFragment : Fragment(), OnBackPressedListener {

    private var binding: FragmentVerificationBinding? = null
    var array = arrayOf(' ', ' ', ' ', ' ')
    private var communicator: Communicator? = null

    //test code
    val test = "1234"

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
        //TODO server request
    }

    @Suppress("DEPRECATION")
    private fun fillUI() {
        val phone = arguments?.getString(PHONE)
        val description = context?.getString(R.string.enterVerificationCode) + " " + phone
        binding?.description?.text = description
        val text = "<font color=${context?.resources?.getColor(R.color.textColorGray)}>" +
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
                Logging.logDebug("array: ${array.joinToString("")}")

                if (isCodeFulled()) {
                    if (array.joinToString("") == test) {
                        SharedPreferencesSetting.setData(
                            SharedPreferencesSetting.USER_NAME,
                            "USER"
                        )
                        communicator?.startApp()
                    } else {
                        communicator?.showToast(R.string.codeIncorrect)
                    }
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

    private fun isCodeFulled(): Boolean {
        for (i in array) {
            if (i == ' ') {
                return false
            }
        }
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String) =
            VerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)
                }
            }

        private const val PHONE = "PHONE"
    }

    override fun doBack() {
        arguments!!.getString(PHONE)?.let {
            communicator?.openRegistrationFragment(it)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is Communicator) {
            communicator = activity as Communicator
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        communicator = null
    }
}