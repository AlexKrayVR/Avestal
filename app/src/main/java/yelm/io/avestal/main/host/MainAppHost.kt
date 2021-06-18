package yelm.io.avestal.main.host

import yelm.io.avestal.database.BasketRepository

interface MainAppHost {
    fun setBadges(count: Int)
    fun getDBRepository():BasketRepository
}