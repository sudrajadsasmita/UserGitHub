package com.sds.usergithub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.sds.usergithub.UserGitHub
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserGitHub>>()

    fun setUser(name: String){
        val lisItems = ArrayList<UserGitHub>()
        val url = "https://api.github.com/search/users?q=$name"
        AndroidNetworking.get(url)
            .addPathParameter("username", name)
            .addHeaders("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                   try {
                       val items = response!!.getJSONArray("items")
                       for (i in 0 until items.length()){
                           val item = items.getJSONObject(i)
                           val username = item.getString("login")
                           val avatar = item.getString("avatar_url")
                           lisItems.add(UserGitHub(null,username, null, avatar, null, null, null, null, null))
                       }
                       listUser.postValue(lisItems)
                   }catch (e: Exception){
                       Log.d("onResponse:", e.message.toString())
                   }
                }

                override fun onError(anError: ANError?) {
                    Log.e("onError", anError.toString())
                }

            })
    }
    fun getUser(): LiveData<ArrayList<UserGitHub>>{
        return listUser
    }
}