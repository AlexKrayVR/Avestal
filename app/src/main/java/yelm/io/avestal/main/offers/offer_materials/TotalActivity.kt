package yelm.io.avestal.main.offers.offer_materials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import yelm.io.avestal.R
import yelm.io.avestal.databinding.ActivityTotalBinding
import yelm.io.avestal.main.offers.offer_materials.mode.Stuff

class TotalActivity : AppCompatActivity() {

    lateinit var adapter: StuffTableAdapter

    lateinit var binding: ActivityTotalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTotalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listStuff = listOf(
            Stuff("4", "Кисть Dexter Pro универсал 70мм", "288", 4),
            Stuff("3", "Кисть Dexter Pro универсал 70мм", "56700", 1),
            Stuff("2", "Кисть Dexter Pro универсал 70мм", "23688", 2),
            Stuff("1", "Кисть Dexter Pro универсал 70мм", "77890", 11),
        )
        adapter = StuffTableAdapter(listStuff, this)
        binding.recyclerStuff.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_stuff_table)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }

        binding.recyclerStuff.addItemDecoration(dividerItemDecoration)


    }
}