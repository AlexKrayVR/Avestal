package yelm.io.avestal.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob



class BasketItemApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { BasketRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BasketRepository(database.basketItemDao()) }

}