package yelm.io.avestal.reg_ver.registration.phone_registration.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import yelm.io.avestal.databinding.FragmentLoginBinding
import yelm.io.avestal.reg_ver.common.PhoneTextFormatter
import yelm.io.avestal.reg_ver.registration.phone_registration.presenter.LoginPresenter
import java.lang.RuntimeException

class LoginFragment : Fragment(), RegistrationView {
    private lateinit var loginPresenter: LoginPresenter

    private var binding: FragmentLoginBinding? = null
    private var mHostRegistration: HostRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginPresenter = LoginPresenter(this)
        binding?.phone?.addTextChangedListener(
            PhoneTextFormatter(
                binding?.phone,
                "+7 (###) ###-##-##"
            )
        )
        val phone = arguments?.getString(PHONE)
        binding?.phone?.setText(phone)

        binding?.further?.setOnClickListener {
            registration()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)
                }
            }

        private const val PHONE = "PHONE"
    }

    override fun registration() {
        loginPresenter.phoneValidation(binding?.phone?.text.toString())
    }

    override fun validationPhoneError(error: Int) {
        mHostRegistration?.showToast(error)
    }

    override fun validationPhoneSuccess(phone: String) {
        mHostRegistration?.openValidationFragment(phone)
    }

    //Because Fragments continue to live after the View has gone, it’s good to remove any references to the binding class instance
    override fun onDestroyView() {
        binding = null
        loginPresenter.detachView()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            mHostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mHostRegistration = null
    }

}