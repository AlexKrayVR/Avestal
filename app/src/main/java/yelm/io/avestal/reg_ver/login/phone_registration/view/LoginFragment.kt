package yelm.io.avestal.reg_ver.login.phone_registration.view

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
import yelm.io.avestal.reg_ver.host.HostRegistration
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.reg_ver.login.phone_registration.presenter.LoginPresenter
import java.lang.RuntimeException

class LoginFragment : Fragment(), LoginView {
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

        loginPresenter = LoginPresenter(this)
        binding?.phone?.let { loginPresenter.setTextFormatter(it) }

        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
            binding?.phone?.setText(it.phone)
        })

        binding?.further?.setOnClickListener {
            loginPresenter.phoneValidation(binding?.phone?.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }

    override fun loginPhoneError(error: Int) {
        hostRegistration?.showToast(error)
    }

    override fun loginPhoneSuccess(phone: String, response: AuthResponse) {
        viewModel.setPhone(phone)
        hostRegistration?.openVerificationFragment(phone, response)
    }

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