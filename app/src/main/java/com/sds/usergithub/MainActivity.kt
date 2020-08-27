package com.sds.usergithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sds.usergithub.adapter.UserGitHubAdapter
import com.sds.usergithub.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val list = ArrayList<UserGitHub>()
    private lateinit var userGitHubAdapter: UserGitHubAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading(false)
        showTextView(true)
        list.clear()
        userGitHubAdapter = UserGitHubAdapter()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        edt_search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                getUserData(p0.toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        if (item.itemId == R.id.favourite){
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getUserData(name: String){
        showLoading(true)
        showTextView(false)
        val user = ArrayList<UserGitHub>()
        if (name == ""){
            showLoading(false)
            showTextView(true)
            showRecycleView(user)
        }else{
            mainViewModel.setUser(name)
            mainViewModel.getUser().observe(this@MainActivity, Observer { cek ->
                if (cek != null){
                    showLoading(false)
                    showTextView(false)
                    showRecycleView(cek)
                }
            })
        }
    }
    private fun showRecycleView(list : ArrayList<UserGitHub>){
        showLoading(false)
        rv_user.layoutManager  = LinearLayoutManager(this@MainActivity)
        rv_user.adapter = userGitHubAdapter
        userGitHubAdapter.setData(list)
        userGitHubAdapter.setOnItemClickCallback(object : UserGitHubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserGitHub) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
    private fun showTextView(state: Boolean){
        if (state) {
            tv_app.visibility = View.VISIBLE
        } else {
            tv_app.visibility = View.GONE
        }
    }
}