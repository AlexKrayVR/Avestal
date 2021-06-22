package yelm.io.avestal.main.offers.offer.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yelm.io.avestal.databinding.ActivityChooseMaterialBinding

class ChooseMaterialActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseMaterialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}