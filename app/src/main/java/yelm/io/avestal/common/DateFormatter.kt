package yelm.io.avestal.common

import java.text.SimpleDateFormat
import java.util.*

var currentFormatterDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
//2021-06-03T07:38:35.000000Z

var printedFormatterDate = SimpleDateFormat("HH:mm", Locale.getDefault())

var printedFormatterDateOfferActivity = SimpleDateFormat("dd MMMM", Locale.getDefault())