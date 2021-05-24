package yelm.io.avestal.reg_val.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import yelm.io.avestal.R
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
        val description = context?.getString(R.string.enterPhoneNumber) + " " + phone
        binding?.description?.text = description

        binding?.first?.let { setFocusChangeListener(it) }
        binding?.second?.let { setFocusChangeListener(it) }
        binding?.third?.let { setFocusChangeListener(it) }
        binding?.fourth?.let { setFocusChangeListener(it) }
    }

    private fun setFocusChangeListener(editText: EditText) {
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                editText.background = context?.let {
                    ContextCompat.getDrawable(it, R.drawable.back_enter)
                }
            } else {
                editText.background = context?.let {
                    ContextCompat.getDrawable(it, R.drawable.back_enter_under_focus)
                }
            }
        }

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