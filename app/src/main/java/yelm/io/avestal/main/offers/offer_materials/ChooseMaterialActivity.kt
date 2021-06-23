package yelm.io.avestal.main.offers.offer_materials

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.ActivityChooseMaterialBinding
import yelm.io.avestal.rest.responses.service.ServiceData
import yelm.io.avestal.rest.responses.service.ServiceItem
import java.math.BigDecimal

class ChooseMaterialActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseMaterialBinding

    lateinit var adapterMaterials: StuffAdapter
    lateinit var adapterInstruments: StuffAdapter
lateinit var service: ServiceData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        service = intent.extras?.get(ServiceData::class.java.name) as ServiceData

        initAdapter()

        binding.totalPrice.setOnClickListener {
            val items = adapterMaterials.getData() as MutableList
            items.addAll(adapterInstruments.getData())
            val intent = Intent(this@ChooseMaterialActivity, TotalActivity::class.java)
            intent.putExtra("items", DataWrapper(items))
            intent.putExtra("id", service.id)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            finish()
        }

        getTotalPrice()

    }

    private fun initAdapter() {

        adapterMaterials = StuffAdapter(service.items.filter { it.statusMaterial == "1" }
                as MutableList<ServiceItem>, this)
        adapterInstruments =
            StuffAdapter(service.items.filter { it.statusMaterial == "2" } as MutableList<ServiceItem>,
                this)

        binding.recyclerInstrumentlRent.adapter = adapterInstruments
        binding.recyclerPurchaseOfMaterials.adapter = adapterMaterials


        adapterMaterials.setListener(object : StuffAdapter.Listener {
            override fun changed() {
                getTotalPrice()
            }
        })

        adapterInstruments.setListener(object : StuffAdapter.Listener {
            override fun changed() {
                getTotalPrice()
            }
        })

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
            price = price.add(BigDecimal(item.quantity).multiply(BigDecimal(item.price)))
        }
        Logging.logDebug("getMaterialsPrice: $price")

        return price
    }

    private suspend fun getInstrumentsPrice(): BigDecimal {
        var price = BigDecimal("0")
        val instruments = adapterInstruments.getData()
        for (item in instruments) {
            price = price.add(BigDecimal(item.quantity).multiply(BigDecimal(item.price)))
        }
        Logging.logDebug("getInstrumentsPrice: $price")

        return price
    }
}