package yelm.io.avestal.main.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.databinding.ItemWorkTypeBinding
import java.util.*

class WorkTypeAdapter(private var items: ArrayList<TempWorkType>, var context: Context) :
    RecyclerView.Adapter<WorkTypeAdapter.WorkTypeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkTypeAdapter.WorkTypeViewHolder {
        val binding =
            ItemWorkTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkTypeViewHolder(binding)
    }

    fun addItems(items: ArrayList<TempWorkType>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WorkTypeViewHolder, position: Int) {
        val current = items[position]
        holder.binding.name.text = current.name
        fillStars(holder, current.starCount.toInt())
    }

    private fun fillStars(holder: WorkTypeViewHolder, count: Int) {
        for (i in 0 until count) {
            val iv = ImageView(context)
            when (count) {
                5 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star5
                        )
                    )
                }
                4 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star4
                        )
                    )
                }
                3 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star3
                        )
                    )
                }
                2 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star2
                        )
                    )
                }
                1 -> {
                    iv.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_star2
                        )
                    )
                }
            }
            holder.binding.layoutStars.addView(iv)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class WorkTypeViewHolder(var binding: ItemWorkTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}