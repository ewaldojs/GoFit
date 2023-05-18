package com.ewaldo.gofit

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        getSupportActionBar()?.hide()

        var role = sharedPreferences.getString("role",null)
//      var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)

//        val homeFragment = HomeMemberFragment()
//


        if(role == "member"){
            setThatFragments(FragmentMember())
            nav_view.setOnItemSelectedListener {
                when(it){
                    R.id.nav_view ->{
                        setThatFragments(FragmentMember())
                    }
                }
                true
            }
        }

        if(role == "manager operational"){
            setThatFragments(FragmentMO())
            nav_view.setOnItemSelectedListener {
                when(it){
                    R.id.nav_view ->{
                        setThatFragments(FragmentMO())
                    }
                }
                true
            }
        }

        if(role == "instruktur"){
            setThatFragments(FragmentInstruktur())
            nav_view.setOnItemSelectedListener {
                when(it){
                    R.id.nav_view ->{
                        setThatFragments(FragmentInstruktur())
                    }
                }
                true
            }
        }
    }

    private fun setThatFragments(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layout_fragment,fragment)
            commit()
        }
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

}