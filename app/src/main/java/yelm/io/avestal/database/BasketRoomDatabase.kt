package yelm.io.avestal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(BasketItem::class), version = 1, exportSchema = false)
abstract class BasketRoomDatabase : RoomDatabase() {

    abstract fun basketItemDao(): BasketItemDao

    companion object {
        @Volatile
        private var INSTANCE: BasketRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BasketRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BasketRoomDatabase::class.java,
                    "item_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}