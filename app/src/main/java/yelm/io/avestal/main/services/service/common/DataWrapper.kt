package yelm.io.avestal.main.services.service.common

import yelm.io.avestal.rest.responses.service.ServiceItem
import java.io.Serializable

class DataWrapper(data: MutableList<ServiceItem>) :Serializable {
    private val items: MutableList<ServiceItem> = data

    fun getServiceItems(): MutableList<ServiceItem> {
        return items
    }

}