package yelm.io.avestal.main.basket.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import yelm.io.avestal.Logging
import yelm.io.avestal.database.*
import yelm.io.avestal.databinding.FragmentBasketBinding
import yelm.io.avestal.main.basket.adapter.BasketAdapter
import java.util.ArrayList

class BasketFragment : Fragment() {

    private lateinit var basketItemViewModel: BasketItemViewModel
    private var binding: FragmentBasketBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = BasketRoomDatabase.getDatabase(requireActivity(), CoroutineScope(SupervisorJob()))
        val repository = BasketRepository(db.basketItemDao())

        val adapter = BasketAdapter(arrayListOf(), requireContext())

        adapter.setListener(object : BasketAdapter.Listener {
            override fun increase(itemID: String) {
                basketItemViewModel.increase(itemID)
                Logging.logDebug("increase")
            }

            override fun reduce(itemID: String) {
                basketItemViewModel.reduce(itemID)
                Logging.logDebug("reduce")
            }

            override fun deleteByID(itemID: String) {
                basketItemViewModel.deleteByID(itemID)
                Logging.logDebug("deleteByID")
            }
        })

        binding?.recyclerBasket?.adapter = adapter
        basketItemViewModel =
            BasketItemModelFactory(repository)
                .create(BasketItemViewModel::class.java)

        basketItemViewModel.allItems.observe(requireActivity(), Observer { items ->
            items?.let {
                adapter.addItems(it as ArrayList<BasketItem>)
                Logging.logDebug("size: ${it.size}")

                for(item in it){
                    Logging.logDebug("item.count: ${item.count}")
                }

            }
        })
        binding?.button?.setOnClickListener {
            val basketItem1 = BasketItem(0, "3", "name", 1)
            val basketItem2 = BasketItem(0, "4", "name", 2)
            val basketItem3 = BasketItem(0, "5", "name", 3)
            basketItemViewModel.insert(basketItem1)
            basketItemViewModel.insert(basketItem2)
            basketItemViewModel.insert(basketItem3)
        }

        binding?.clean?.setOnClickListener {
            basketItemViewModel.deleteAll()
        }

        binding?.back?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_basket_to_navigation_home)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BasketFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}

