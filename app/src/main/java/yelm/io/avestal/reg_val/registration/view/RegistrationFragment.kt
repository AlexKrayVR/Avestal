package yelm.io.avestal.reg_val.registration.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import yelm.io.avestal.databinding.FragmentRegistrationBinding
import yelm.io.avestal.reg_val.common.PhoneTextFormatter
import yelm.io.avestal.reg_val.RegVerActivity
import yelm.io.avestal.reg_val.registration.presenter.RegistrationPresenter

class RegistrationFragment : Fragment(), RegistrationView {
    lateinit var registrationPresenter: RegistrationPresenter
    private var toast: Toast? = null

    private var binding: FragmentRegistrationBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationPresenter = RegistrationPresenter(this)
        binding?.phone?.addTextChangedListener(
            PhoneTextFormatter(
                binding?.phone,
                "+7 (###) ###-##-##"
            )
        )
        val phone = arguments?.getString(PHONE)
        binding?.phone?.setText(phone)

        binding?.further?.setOnClickListener {
            registration()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(phone: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE, phone)
                }
            }

        private const val PHONE = "PHONE"
    }

    override fun registration() {
        registrationPresenter.phoneValidation(binding?.phone?.text.toString())
    }

    override fun validationPhoneError(error: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, context?.resources?.getString(error), Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun validationPhoneSuccess(phone: String) {
        (activity as RegVerActivity).openValidationFragment(phone)
    }

    //Because Fragments continue to live after the View has gone, it’s good to remove any references to the binding class instance
    override fun onDestroyView() {
        binding = null
        registrationPresenter.detachView()
        super.onDestroyView()
    }


}