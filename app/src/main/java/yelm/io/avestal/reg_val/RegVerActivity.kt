package yelm.io.avestal.reg_val

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import yelm.io.avestal.R
import yelm.io.avestal.databinding.RegVerActivityBinding
import yelm.io.avestal.reg_val.registration.view.RegistrationFragment
import yelm.io.avestal.reg_val.verification.OnBackPressedListener
import yelm.io.avestal.reg_val.verification.VerificationFragment

class RegVerActivity : AppCompatActivity() {
    lateinit var binding: RegVerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Avestal)
        binding = RegVerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openRegistrationFragment("")
    }

    fun openRegistrationFragment(phone: String) {
        val registrationFragment: Fragment = RegistrationFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, registrationFragment).commit()
    }

    fun openValidationFragment(phone: String) {
        val validationFragment: Fragment = VerificationFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, validationFragment).commit()
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is OnBackPressedListener) {
                (fragment as OnBackPressedListener).doBack()
                return
            }
        }
        finish()
    }

}