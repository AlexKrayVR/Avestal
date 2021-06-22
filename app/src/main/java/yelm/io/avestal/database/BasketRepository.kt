package yelm.io.avestal.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BasketRepository(private val basketItemDao: BasketItemDao) {

    val allItems: Flow<List<BasketItem>> = basketItemDao.getBasketItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(basketItem: BasketItem) {
        basketItemDao.insert(basketItem)
    }

    @WorkerThread
    suspend fun deleteAll() {
        basketItemDao.deleteAll()
    }

    @WorkerThread
    suspend fun increase(itemID: String) {
        basketItemDao.increase(itemID)
    }

    @WorkerThread
    suspend fun reduce(itemID: String) {
        basketItemDao.reduce(itemID)
    }

    @WorkerThread
    suspend fun deleteByID(itemID: String) {
        basketItemDao.deleteByID(itemID)
    }
}