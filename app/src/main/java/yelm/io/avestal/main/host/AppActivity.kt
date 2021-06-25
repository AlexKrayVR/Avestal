package yelm.io.avestal.main.host

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.common.LOCATION_PERMISSIONS
import yelm.io.avestal.common.LOCATION_PERMISSION_REQUEST_CODE
import yelm.io.avestal.database.BasketItemModelFactory
import yelm.io.avestal.database.BasketItemViewModel
import yelm.io.avestal.database.BasketRepository
import yelm.io.avestal.database.BasketRoomDatabase
import yelm.io.avestal.databinding.ActivityAppBinding
import yelm.io.avestal.main.model.UserInfoModel
import yelm.io.avestal.rest.responses.user.UserInfo

class AppActivity : AppCompatActivity(), AppHost {

    private lateinit var binding: ActivityAppBinding
    private lateinit var badges: BadgeDrawable
    private lateinit var navView: BottomNavigationView

    private lateinit var db: BasketRoomDatabase
    private lateinit var basketItemViewModel: BasketItemViewModel
    private lateinit var repository: BasketRepository
    private var toast: Toast? = null

    private lateinit var viewModel: UserInfoModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BasketRoomDatabase.getDatabase(this, CoroutineScope(SupervisorJob()))
        repository = BasketRepository(db.basketItemDao())

        basketItemViewModel =
            BasketItemModelFactory(repository)
                .create(BasketItemViewModel::class.java)

        basketItemViewModel.allItems.observe(this, { items ->
            items?.let {
                setBadges(it.size)
            }
        })

        val userInfo = intent.extras?.get(UserInfo::class.java.name) as UserInfo
        Logging.logDebug("userInfo: ${userInfo.toString()}")
        viewModel = ViewModelProvider(this).get(UserInfoModel::class.java)
        viewModel.setUserInfo(userInfo)

        initNavigation()
        initBadges()
    }

    override fun isVerified(): Boolean {
        return viewModel.getUserInfo()?.isVerified == true
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
        badges.backgroundColor = ContextCompat.getColor(this, R.color.mainColor)
    }

    override fun setBadges(count: Int) {
        if (count == 0) {
            badges.isVisible = false
        } else {
            badges.number = count
            badges.isVisible = true
        }
    }

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun getDBRepository(): BasketRepository {
        return repository
    }






}