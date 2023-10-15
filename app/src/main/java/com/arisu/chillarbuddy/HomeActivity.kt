package com.arisu.chillarbuddy



import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.arisu.chillarbuddy.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



class HomeActivity : AppCompatActivity() {


    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        replaceFregment(HomeFragment())

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {

            when ( it.itemId) {

                R.id.home -> replaceFregment(HomeFragment())
                R.id.refer -> replaceFregment(ReferFragment())
                R.id.setting -> replaceFregment(SettingsFragment())


                else -> {

                }

            }
            true


        }
    }

    private fun  replaceFregment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransition=fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container,fragment)
        fragmentTransition.commit()


    }

}