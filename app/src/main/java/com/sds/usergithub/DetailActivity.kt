package com.sds.usergithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_USER = "extra_user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val actionBack = supportActionBar!!
        val userGitHub = intent.getParcelableExtra<UserGitHub>(EXTRA_USER)!!
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://api.github.com/users/${userGitHub.username}")
            .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    actionBack.title = response.getString("login")
                    tv_detailUsername.text = response.getString("login")
                    Glide.with(this@DetailActivity)
                        .load(response.getString("avatar_url"))
                        .apply(RequestOptions())
                        .into(civ_detailProfile)
                    tv_detailLocation.text = response.getString("location")
                    tv_detailName.text = response.getString("name")
                    tv_detailCompany.text = response.getString("company")
                    tv_detailFollower.text = response.getInt("followers").toString()
                    tv_detailFollowing.text = response.getInt("following").toString()
                    AndroidNetworking.get(response.getString("repos_url"))
                        .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(object : JSONArrayRequestListener{
                            override fun onResponse(response: JSONArray) {
                                tv_detailRepo.text = response.length().toString()
                            }

                            override fun onError(anError: ANError?) {
                                Log.e("onError", anError.toString())
                            }

                        })
                }

                override fun onError(anError: ANError?) {
                    Log.e("onError", anError.toString())
                }

            })
        actionBack.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
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
}