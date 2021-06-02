package yelm.io.avestal.reg_ver.registration.registration_fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import yelm.io.avestal.databinding.InfoFragmentBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration

class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
    }

    private lateinit var viewModel: UserViewModel
    private var hostRegistration: HostRegistration? = null
    private var binding: InfoFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InfoFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding?.further?.setOnClickListener {
            if (binding?.userInfo?.text.toString().trim().isEmpty()) {
                hostRegistration?.showToast(R.string.infoEmpty)
            }else{
                viewModel.setInfo(
                    binding?.userInfo?.text.toString().trim()
                )
                hostRegistration?.openProfilePhotoFragment()
            }
        }
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
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }
}