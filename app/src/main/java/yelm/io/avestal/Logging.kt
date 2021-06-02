package yelm.io.avestal

import android.util.Log

class Logging {
    companion object {
        private const val error = "AppError"
        private const val debug = "AppDebug"

        @JvmStatic
        fun logDebug(message: String?) {
            if (BuildConfig.DEBUG) {
                Log.d(debug, message!!)
            }
        }

        @JvmStatic
        fun logError(message: String?) {
            if (BuildConfig.DEBUG) {
                Log.e(error, message!!)
            }
        }
    }
}