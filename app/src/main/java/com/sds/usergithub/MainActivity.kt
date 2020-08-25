package com.sds.usergithub

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.sds.usergithub.adapter.UserGitHubAdapter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<UserGitHub>()
    private lateinit var userGitHubAdapter: UserGitHubAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading(false)
        showTextView(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_user)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {

                setSearchUserData(p0)
                return true
            }
        })
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
    fun setSearchUserData(name: String?){
        if (name == ""){
            list.clear()
            showTextView(true)
            showLoading(false)
            showRecycleView(list)
        }else{
            list.clear()
            showTextView(false)
            showLoading(true)
            userGitHubAdapter = UserGitHubAdapter()
            val url = "https://api.github.com/search/users?q=$name"
            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
            client.addHeader("User-Agent", "request")
            client.get(url, object :AsyncHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        val result = String(responseBody!!)
                        val responseObject = JSONObject(result)
                        val items = responseObject.getJSONArray("items")
                        for (i in 0 until items.length()){
                            val item = items.getJSONObject(i)
                            val username = item.getString("login")
                            val avatar = item.getString("avatar_url")
                            list.add(UserGitHub(null,username, null, avatar, null, null, null, null, null))
                            //Log.d("onSuccess : ", username)
                        }
                        showRecycleView(list)
                    }catch (e: Exception){
                        Log.d("onResponse:", e.message.toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Log.e("onError", error.toString())
                }
            })
        }
    }
    fun showRecycleView(list : ArrayList<UserGitHub>){
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
    fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
    fun showTextView(state: Boolean){
        if (state) {
            tv_app.visibility = View.VISIBLE
        } else {
            tv_app.visibility = View.GONE
        }
    }
}