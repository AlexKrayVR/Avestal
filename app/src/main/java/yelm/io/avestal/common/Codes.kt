package yelm.io.avestal.common

import android.Manifest

const val REQUEST_PERMISSIONS_CAMERA_CODE = 10
val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val USER_PASSPORT_IMAGE_REQUEST_CODE = 100
const val USER_SELFIE_IMAGE_REQUEST_CODE = 1000

const val REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE = 200
const val PROFILE_IMAGE_REQUEST_CODE = 20
val READ_WRITE_EXTERNAL_PERMISSIONS = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

const val LOCATION_PERMISSION_REQUEST_CODE = 30
val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)


