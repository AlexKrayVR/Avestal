package yelm.io.avestal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.databinding.FragmentUserPhotoBinding
import yelm.io.avestal.databinding.FullNameFragmentBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration

class UserPhotoFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentUserPhotoBinding? = null
    private var hostRegistration: HostRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserPhotoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UserPhotoFragment().apply {

            }
    }
}