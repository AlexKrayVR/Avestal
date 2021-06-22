package yelm.io.avestal.main.offers.offer_materials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ActivityChooseMaterialBinding
import yelm.io.avestal.main.offers.offer.adapter.OffersAdapter
import yelm.io.avestal.main.offers.offer_materials.mode.Stuff
import java.math.BigDecimal
import java.text.DecimalFormat

class ChooseMaterialActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseMaterialBinding

    lateinit var adapterMaterials: StuffAdapter
    lateinit var adapterInstruments: StuffAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listStuff = listOf(
            Stuff("4", "Кисть Dexter Pro универсал 70мм", "288", 4),
            Stuff("3", "Кисть Dexter Pro универсал 70мм", "56700", 1),
            Stuff("2", "Кисть Dexter Pro универсал 70мм", "23688", 2),
            Stuff("1", "Кисть Dexter Pro универсал 70мм", "77890", 11),
        )

        adapterMaterials = StuffAdapter(listStuff, this)
        adapterInstruments = StuffAdapter(listStuff, this)

        binding.recyclerInstrumentlRent.adapter = adapterInstruments
        binding.recyclerPurchaseOfMaterials.adapter = adapterMaterials

        getTotalPrice()

        binding.totalPrice.setOnClickListener {
            startActivity(Intent(this@ChooseMaterialActivity, TotalActivity::class.java))
        }

    }

    private fun getTotalPrice() {
        CoroutineScope(Dispatchers.IO).launch {
            val materialPrice = getMaterialsPrice()
            val instrumentPrice = getInstrumentsPrice()
            val result = (materialPrice.add(instrumentPrice)).toString()
            Logging.logDebug("result: $result")
            val formattedPrice = priceFormat.format(result.toDouble())
            val totalPrice = getString(R.string.chooseOn) + " " + formattedPrice + " " +
                    getString(R.string.ruble)
            launch(Dispatchers.Main) {
                binding.totalPrice.text = totalPrice
            }
        }
    }

    private suspend fun getMaterialsPrice(): BigDecimal {
        var price = BigDecimal("0")
        val materials = adapterMaterials.getData()
        for (item in materials) {
            price = price.add(BigDecimal(item.count).multiply(BigDecimal(item.price)))
        }
        Logging.logDebug("getMaterialsPrice: $price")

        return price
    }


    private suspend fun getInstrumentsPrice(): BigDecimal {
        var price = BigDecimal("0")
        val instruments = adapterInstruments.getData()
        for (item in instruments) {
            price = price.add(BigDecimal(item.count).multiply(BigDecimal(item.price)))
        }
        Logging.logDebug("getInstrumentsPrice: $price")

        return price
    }
}