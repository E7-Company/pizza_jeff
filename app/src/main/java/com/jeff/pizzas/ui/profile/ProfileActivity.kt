package com.jeff.pizzas.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.jeff.pizzas.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, ProfileFragment.newInstance(), ProfileFragment.TAG)
            }
        }
    }

    fun closeActivity(view: View) {
        finish()
    }

}