package yelm.io.avestal.main.user.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.ActivitySettingsBinding
import yelm.io.avestal.main.user.settings.fragments.*
import yelm.io.avestal.auth.host.AuthActivity

class ActivitySettings : AppCompatActivity(), HostSettings {

    private lateinit var binding: ActivitySettingsBinding
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openSettingsFragment()
    }

    override fun openPhoneFragment(phone: String) {
        val phoneFragment: Fragment = PhoneFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction
            .add(R.id.container, phoneFragment)
            .addToBackStack("phone")
            .commit()
    }

    override fun openSettingsFragment() {
        val settingsFragment: Fragment = SettingsFragment.newInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, settingsFragment).commit()
    }

    override fun openChatNotificationFragment() {
        val chatNotificationFragment: Fragment = ChatNotificationFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction
            .add(R.id.container, chatNotificationFragment)
            .addToBackStack("chat")
            .commit()
    }

    override fun openOrderNotificationFragment() {
        val orderNotificationFragment: Fragment = OrderNotificationFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction
            .add(R.id.container, orderNotificationFragment)
            .addToBackStack("order")
            .commit()
    }

    override fun openMailFragment() {
        val mailFragment: Fragment = MailFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction
            .add(R.id.container, mailFragment)
            .addToBackStack("mail")
            .commit()
    }

    override fun logOut() {
        SharedPreferencesSetting.setData(SharedPreferencesSetting.USER_PHONE, "")
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun finishActivity() {
        finish()
    }

    override fun back() {
        this.onBackPressed()
    }

}