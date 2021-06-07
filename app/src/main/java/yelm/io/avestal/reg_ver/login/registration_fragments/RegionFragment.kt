package yelm.io.avestal.reg_ver.login.registration_fragments

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
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.host.HostRegistration

class RegionFragment : Fragment() {
    private var hostRegistration: HostRegistration? = null
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
            binding?.region?.setText(it.region)
        })
        binding?.further?.setOnClickListener {
            inputValidation()
        }
    }
    private fun inputValidation(){
        if (binding?.region?.text.toString().trim().isEmpty()) {
            hostRegistration?.showToast(R.string.addressEmpty)
            return
        }
        viewModel.setRegion(binding?.region?.text.toString().trim())
        hostRegistration?.openInfoFragment()
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement HostRegistration interface")
        }
    }
}