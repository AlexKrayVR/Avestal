package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import yelm.io.avestal.auth.host.HostAuth
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.registration.interfaces.RegionView
import yelm.io.avestal.auth.registration.presenters.RegionPresenter
import yelm.io.avestal.databinding.FragmentRegionBinding
import yelm.io.avestal.rest.responses.user.UserInfo

class RegionFragment : MvpAppCompatFragment(), RegionView {
    private var hostAuth: HostAuth? = null
    private var binding: FragmentRegionBinding? = null
    private lateinit var viewModel: UserViewModel

    @InjectPresenter
    lateinit var regionPresenter: RegionPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    companion object {
        fun newInstance() = RegionFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding?.further?.setOnClickListener {
            regionPresenter.validateInput(binding?.regionEditText?.text.toString())
        }

        binding?.back?.setOnClickListener {
            hostAuth?.back()
        }

        binding?.close?.setOnClickListener {

        }

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
            throw RuntimeException(activity.toString() + " must implement HostRegistration interface")
        }
    }

    override fun validationRegionError(error: Int) {
        hostAuth?.showToast(error)
    }

    override fun validationRegionSuccess(region: String) {
        viewModel.setRegion(region)
        hostAuth?.openInfoFragment()
    }

    override fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    override fun serverError(error: Int) {
        hostAuth?.showToast(error)
    }

    override fun startApp(userInfo: UserInfo) {
        hostAuth?.startApp(userInfo)
    }
}