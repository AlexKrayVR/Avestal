package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentRegionBinding
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.host.HostAuth

class RegionFragment : Fragment() {
    private var mHostAuth: HostAuth? = null
    private var binding: FragmentRegionBinding? = null
    private lateinit var viewModel: UserViewModel

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
        viewModel.user.observe(requireActivity(), {
            Logging.logDebug("RegionFragment: $it")
        })

        binding?.region?.setText(viewModel.user.value?.region)

        binding?.further?.setOnClickListener {
            inputValidation()
        }
        binding?.back?.setOnClickListener {
            mHostAuth?.back()
        }
    }

    private fun inputValidation() {
        if (binding?.region?.text.toString().trim().isEmpty()) {
            mHostAuth?.showToast(R.string.addressEmpty)
            return
        }
        viewModel.setRegion(binding?.region?.text.toString().trim())
        mHostAuth?.openInfoFragment()
    }

    override fun onDetach() {
        super.onDetach()
        mHostAuth = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            mHostAuth = activity as HostAuth
        } else {
            throw RuntimeException(activity.toString() + " must implement HostRegistration interface")
        }
    }
}