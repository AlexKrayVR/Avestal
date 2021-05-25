package yelm.io.avestal.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface BasketItemDao {

    @Query("SELECT * FROM basket_table ORDER BY id ASC")
    fun getBasketItems(): Flow<List<BasketItem>>


    @Insert()
    suspend fun insert(basketItem: BasketItem)


    @Query("DELETE FROM basket_table")
    suspend fun deleteAll()
}