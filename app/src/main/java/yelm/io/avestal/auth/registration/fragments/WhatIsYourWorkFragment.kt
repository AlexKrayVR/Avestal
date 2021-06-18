package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentWhatIsYourWorkBinding
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.host.HostAuth

class WhatIsYourWorkFragment : Fragment() {

    private var binding: FragmentWhatIsYourWorkBinding? = null
    private lateinit var viewModel: UserViewModel
    private var mHostAuth: HostAuth? = null

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
        binding?.further?.setOnClickListener {
            mHostAuth?.openFullNameFragment()
        }
    }

    companion object {
        fun newInstance() = WhatIsYourWorkFragment()
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
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mHostAuth = null
    }
}