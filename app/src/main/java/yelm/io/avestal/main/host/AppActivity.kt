package yelm.io.avestal.main.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import yelm.io.avestal.R
import yelm.io.avestal.database.BasketItemApplication
import yelm.io.avestal.database.BasketItemModelFactory
import yelm.io.avestal.database.BasketItemViewModel
import yelm.io.avestal.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity(), BadgeInterface {

    private lateinit var binding: ActivityAppBinding
    lateinit var badges: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.navHostFragment)
        navView.setupWithNavController(navController)
        badges = navView.getOrCreateBadge(R.id.navigation_basket)
        badges.isVisible = false

    }

    override fun setBadges(count: Int) {
        if (count == 0) {
            badges.isVisible = false
        } else {
            badges.number = count
            badges.isVisible = true
        }
    }

}