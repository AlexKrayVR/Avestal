package yelm.io.avestal.auth.model

data class User(
    var phone: String = "",
    var jobStatus: String = "",
    var name: String = "",
    var surname: String = "",
    var lastName: String = "",
    var region: String = "",
    var jobDescription: String = "",
    var profilePhoto: String = "",
    var passportPhoto: String = "",
    var selfie: String = "",
    var accessToken: String = ""
)