package yelm.io.avestal.main.basket.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.database.BasketItem
import yelm.io.avestal.database.BasketItemModelFactory
import yelm.io.avestal.database.BasketItemViewModel
import yelm.io.avestal.databinding.FragmentBasketBinding
import yelm.io.avestal.main.basket.adapter.BasketAdapter
import yelm.io.avestal.main.host.BadgeInterface
import java.util.*

class BasketFragment : Fragment() {

    private lateinit var basketItemViewModel: BasketItemViewModel
    private var binding: FragmentBasketBinding? = null
    private var badgeInterface: BadgeInterface? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BasketAdapter(arrayListOf(), requireContext())

        adapter.setListener(object : BasketAdapter.Listener {
            override fun increase(itemID: String) {
                basketItemViewModel.increase(itemID)
            }

            override fun reduce(itemID: String) {
                basketItemViewModel.reduce(itemID)
            }

            override fun deleteByID(itemID: String) {
                basketItemViewModel.deleteByID(itemID)
            }
        })

        binding?.recyclerBasket?.adapter = adapter
        basketItemViewModel =
            BasketItemModelFactory(badgeInterface?.getDBRepository()!!)
                .create(BasketItemViewModel::class.java)


        basketItemViewModel.allItems.observe(requireActivity(), { items ->
            items?.let {
                adapter.addItems(it as ArrayList<BasketItem>)
                Logging.logDebug("size: ${it.size}")
                for (item in it) {
                    Logging.logDebug("itemID: ${item.itemID}, count: ${item.count}")
                }
                badgeInterface?.setBadges(it.size)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        badgeInterface = activity as BadgeInterface
    }

    override fun onDetach() {
        super.onDetach()
        badgeInterface = null
    }
}