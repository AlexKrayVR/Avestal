package yelm.io.avestal.main.services.service.host

import yelm.io.avestal.main.services.service.common.DataWrapper

interface HostService {
    fun openServiceInfo()
    fun back()
    fun closeActivity()
    fun openServiceMaterials()
    fun openServiceTotal(id: String, dataWrapper: DataWrapper)
}