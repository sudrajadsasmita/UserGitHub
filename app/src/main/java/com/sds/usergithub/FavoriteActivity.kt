package com.sds.usergithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.usergithub.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        showTextView(true)

        rv_userFav.layoutManager = LinearLayoutManager(this)
        rv_userFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
    }
    fun showTextView(state: Boolean){
        if (state) {
            tv_information.visibility = View.VISIBLE
        } else {
            tv_information.visibility = View.GONE
        }
    }
}