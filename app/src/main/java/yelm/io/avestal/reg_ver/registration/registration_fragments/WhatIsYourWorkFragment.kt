package yelm.io.avestal.reg_ver.registration.registration_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentWhatIsYourWorkBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration

class WhatIsYourWorkFragment : Fragment() {

    private var binding: FragmentWhatIsYourWorkBinding? = null
    private lateinit var viewModel: UserViewModel
    private var hostRegistration: HostRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWhatIsYourWorkBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        viewModel.user.observe(requireActivity(), Observer {
            Logging.logDebug(it.toString())
        })

        binding?.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            binding?.further?.isEnabled = true
            when (checkedId) {
                R.id.workMyself -> {
                    viewModel.setWorkType("workMyself")
                }
                R.id.workCompany -> {
                    viewModel.setWorkType("workCompany")
                }
            }
        }

        binding?.further?.setOnClickListener{
            hostRegistration?.openFullNameFragment()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WhatIsYourWorkFragment().apply {
                arguments = Bundle().apply {

                }
            }
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
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

}