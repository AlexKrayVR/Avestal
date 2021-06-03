package yelm.io.avestal.main.offers.respond

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yelm.io.avestal.databinding.ActivityOfferBinding

class OfferActivity : AppCompatActivity() {

    lateinit var binding: ActivityOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }

    }
}