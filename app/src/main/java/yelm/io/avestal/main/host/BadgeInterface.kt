package yelm.io.avestal.main.host

import yelm.io.avestal.database.BasketRepository

interface BadgeInterface {
    fun setBadges(count: Int) {
    }
    fun getDBRepository():BasketRepository
}