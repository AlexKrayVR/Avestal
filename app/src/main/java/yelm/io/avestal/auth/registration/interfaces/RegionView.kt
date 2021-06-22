package yelm.io.avestal.auth.registration.interfaces

import moxy.viewstate.strategy.alias.OneExecution

@OneExecution
interface RegionView : BaseRegistrationView {

    fun validationRegionError(error: Int)
    fun validationRegionSuccess(region: String)

}