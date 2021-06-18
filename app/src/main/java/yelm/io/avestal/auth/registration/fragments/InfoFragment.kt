package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentInfoBinding
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.host.HostAuth

class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
    }

    private lateinit var viewModel: UserViewModel
    private var mHostAuth: HostAuth? = null
    private var binding: FragmentInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding?.back?.setOnClickListener {
            mHostAuth?.back()
        }

        binding?.userInfo?.setText(viewModel.user.value?.jobDescription)


        binding?.further?.setOnClickListener {
            if (binding?.userInfo?.text.toString().trim().isEmpty()) {
                mHostAuth?.showToast(R.string.infoEmpty)
            }else{
                viewModel.setInfo(
                    binding?.userInfo?.text.toString().trim()
                )
                mHostAuth?.openProfilePhotoFragment()
            }
        }
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
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }
}