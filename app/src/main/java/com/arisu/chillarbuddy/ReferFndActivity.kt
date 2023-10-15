package com.arisu.chillarbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arisu.chillarbuddy.databinding.ActivityPlayBinding
import com.arisu.chillarbuddy.databinding.ActivityReferFndBinding

class ReferFndActivity : AppCompatActivity() {
    lateinit var binding: ActivityReferFndBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReferFndBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }
    }
}