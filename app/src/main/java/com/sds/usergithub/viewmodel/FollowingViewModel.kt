package com.sds.usergithub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.sds.usergithub.UserGitHub
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel: ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserGitHub>>()

    fun setUser(user: String){
        val listItem = ArrayList<UserGitHub>()
        val url = "https://api.github.com/users/${user}/following"
        AndroidNetworking.get(url)
            .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    try {

                        for (i in 0 until response!!.length()){
                            val items = response.getJSONObject(i)
                            val username = items.getString("login")
                            val profile = items.getString("avatar_url")
                            listItem.add(UserGitHub(null, username, null, profile, null, null, null, null, null))
                        }
                        listUser.postValue(listItem)
                    }catch (e: Exception){
                        Log.d("onResponse:", e.message.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("onError", anError.toString())
                }
            })
    }
    fun getUser(): LiveData<ArrayList<UserGitHub>> {
        return listUser
    }
}