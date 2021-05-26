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

    @Query("UPDATE basket_table SET count=count+1 WHERE itemID=:itemID")
    suspend fun increase(itemID: String)

    @Query("UPDATE basket_table SET count=count-1 WHERE itemID=:itemID")
    suspend fun reduce(itemID: String)

    @Query("DELETE FROM basket_table WHERE itemID=:itemID")
    suspend fun deleteByID(itemID: String)

    @Query("DELETE FROM basket_table")
    suspend fun deleteAll()
}