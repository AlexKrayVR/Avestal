package yelm.io.avestal.main.shop

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.common.LOCATION_PERMISSIONS
import yelm.io.avestal.common.LOCATION_PERMISSION_REQUEST_CODE
import yelm.io.avestal.databinding.FragmentShopBinding
import yelm.io.avestal.main.host.AppHost
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.items.ItemsObject
import java.io.IOException
import java.util.*


class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private var _appHost: AppHost? = null
    private val appHost get() = _appHost!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopFragment().apply {
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        getItems()
        getLocationPermission()
    }

    fun getItems() {
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getItems("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<ItemsObject?> {
                override fun onResponse(
                    call: Call<ItemsObject?>,
                    response: Response<ItemsObject?>
                ) {
                    hideLoading()

                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        response.body().let {
//                            if (it != null) {
//                                viewState.startApp(it)
//                            } else {
//                                viewState.serverError(R.string.serverError)
//                            }
                        }
                    } else {
                        appHost.showToast(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<ItemsObject?>, t: Throwable) {
                    hideLoading()
                    Logging.logDebug("onFailure: ${t.message}")
                    appHost.showToast(R.string.serverError)
                }
            })
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is AppHost) {
            _appHost = activity as AppHost
        } else {
            throw RuntimeException(activity.toString() + " must implement AppHost interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        _appHost = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (hasLocationPermission()) {
                Logging.logDebug("Method onRequestPermissionsResult() - Request Permissions Result: Success!")
                getUserCurrentLocation()
//            } else if (shouldShowRequestPermissionRationale(permissions[0]!!)) {
//                showDialogExplanationAboutRequestLocationPermission(getText(R.string.mainActivityRequestPermission).toString())
            } else {
                Logging.logDebug("Method onRequestPermissionsResult() - Request Permissions Result: Failed!")
                //performIfNoLocationPermission()
            }
        } else {
            super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }


    private fun getLocationPermission() {
        if (hasLocationPermission()) {
            Logging.logDebug("Method getLocationPermission() - Location permission granted")
            //getUserCurrentLocation()
        } else {
            Logging.logDebug("Method getLocationPermission() - Location permission not granted")
            requestPermissions(
                LOCATION_PERMISSIONS,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getUserCurrentLocation() {
        Logging.logDebug("Method getUserCurrentLocation()")
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 100
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval = 100
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            performIfNoLocationPermission()
        } else {
            LocationServices.getFusedLocationProviderClient(requireActivity())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun performIfNoLocationPermission() {
        Logging.logDebug("Method performIfNoLocationPermission()")
        binding.progress.visibility = View.GONE
//        if (Common.userAddressesRepository.getUserAddressesList() != null && Common.userAddressesRepository.getUserAddressesList()
//                .size() !== 0
//        ) {
//            for (userAddress in Common.userAddressesRepository.getUserAddressesList()) {
//                if (userAddress.isChecked) {
//                    binding.userCurrentAddress.setText(userAddress.address)
//                    getCategoriesWithProducts(userAddress.latitude, userAddress.longitude)
//                    break
//                }
//            }
//        } else {
//            binding.userCurrentAddress.text = getText(R.string.choose_address)
//        }
    }


    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            binding.progress.visibility = View.GONE
            val latitude = locationResult.lastLocation.latitude
            val longitude = locationResult.lastLocation.longitude

            Logging.logDebug("location updated:\nlatitude: $latitude\nlongitude: $longitude")

            val userStreet: String = getUserStreet(locationResult.lastLocation)

            Logging.logDebug("Method getUserCurrentLocation() - userStreet: $userStreet")
            LocationServices.getFusedLocationProviderClient(requireActivity())
                .removeLocationUpdates(
                    this
                )
        }
    }

    private fun getUserStreet(location: Location): String {
        var userStreet = ""
        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude, 1
            )
            if (addresses.size > 0) {
                val userCurrentAddress = addresses[0]
                userStreet =
                    ((if (userCurrentAddress.thoroughfare == null) "" else userCurrentAddress.thoroughfare)
                            + (if (userCurrentAddress.thoroughfare != null && userCurrentAddress.featureName != null) ", " else "")
                            + if (userCurrentAddress.featureName == null) "" else userCurrentAddress.featureName)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return userStreet
    }






    private fun hasLocationPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                requireActivity().applicationContext,
                LOCATION_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }



}