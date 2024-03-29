package yelm.io.avestal.auth.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.databinding.FragmentLoginBinding
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.host.HostAuth
import yelm.io.avestal.rest.responses.AuthResponse
import java.lang.RuntimeException

class LoginFragment : Fragment(), LoginView {
    private lateinit var loginPresenter: LoginPresenter
    private var binding: FragmentLoginBinding? = null
    private var hostAuth: HostAuth? = null
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
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding?.phone?.let {
            loginPresenter.setTextFormatter(it)
        }

        binding?.phone?.setText(viewModel.user.value?.phone)

        binding?.further?.setOnClickListener {
            loginPresenter.phoneValidation(binding?.phone?.text.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun loginPhoneError(error: Int) {
        hostAuth?.showToast(error)
    }

    override fun loginPhoneSuccess(phone: String, response: AuthResponse) {
        viewModel.setPhone(phone)
        hostAuth?.openVerificationFragment(response)
    }

    override fun onDestroyView() {
        binding = null
        loginPresenter.detachView()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            hostAuth = activity as HostAuth
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostAuth = null
    }

    override fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }
}