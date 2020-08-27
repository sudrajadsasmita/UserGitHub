package com.sds.usergithub


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sds.usergithub.adapter.SectionPagerAdapter
import com.sds.usergithub.db.UserHelper
import kotlinx.android.synthetic.main.activity_detail.*
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

        val url = "https://api.github.com/users/${userGitHub.username}"
        AndroidNetworking.get(url)
            .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    try {
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
                        val repoUrl = response.getString("repos_url")
                        AndroidNetworking.get(repoUrl)
                            .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
                            .build()
                            .getAsJSONArray(object : JSONArrayRequestListener{
                                override fun onResponse(response: JSONArray?) {
                                    tv_detailRepo.text = response!!.length().toString()
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e("onError", anError.toString())
                                }
                            })
                    }catch (e: Exception){
                        Log.d("onResponse:", e.message.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("onError", anError.toString())
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