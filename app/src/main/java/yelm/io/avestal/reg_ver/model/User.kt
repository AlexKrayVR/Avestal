package yelm.io.avestal.reg_ver.model

import android.net.Uri
import java.io.File

data class User(
    var workType: String = "",
    var name: String = "",
    var surname: String = "",
    var lastName: String = "",
    var region: String = "",
    var info: String = "",
    var profilePhotoUri: Uri? = null,
    var userIDPhoto: File? = null,
    var userSelfie: File? = null
)