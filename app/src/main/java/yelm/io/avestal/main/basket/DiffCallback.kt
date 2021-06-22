package yelm.io.avestal.main.basket

import androidx.recyclerview.widget.DiffUtil
import yelm.io.avestal.database.BasketItem
import java.util.ArrayList

class DiffCallback(
    private var oldList: ArrayList<BasketItem>,
    private var newList: ArrayList<BasketItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].itemID == newList[newItemPosition].itemID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].count == newList[newItemPosition].count

    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}