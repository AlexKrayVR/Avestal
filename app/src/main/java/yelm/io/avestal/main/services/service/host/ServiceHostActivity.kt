package yelm.io.avestal.main.services.service.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.ActivityHostServiceBinding
import yelm.io.avestal.main.services.service.common.DataWrapper
import yelm.io.avestal.main.services.service.fragments.ServiceInfoFragment
import yelm.io.avestal.main.services.service.fragments.ServiceMaterialsFragment
import yelm.io.avestal.main.services.service.fragments.ServiceTotalFragment
import yelm.io.avestal.rest.responses.service.ServiceData

class ServiceHostActivity : AppCompatActivity(), HostService {

    lateinit var binding: ActivityHostServiceBinding
    lateinit var serviceData: ServiceData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceData = intent.extras?.get(ServiceData::class.java.name) as ServiceData

        openServiceInfo()

    }

    override fun openServiceInfo() {
        val fragment: Fragment = ServiceInfoFragment.newInstance(serviceData)
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .addToBackStack(null)
            .add(R.id.container, fragment, "info")
            .commit()
    }

    override fun back() {
        this.onBackPressed()
    }

    override fun openServiceMaterials() {
        val fragment: Fragment = ServiceMaterialsFragment.newInstance(serviceData)
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .addToBackStack(null)
            .add(R.id.container, fragment, "materials")
            .commit()
    }

    override fun closeActivity() {
        finish()
    }

    override fun openServiceTotal(id: String, dataWrapper: DataWrapper) {
        val fragment: Fragment = ServiceTotalFragment.newInstance(id, dataWrapper)
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .addToBackStack(null)
            .add(R.id.container, fragment, "total")
            .commit()
    }

    private fun setFragmentTransactionAnimation(transaction: FragmentTransaction) {
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_right
        )
    }

    //TODO bag with fragments
    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        Logging.logDebug("size: ${fragmentList.size}")

        for (fragment in fragmentList) {
            Logging.logDebug("tag: ${fragment.tag}")
        }

        //weird logic must be 1 fragment
        if (fragmentList.size == 2) {
            finish()
        }

        supportFragmentManager.popBackStack()
    }
}