package yelm.io.avestal.reg_ver.model

import android.net.Uri
import java.io.File

data class User(
    var phone: String = "",
    var jobStatus: String = "",
    var name: String = "",
    var surname: String = "",
    var lastName: String = "",
    var region: String = "",
    var jobDescription: String = "",
    var profilePhotoUri: Uri? = null,
    var passportPhoto: File? = null,
    var userSelfie: File? = null
)