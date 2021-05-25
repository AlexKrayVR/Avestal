package yelm.io.avestal.reg_ver

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import yelm.io.avestal.R
import yelm.io.avestal.databinding.RegVerActivityBinding
import yelm.io.avestal.main.AppActivity
import yelm.io.avestal.reg_ver.registration.view.Communicator
import yelm.io.avestal.reg_ver.registration.view.LoginFragment
import yelm.io.avestal.reg_ver.verification.OnBackPressedListener
import yelm.io.avestal.reg_ver.verification.VerificationFragment

class RegVerActivity : AppCompatActivity(), Communicator {
    private lateinit var binding: RegVerActivityBinding
    //var verificationCode = ""
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Avestal)
        binding = RegVerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openRegistrationFragment("")
    }

    override fun openRegistrationFragment(phone: String) {
        val registrationFragment: Fragment = LoginFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, registrationFragment).commit()
    }

    override  fun openValidationFragment(phone: String) {
        val validationFragment: Fragment = VerificationFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, validationFragment).commit()
    }

    override fun startApp() {
        startActivity(Intent(this, AppActivity::class.java))
        finish()
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

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

}