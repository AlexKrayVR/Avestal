package yelm.io.avestal.main.user.region

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yelm.io.avestal.databinding.ActivityPickPhotoBinding
import yelm.io.avestal.databinding.ActivityRegionBinding

class RegionActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }
    }
}