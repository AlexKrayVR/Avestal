package yelm.io.avestal.reg_val.verification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.databinding.FragmentVerificationBinding
import yelm.io.avestal.reg_val.RegVerActivity

class VerificationFragment : Fragment(), OnBackPressedListener {

    private var binding: FragmentVerificationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phone = arguments?.getString(PHONE)
    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String) =
            VerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)

                }
            }
        private const val PHONE = "PHONE"
    }

    override fun doBack() {
        arguments!!.getString(PHONE)?.let {
            (activity as RegVerActivity).openRegistrationFragment(it)
        }
    }


}