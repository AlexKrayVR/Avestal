package yelm.io.avestal.auth.host

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.auth.login.LoginFragment
import yelm.io.avestal.auth.registration.common.finishFragmentTag
import yelm.io.avestal.auth.registration.common.whatIsYourWorkFragmentTag
import yelm.io.avestal.auth.registration.fragments.*
import yelm.io.avestal.auth.verification.view.VerificationFragment
import yelm.io.avestal.common.CAMERA_PERMISSIONS
import yelm.io.avestal.common.READ_WRITE_EXTERNAL_PERMISSIONS
import yelm.io.avestal.common.REQUEST_PERMISSIONS_CAMERA_CODE
import yelm.io.avestal.common.REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE
import yelm.io.avestal.databinding.RegVerActivityBinding
import yelm.io.avestal.main.host.AppActivity
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AccessToken
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.responses.user.UserInfo

class AuthActivity : AppCompatActivity(), HostAuth {
    private lateinit var binding: RegVerActivityBinding

    private var toast: Toast? = null

    private val whatIsYourWorkFragment: Fragment? = null
    private val fullNameFragment: Fragment? = null
    private val regionFragment: Fragment? = null
    private val infoFragment: Fragment? = null
    private val userPhotoFragment: Fragment? = null
    private val confirmUserFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegVerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesSetting.initSharedPreferencesSettings(this)

        //todo test
            //openFullNameFragment()
        checkUser()
    }

    /**
     * check if app knows that user registered by phone
     * it is saved in app settings - key: USER_PHONE
     */
    private fun checkUser() {
        if (SharedPreferencesSetting.getDataString(SharedPreferencesSetting.USER_PHONE).isEmpty()) {
            openLoginFragment()
        } else {
            getBearerToken(SharedPreferencesSetting.getDataString(SharedPreferencesSetting.USER_PHONE))
        }
    }

    /**
     * get all info about user: fio, rating, photos etc.
     * thereafter start main app and transfer user data
     */
    private fun getUserInfo() {
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getUserInfo("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<UserInfo?> {
                override fun onResponse(
                    call: Call<UserInfo?>,
                    response: Response<UserInfo?>
                ) {
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                startApp(it)
                            } else {
                                showToast(R.string.serverError)
                            }
                        }
                    } else {
                        showToast(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    showToast(R.string.serverError)
                }
            })
    }

    /**
     * get bearer token for another query of application (lives 24h)
     * save it in app settings - key: BEARER_TOKEN
     *
     * if response success - call method getUserInfo()
     * otherwise show toast with error
     */
    private fun getBearerToken(phone: String) {
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getAccessToken(
                phone
            )
            .enqueue(object : Callback<AccessToken> {
                override fun onResponse(
                    call: Call<AccessToken>,
                    response: Response<AccessToken>
                ) {
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                Logging.logDebug("BearerToken: ${it.accessToken}")
                                SharedPreferencesSetting.setData(
                                    SharedPreferencesSetting.BEARER_TOKEN,
                                    it.accessToken
                                )
                                getUserInfo()
                            } else {
                                showToast(R.string.serverError)
                            }
                        }
                    } else {
                        showToast(R.string.serverError)
                        Logging.logError(
                            "Method getBearerToken() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Logging.logError("Method getBearerToken() - failure: $t")
                    showToast(R.string.serverError)
                }
            })
    }

    override fun openLoginFragment() {
        val registrationFragment: Fragment = LoginFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .addToBackStack("login")
            .add(R.id.container, registrationFragment)
            .commit()
    }

    override fun openVerificationFragment(response: AuthResponse) {
        val validationFragment: Fragment = VerificationFragment.newInstance(response)
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .addToBackStack(null)
            .add(R.id.container, validationFragment)
            .commit()
    }

    /**
     * set animation of fragment transaction
     * by default swap to right at opening
     * and swap to left at closing
     */
    private fun setFragmentTransactionAnimation(transaction: FragmentTransaction) {
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_right
        )
    }

    /**
     * start main app and pass UserInfo
     */
    override fun startApp(userInfo: UserInfo) {
        val intent = Intent(this, AppActivity::class.java)
        intent.putExtra(UserInfo::class.java.name, userInfo)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        Logging.logDebug("size: ${fragmentList.size}")

        if (fragmentList.size == 1) {
            finish()
        }
        for (fragment in fragmentList) {
            if (fragment.tag == finishFragmentTag) {
                finish()
            }
        }
        supportFragmentManager.popBackStack()
    }

    override fun openWhatIsYourWorkFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(
                R.id.container,
                whatIsYourWorkFragment ?: WhatIsYourWorkFragment.newInstance(),
                whatIsYourWorkFragmentTag
            )
            .addToBackStack(null)
            .commit()
    }

    override fun openFullNameFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(R.id.container, fullNameFragment ?: FullNameFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun openFinishFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(
                R.id.container,
                FinishFragment.newInstance(),
                finishFragmentTag
            )
            .addToBackStack(null)
            .commit()
    }

    override fun openRegionFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(R.id.container, regionFragment ?: RegionFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun openInfoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(R.id.container, infoFragment ?: InfoFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun openPassportSelfieUserFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(R.id.container, confirmUserFragment ?: PassportSelfieUserFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun openProfilePhotoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        setFragmentTransactionAnimation(transaction)
        transaction
            .add(R.id.container, userPhotoFragment ?: UserProfilePhotoFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun requestCameraPermissions() {
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSIONS,
                REQUEST_PERMISSIONS_CAMERA_CODE
            )
        }
    }

    override fun back() {
        this.onBackPressed()
    }

    override fun closeActivity() {
        finish()
    }

    override fun requestReadExternalStoragePermission() {
        if (!hasReadExternalStoragePermission()) {
            ActivityCompat.requestPermissions(
                this,
                READ_WRITE_EXTERNAL_PERMISSIONS,
                REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE
            )
        }
    }

    override fun hasCameraPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                this,
                CAMERA_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun hasReadExternalStoragePermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                this,
                READ_WRITE_EXTERNAL_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_CAMERA_CODE) {
            if (hasCameraPermission()) {
                Logging.logDebug("Permission CAMERA added")
            } else {
                Logging.logDebug("Permission CAMERA denied")
            }
        } else if (requestCode == REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE) {
            if (hasReadExternalStoragePermission()) {
                Logging.logDebug("Permission Storage added")
            } else {
                Logging.logDebug("Permission Storage denied")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}