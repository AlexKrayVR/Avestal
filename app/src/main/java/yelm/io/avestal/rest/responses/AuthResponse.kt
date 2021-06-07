package yelm.io.avestal.rest.responses

import java.io.Serializable

data class AuthResponse(var hash: String?, var auth: Boolean?) : Serializable
