package yelm.io.avestal.order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import yelm.io.avestal.databinding.ActivityOrderBinding;

public class OrderActivity extends AppCompatActivity {
    ActivityOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}