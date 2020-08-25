package com.sds.usergithub

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sds.usergithub.adapter.SectionPagerAdapter
import com.sds.usergithub.db.DatabaseContract
import com.sds.usergithub.db.UserHelper
import com.sds.usergithub.helper.MappingHelper
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {
    private lateinit var userHelper: UserHelper
    private lateinit var username: String
    private lateinit var avatar: String
    private var id: Int? = null
    private var statusFavorite = true
    companion object{
        const val EXTRA_USER = "extra_user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val actionBack = supportActionBar!!
        val userGitHub = intent.getParcelableExtra<UserGitHub>(EXTRA_USER)!!

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${userGitHub.username}"
        client.addHeader("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val response = JSONObject(result)
                    id = response.getInt("id")
                    username = response.getString("login")
                    avatar = response.getString("avatar_url")
                    actionBack.title = username
                    tv_detailUsername.text = username
                    Glide.with(this@DetailActivity)
                        .load(avatar)
                        .apply(RequestOptions())
                        .into(civ_detailProfile)
                    tv_detailLocation.text = response.getString("location")
                    tv_detailName.text = response.getString("name")
                    tv_detailCompany.text = response.getString("company")
                    tv_detailFollower.text = response.getInt("followers").toString()
                    tv_detailFollowing.text = response.getInt("following").toString()
                    val subClient = AsyncHttpClient()
                    val repoUrl = response.getString("repos_url")
                    subClient.addHeader("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
                    subClient.addHeader("User-Agent", "request")
                    subClient.get(repoUrl, object : AsyncHttpResponseHandler(){
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseBody: ByteArray?
                        ) {
                            try {
                                val subResult = String(responseBody!!)
                                val subResponse = JSONArray(subResult)
                                tv_detailRepo.text = subResponse.length().toString()
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

        actionBack.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(
            this,
            supportFragmentManager
        )
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)
        actionBack.elevation=0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setStatusFavorite(statusFavorite: Boolean){
        if (statusFavorite){
            fab.setImageResource(R.drawable.ic_favorite_true)
        }else{
            fab.setImageResource(R.drawable.ic_favorite_false)
        }
    }
}