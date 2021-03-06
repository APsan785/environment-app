package com.apsan.environmentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apsan.environmentapp.databinding.ActivityMainBinding
import com.hoc081098.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.root
    }
}