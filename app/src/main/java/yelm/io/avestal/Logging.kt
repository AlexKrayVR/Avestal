package yelm.io.avestal

import android.util.Log

class Logging {
    companion object {
        private const val error = "AppError"
        private const val debug = "AppDebug"

        fun logDebug(message: String?) {
            if (BuildConfig.DEBUG) {
                Log.d(debug, message!!)
            }
        }

        fun logError(message: String?) {
            if (BuildConfig.DEBUG) {
                Log.e(error, message!!)
            }
        }
    }
}