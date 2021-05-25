package yelm.io.avestal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket_table")
class BasketItem(
           @PrimaryKey(autoGenerate = true) val id: Int,
           @ColumnInfo(name = "name") val name: String
           )