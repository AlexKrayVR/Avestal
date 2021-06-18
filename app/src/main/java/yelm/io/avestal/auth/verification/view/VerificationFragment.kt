package yelm.io.avestal.auth.verification.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentVerificationBinding
import yelm.io.avestal.auth.host.HostAuth
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.verification.presenter.VerificationPresenter
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.responses.UserInfo

class VerificationFragment : Fragment(), OnBackPressedListener, VerificationView {

    private lateinit var verificationPresenter: VerificationPresenter

    private var binding: FragmentVerificationBinding? = null
    var array = arrayOf(' ', ' ', ' ', ' ')
    private var mHostAuth: HostAuth? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verificationPresenter = VerificationPresenter(this)
        fillUI()
        bindAction()
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
        })
    }

    private fun fillUI() {
        val phone = arguments?.getString(PHONE)
        val description = context?.getString(R.string.enterVerificationCode) + " " + phone
        binding?.description?.text = description
        val text = "<font color=${ContextCompat.getColor(requireContext(), R.color.color828282)}>" +
                "${context?.resources?.getString(R.string.codeDidNotCome)}" +
                "</font> <u><font color=${
                    ContextCompat.getColor(
                        requireContext(), R.color.colorBlue
                    )
                }>${
                    context?.resources?.getString(R.string.resend)
                }</font></u>"
        binding?.resend?.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
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
            verificationPresenter.resendPhone(arguments?.getString(PHONE) ?: "")
        }

        binding?.back?.setOnClickListener {
            mHostAuth?.openLoginFragment()
        }

        binding?.enter?.setOnClickListener {
            if (isCodeFulled()) {
                verificationPresenter.compareSHA2(
                    array.joinToString(""),
                    (arguments?.getSerializable(RESPONSE) as AuthResponse).hash ?: ""
                )
            } else {
                mHostAuth?.showToast(R.string.codeIncorrect)
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
                    verificationPresenter.compareSHA2(
                        array.joinToString(""),
                        (arguments?.getSerializable(RESPONSE) as AuthResponse).hash ?: ""
                    )
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
        fun newInstance(phone: String, response: AuthResponse) =
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
        mHostAuth?.openLoginFragment()
    }

    override fun onDestroyView() {
        binding = null
        verificationPresenter.detachView()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            mHostAuth = activity as HostAuth
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mHostAuth = null
    }

    override fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    override fun loginPhoneError(error: Int) {
        mHostAuth?.showToast(error)
    }

    override fun serverError(error: Int) {
        mHostAuth?.showToast(error)
    }


    override fun loginPhoneSuccess(response: AuthResponse) {
        arguments?.putSerializable("RESPONSE", response)
    }

    override fun codeIsCorrect() {
        if ((arguments?.getSerializable(RESPONSE) as AuthResponse).auth == true) {
            verificationPresenter.getBearerToken(viewModel.user.value?.phone!!)
        } else {
            mHostAuth?.openWhatIsYourWorkFragment()
        }
    }

    override fun startApp(userInfo: UserInfo) {
        mHostAuth?.startApp(userInfo)
    }
}