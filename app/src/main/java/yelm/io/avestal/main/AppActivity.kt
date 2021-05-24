package yelm.io.avestal.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yelm.io.avestal.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}