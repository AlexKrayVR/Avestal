package yelm.io.avestal.main.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.database.*
import yelm.io.avestal.databinding.ActivityAppBinding
import java.util.ArrayList

class AppActivity : AppCompatActivity(), BadgeInterface {

    private lateinit var binding: ActivityAppBinding
    private lateinit var badges: BadgeDrawable
    private lateinit var navView: BottomNavigationView

    private lateinit var db: BasketRoomDatabase
    private lateinit var basketItemViewModel: BasketItemViewModel
    lateinit var repository: BasketRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BasketRoomDatabase.getDatabase(this, CoroutineScope(SupervisorJob()))
        repository = BasketRepository(db.basketItemDao())

        basketItemViewModel =
            BasketItemModelFactory(repository!!)
                .create(BasketItemViewModel::class.java)
        basketItemViewModel.allItems.observe(this, { items ->
            items?.let {
                setBadges(it.size)
            }
        })

        initNavigation()
        initBadges()

    }

    private fun initNavigation() {
        navView = binding.bottomNavigationView
        val navController = findNavController(R.id.navHostFragment)
        navView.setupWithNavController(navController)
    }

    private fun initBadges() {
        badges = navView.getOrCreateBadge(R.id.navigation_basket)
        badges.isVisible = false
        badges.maxCharacterCount = 3
        badges.backgroundColor = resources.getColor(R.color.mainColor)
    }

    override fun setBadges(count: Int) {
        if (count == 0) {
            badges.isVisible = false
        } else {
            badges.number = count
            badges.isVisible = true
        }
    }

    override fun getDBRepository():BasketRepository {
        return repository
    }




}