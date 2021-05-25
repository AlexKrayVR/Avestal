package yelm.io.avestal.main.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.observeOn
import yelm.io.avestal.Logging
import yelm.io.avestal.database.*
import yelm.io.avestal.databinding.FragmentBasketBinding


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

        val applicationScope = CoroutineScope(SupervisorJob())
        val db = BasketRoomDatabase.getDatabase(requireActivity(), applicationScope)
        val repository = BasketRepository(db.basketItemDao())

        basketItemViewModel =
            BasketItemModelFactory(repository)
                .create(BasketItemViewModel::class.java)


        basketItemViewModel.allItems.observe(requireActivity(), Observer { items ->
            items?.let {
                //adapter.submitList(it)
                Logging.logDebug("size: ${it.size}")
            }
        })
        binding?.button?.setOnClickListener {
            val basketItem = BasketItem(0, "324")
            basketItemViewModel.insert(basketItem)
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