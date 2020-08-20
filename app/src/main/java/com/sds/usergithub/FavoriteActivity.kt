package com.sds.usergithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        showTextView(true)
    }
    fun showTextView(state: Boolean){
        if (state) {
            tv_information.visibility = View.VISIBLE
        } else {
            tv_information.visibility = View.GONE
        }
    }
}