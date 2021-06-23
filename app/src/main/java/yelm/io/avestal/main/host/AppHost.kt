package yelm.io.avestal.main.host

import yelm.io.avestal.database.BasketRepository

interface AppHost {
    fun setBadges(count: Int)
    fun isVerified():Boolean
    fun getDBRepository():BasketRepository
}