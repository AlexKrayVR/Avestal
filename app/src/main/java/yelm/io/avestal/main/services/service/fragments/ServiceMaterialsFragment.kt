package yelm.io.avestal.main.services.service.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.databinding.FragmentServiceMaterialsBinding
import yelm.io.avestal.main.services.service.common.DataWrapper
import yelm.io.avestal.main.services.service.host.HostService
import yelm.io.avestal.main.services.service.adapters.StuffAdapter
import yelm.io.avestal.rest.responses.service.ServiceData
import yelm.io.avestal.rest.responses.service.ServiceItem
import java.lang.RuntimeException
import java.math.BigDecimal


class ServiceMaterialsFragment : Fragment() {
    private lateinit var serviceData: ServiceData
    private var _binding: FragmentServiceMaterialsBinding? = null
    private val binding get() = _binding!!
    private var hostService: HostService? = null

    lateinit var adapterMaterials: StuffAdapter
    lateinit var adapterInstruments: StuffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceData = it.get(ServiceData::class.java.name) as ServiceData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceMaterialsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        binding.totalPrice.setOnClickListener {
            val items = adapterMaterials.getData() as MutableList
            items.addAll(adapterInstruments.getData())

            hostService?.openServiceTotal(serviceData.id, DataWrapper(items))

        }

        binding.back.setOnClickListener {
            hostService?.back()
        }

        getTotalPrice()

    }

    private fun initAdapter() {
        adapterMaterials = StuffAdapter(serviceData.items.filter { it.statusMaterial == "1" }
                as MutableList<ServiceItem>, requireContext())
        adapterInstruments =
            StuffAdapter(serviceData.items.filter { it.statusMaterial == "2" } as MutableList<ServiceItem>,
                requireContext())

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


    companion object {
        @JvmStatic
        fun newInstance(serviceData: ServiceData) =
            ServiceMaterialsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ServiceData::class.java.name, serviceData)
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostService) {
            hostService = activity as HostService
        } else {
            throw RuntimeException(activity.toString() + " must implement HostService interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostService = null
    }

}