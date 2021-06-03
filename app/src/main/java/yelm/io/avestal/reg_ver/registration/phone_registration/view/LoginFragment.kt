package yelm.io.avestal.reg_ver.registration.phone_registration.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.databinding.FragmentLoginBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponseKotlin
import yelm.io.avestal.reg_ver.registration.phone_registration.presenter.LoginPresenter
import java.lang.RuntimeException

class LoginFragment : Fragment(), RegistrationView {
    private lateinit var loginPresenter: LoginPresenter
    private var binding: FragmentLoginBinding? = null
    private var hostRegistration: HostRegistration? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
            binding?.phone?.setText(it.phone)
        })

        loginPresenter = LoginPresenter(this)
        binding?.phone?.let { loginPresenter.setTextFormatter(it) }

        //val phone = arguments?.getString(PHONE)
        //binding?.phone?.setText(phone)

        binding?.further?.setOnClickListener {
            auth()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }

    override fun auth() {
        loginPresenter.phoneValidation(binding?.phone?.text.toString())
    }

    override fun validationPhoneError(error: Int) {
        hostRegistration?.showToast(error)
    }

    override fun validationPhoneSuccess(phone: String,response: AuthResponseKotlin) {
        viewModel.setPhone(phone)
        hostRegistration?.openValidationFragment(phone, response)
    }

    override fun authPhoneSuccess() {
        hostRegistration?.startApp()
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
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

    override fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

}