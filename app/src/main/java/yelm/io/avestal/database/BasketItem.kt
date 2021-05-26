package yelm.io.avestal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket_table")
class BasketItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "itemID") val itemID: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "count") val count: Int
)