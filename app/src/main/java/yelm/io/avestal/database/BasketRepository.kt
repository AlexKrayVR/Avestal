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

}